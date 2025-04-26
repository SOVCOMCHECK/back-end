package ru.sovcomcheck.back_end.checkservice.dtos.classifier;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Prediction {

    @JsonProperty("predicted_category")
    private String predictedCategory;
}
