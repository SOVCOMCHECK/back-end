package ru.sovcomcheck.back_end.checkservice.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Item {
    @JsonProperty("name")
    private String name;

    @JsonProperty("price")
    private Double price;

    @JsonProperty("quantity")
    private Double quantity;

    @JsonProperty("sum")
    private Double sum;

    @JsonProperty("nds")
    private Integer ndsRate;
}
