package ru.sovcomcheck.back_end.checkservice.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ManualParams {
    @JsonProperty("fn")
    private String fn;

    @JsonProperty("fd")
    private String fd;

    @JsonProperty("fp")
    private String fp;

    @JsonProperty("check_time")
    private String checkTime;

    @JsonProperty("type")
    private Integer type;

    @JsonProperty("sum")
    private Double sum;
}
