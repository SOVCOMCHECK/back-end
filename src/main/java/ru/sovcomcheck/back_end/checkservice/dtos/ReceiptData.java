package ru.sovcomcheck.back_end.checkservice.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ReceiptData {
    @JsonProperty("json")
    private JsonData json;

    @JsonProperty("html")
    private String html;
}
