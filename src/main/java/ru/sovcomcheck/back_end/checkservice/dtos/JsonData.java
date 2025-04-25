package ru.sovcomcheck.back_end.checkservice.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class JsonData {
    @JsonProperty("user")
    private String organization;

    @JsonProperty("retailPlaceAddress")
    private String address;

    @JsonProperty("userInn")
    private String inn;

    @JsonProperty("ticketDate")
    private String date;

    @JsonProperty("requestNumber")
    private String checkNumber;

    @JsonProperty("shiftNumber")
    private String shiftNumber;

    @JsonProperty("operator")
    private String cashier;

    @JsonProperty("operationType")
    private Integer operationType;

    @JsonProperty("items")
    private List<Item> items;

    @JsonProperty("nds18")
    private Long nds18;

    @JsonProperty("nds10")
    private Long nds10;

    @JsonProperty("nds0")
    private Long nds0;

    @JsonProperty("ndsNo")
    private Long ndsNo;

    @JsonProperty("totalSum")
    private Long totalSum;

    @JsonProperty("cashTotalSum")
    private Long cashTotalSum;

    @JsonProperty("ecashTotalSum")
    private Long ecashTotalSum;

    @JsonProperty("taxationType")
    private Integer taxationType;

    @JsonProperty("kktRegId")
    private String kktRegId;

    @JsonProperty("kktNumber")
    private String kktNumber;

    @JsonProperty("fiscalDriveNumber")
    private String fiscalDriveNumber;

    @JsonProperty("fiscalDocumentNumber")
    private String fiscalDocumentNumber;

    @JsonProperty("fiscalSign")
    private String fiscalSign;
}
