package ru.sovcomcheck.back_end.checkservice.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.sovcomcheck.back_end.checkservice.enums.Code;
import ru.sovcomcheck.back_end.checkservice.enums.First;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Check {
    @JsonProperty("id")
    private String id;

    @JsonProperty("userId")
    private String userId;

    @JsonProperty("isApplied")
    private Boolean isApplied;

    @JsonProperty("imageUrl")
    private String imageUrl;

    @JsonProperty("code")
    private Code code;

    @JsonProperty("first")
    private First first;

    @JsonProperty("data")
    private ReceiptData data;

    @JsonProperty("request")
    private RequestDetails request;
}
