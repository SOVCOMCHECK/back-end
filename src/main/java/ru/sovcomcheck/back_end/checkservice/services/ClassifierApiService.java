package ru.sovcomcheck.back_end.checkservice.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.sovcomcheck.back_end.checkservice.dtos.Check;
import ru.sovcomcheck.back_end.checkservice.dtos.classifier.CheckMl;
import ru.sovcomcheck.back_end.checkservice.dtos.classifier.mapping.CheckMapping;
import ru.sovcomcheck.back_end.checkservice.feign.ClassifierApiClient;

@Service
@RequiredArgsConstructor
public class ClassifierApiService {
    private final ClassifierApiClient classifierApiClient;

    public String predictCategory(Check check)  {

        CheckMl dto = CheckMapping.toMl(check);
        return classifierApiClient.predictCategory(dto).getPredictedCategory();
    }
}
