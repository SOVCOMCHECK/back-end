package ru.sovcomcheck.back_end.checkservice.facades;

import lombok.RequiredArgsConstructor;
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
import ru.sovcomcheck.back_end.photoservice.dtos.FileDTO;
import ru.sovcomcheck.back_end.photoservice.enums.BucketEnum;
import ru.sovcomcheck.back_end.photoservice.services.FileService;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CheckFacade {
    private final FileService fileService;
    private final ReceiptApiService receiptApiService;
    private final CheckRepository checkRepository;
    private final KafkaProducerService kafkaProducerService;
    private final ClassifierApiService classifierApiService;

    @Transactional
    public CheckProcessingResponse processReceipt(MultipartFile file) {
        try {
            // 1. Сохраняем фото во временный бакет
            FileDTO tempFile = fileService.uploadFile(file, BucketEnum.TEMP_CHECKS.getBucketName());

            // 2. Отправляем на API проверки чеков
            Check check = receiptApiService.processReceipt(file); // Передаем оригинальный файл
            check.setIsApplied(false);

            // 3. Переносим фото в постоянный бакет
            FileDTO permanentFile = fileService.moveFile(
                    tempFile.getFilename(),
                    BucketEnum.TEMP_CHECKS.getBucketName(),
                    BucketEnum.CHECKS.getBucketName()
            );

            // 4. Сохраняем результат в БД
            CheckDocument document = saveCheckDocument(check, permanentFile.getUrl());

            return buildSuccessResponse(document);

        } catch (Exception e) {
            return buildErrorResponse(e);
        }
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

    private CheckDocument saveCheckDocument(Check check, String imageUrl) {
        return checkRepository.save(CheckDocument.builder()
                .minioImageUrl(imageUrl)
                .checkData(check)
                .status(CheckStatus.PENDING)
                .processedAt(LocalDateTime.now())
                .build());
    }

    private void approveCheck(CheckDocument document) {
        document.setStatus(CheckStatus.APPROVED);
        document.setConfirmedAt(LocalDateTime.now());
        document.getCheckData().setIsApplied(true);
        document.setCategory(classifierApiService.predictCategory(document.getCheckData()));
        checkRepository.save(document);
        kafkaProducerService.sendCheck(document.getCheckData());
    }

    private void rejectCheck(CheckDocument document) {
        fileService.deleteFile(
                extractFilename(document.getMinioImageUrl()),
                BucketEnum.CHECKS.getBucketName()
        );
        checkRepository.delete(document);
    }

    private String extractFilename(String url) {
        return url.substring(url.lastIndexOf('/') + 1);
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
}
