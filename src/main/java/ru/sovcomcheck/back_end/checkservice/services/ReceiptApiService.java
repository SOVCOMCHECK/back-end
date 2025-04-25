package ru.sovcomcheck.back_end.checkservice.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import ru.sovcomcheck.back_end.checkservice.dtos.Check;
import ru.sovcomcheck.back_end.checkservice.exceptions.CheckProcessingException;
import ru.sovcomcheck.back_end.checkservice.feign.ReceiptApiClient;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class ReceiptApiService {
    private final ReceiptApiClient receiptApiClient;
    private final ObjectMapper objectMapper;

    @Value("${receipt.api.token}")
    private String apiToken;

    public Check processReceipt(MultipartFile file) {
        try {
            // 1. Проверка входного файла
            if (file == null || file.isEmpty()) {
                throw new CheckProcessingException("QR-код не предоставлен");
            }

            // 2. Выполнение запроса через Feign Client
            Check check = receiptApiClient.processReceipt(apiToken, file);

            // 3. Валидация ответа
            if (check.getCode() == null || check.getCode().getValue() != 1) {
                throw new CheckProcessingException("Ошибка обработки чека. Код: " +
                        (check.getCode() != null ? check.getCode().getValue() : "null"));
            }

            return check;

        } catch (FeignException e) {
            try {
                String responseBody = e.contentUTF8();
                System.err.println("Feign error response: " + responseBody);

                if (StringUtils.hasText(responseBody)) {
                    return objectMapper.readValue(responseBody, Check.class);
                }
                throw new CheckProcessingException("Ошибка при обработке QR-кода: " + e.getMessage());
            } catch (IOException ex) {
                throw new CheckProcessingException("Ошибка при обработке ответа: " + ex.getMessage());
            }
        } catch (Exception e) {
            throw new CheckProcessingException("Ошибка при обработке QR-кода: " + e.getMessage());
        }
    }
}
