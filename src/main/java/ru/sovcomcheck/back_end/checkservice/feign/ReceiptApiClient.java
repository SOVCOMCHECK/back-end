package ru.sovcomcheck.back_end.checkservice.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;
import ru.sovcomcheck.back_end.checkservice.config.FeignConfig;
import ru.sovcomcheck.back_end.checkservice.dtos.Check;

@FeignClient(
        name = "receiptApiClient",
        url = "https://proverkacheka.com/api/v1",
        configuration = FeignConfig.class
)
public interface ReceiptApiClient {

    @PostMapping(
            value = "/check/get",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    Check processReceipt(
            @RequestPart("token") String token,
            @RequestPart("qrfile") MultipartFile file
    );
}
