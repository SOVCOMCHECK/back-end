package ru.sovcomcheck.back_end.checkservice.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class RequestDetails {
    @JsonProperty("qrurl")
    private String qrUrl;

    @JsonProperty("qrfile")
    private String qrFile;

    @JsonProperty("qrraw")
    private String qrRaw;

    @JsonProperty("manual")
    private ManualParams manual;
}
