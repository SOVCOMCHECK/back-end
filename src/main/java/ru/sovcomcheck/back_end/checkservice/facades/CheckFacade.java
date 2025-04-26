package ru.sovcomcheck.back_end.checkservice.facades;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.sovcomcheck.back_end.checkservice.documents.CheckDocument;
import ru.sovcomcheck.back_end.checkservice.dtos.Check;
import ru.sovcomcheck.back_end.checkservice.dtos.CheckProcessingResponse;
import ru.sovcomcheck.back_end.checkservice.enums.CheckStatus;
import ru.sovcomcheck.back_end.checkservice.enums.ProcessingStatus;
import ru.sovcomcheck.back_end.checkservice.exceptions.CheckProcessingException;
import ru.sovcomcheck.back_end.checkservice.kafka.KafkaProducerService;
import ru.sovcomcheck.back_end.checkservice.repositories.CheckRepository;
import ru.sovcomcheck.back_end.checkservice.services.ClassifierApiService;
import ru.sovcomcheck.back_end.checkservice.services.ReceiptApiService;
import ru.sovcomcheck.back_end.notification.dto.NotificationDto;
import ru.sovcomcheck.back_end.notification.service.NotificationService;
import ru.sovcomcheck.back_end.photoservice.dtos.FileDTO;
import ru.sovcomcheck.back_end.photoservice.enums.BucketEnum;
import ru.sovcomcheck.back_end.photoservice.services.FileService;

import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Slf4j
@Service
@RequiredArgsConstructor
public class CheckFacade {
    private final FileService fileService;
    private final ReceiptApiService receiptApiService;
    private final CheckRepository checkRepository;
    private final KafkaProducerService kafkaProducerService;
    private final ClassifierApiService classifierApiService;
    private final NotificationService notificationService;

    private static final String MESSAGE_APPROVED_CHECK = "Чек от %s успешно загружен";

    @Transactional
    public CheckProcessingResponse processReceipt(String userId, MultipartFile file) {
        try {
            // 1. Сохраняем фото во временный бакет
            FileDTO tempFile = fileService.uploadFile(file, BucketEnum.TEMP_CHECKS.getBucketName());

            // 2. Отправляем на API проверки чеков
            Check check = receiptApiService.processReceipt(file);
            check.setIsApplied(false);
            check.setUserId(userId);

            String qrCodeUrl = check.getData().getHtml();
            check.getData().setHtml(qrCodeUrl.replace("/qrcode/generate", "https://proverkacheka.com/qrcode/generate"));

            // 3. Переносим фото в постоянный бакет
            FileDTO permanentFile = fileService.moveFile(
                    tempFile.getFilename(),
                    BucketEnum.TEMP_CHECKS.getBucketName(),
                    BucketEnum.CHECKS.getBucketName()
            );
            check.setImageUrl(permanentFile.getUrl());

            // 4. Сохраняем результат в БД с userId
            CheckDocument document = saveCheckDocument(check);

            return buildSuccessResponse(document);

        } catch (Exception e) {
            return buildErrorResponse(e);
        }
    }

    private CheckDocument saveCheckDocument(Check check) {
        if (check.getId() == null) {
            check.setId(new ObjectId().toString());
        }

        CheckDocument document = CheckDocument.builder()
                .id(check.getId())
                .userId(check.getUserId())
                .checkData(check)
                .status(CheckStatus.PENDING)
                .processedAt(parseCheckTime(check.getRequest().getManual().getCheckTime()))
                .build();

        return checkRepository.save(document);
    }

    @Transactional(readOnly = true)
    public Check getCheckById(String id) {
        return checkRepository.findById(id)
                .orElseThrow(() -> new CheckProcessingException("Check not found"))
                .getCheckData();
    }

    @Transactional
    public void confirmCheck(String checkId, boolean isApproved) {
        CheckDocument document = checkRepository.findById(checkId)
                .orElseThrow(() -> new CheckProcessingException("Check not found"));

        if (isApproved) {
            approveCheck(document);
        } else {
            rejectCheck(document);
        }
    }

    @Transactional(readOnly = true)
    public Page<Check> getUserChecks(String userId, Pageable pageable) {
        return checkRepository.findByUserId(userId, pageable)
                .map(CheckDocument::getCheckData);
    }

    private void approveCheck(CheckDocument document) {
        document.setStatus(CheckStatus.APPROVED);
        document.getCheckData().setIsApplied(true);
        document.setCategory(classifierApiService.predictCategory(document.getCheckData()));
        checkRepository.save(document);
        kafkaProducerService.sendCheck(document);
        notificationService.createNotification(new NotificationDto(document.getUserId(), String.format(MESSAGE_APPROVED_CHECK, formatDateString(document.getCheckData().getRequest().getManual().getCheckTime()))));
    }

    private void rejectCheck(CheckDocument document) {
        String imageUrl = document.getCheckData().getImageUrl();
        if (imageUrl != null && !imageUrl.isEmpty()) {
            try {
                String filename = extractFilename(imageUrl);
                fileService.deleteFile(filename, BucketEnum.CHECKS.getBucketName());
            } catch (Exception e) {
                log.error("Error deleting file for check {}", document.getId(), e);
            }
        }
        checkRepository.delete(document);
    }

    private String extractFilename(String urlString) {
        try {
            URL url = new URL(urlString);
            String path = url.getPath();
            return Paths.get(path).getFileName().toString();
        } catch (MalformedURLException e) {
            log.error("Malformed URL: {}", urlString, e);
            throw new IllegalArgumentException();
        }
    }

    private CheckProcessingResponse buildSuccessResponse(CheckDocument document) {
        return CheckProcessingResponse.builder()
                .checkId(document.getId())
                .checkData(document.getCheckData())
                .status(ProcessingStatus.PENDING_CONFIRMATION)
                .message("Check processed successfully")
                .build();
    }

    private CheckProcessingResponse buildErrorResponse(Exception e) {
        return CheckProcessingResponse.builder()
                .status(ProcessingStatus.FAILED)
                .message(e.getMessage())
                .build();
    }

    public static String formatDateString(String input) {
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyyMMdd't'HHmm");
        LocalDateTime dateTime = LocalDateTime.parse(input, inputFormatter);
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
        return dateTime.format(outputFormatter);
    }

    public static LocalDateTime parseCheckTime(String input) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd't'HHmm");
            return LocalDateTime.parse(input, formatter);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Неверный формат времени чека: " + input, e);
        }
    }
}
