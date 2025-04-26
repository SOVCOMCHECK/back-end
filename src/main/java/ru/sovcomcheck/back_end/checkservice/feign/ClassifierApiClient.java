package ru.sovcomcheck.back_end.checkservice.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.sovcomcheck.back_end.checkservice.config.FeignConfig;
import ru.sovcomcheck.back_end.checkservice.dtos.classifier.CheckMl;
import ru.sovcomcheck.back_end.checkservice.dtos.classifier.Prediction;

@FeignClient(
        name = "classifierApiClient",
        url = "http://127.0.0.1:5000",
        configuration = FeignConfig.class
)
public interface ClassifierApiClient {

    @PostMapping(
            value = "/predict",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    Prediction predictCategory(
            @RequestBody CheckMl check
    );
}

