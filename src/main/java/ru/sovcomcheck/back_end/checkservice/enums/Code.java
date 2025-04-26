package ru.sovcomcheck.back_end.checkservice.enums;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum Code {
    @JsonProperty("0") CODE_0(0),
    @JsonProperty("1") CODE_1(1),
    @JsonProperty("2") CODE_2(2),
    @JsonProperty("3") CODE_3(3),
    @JsonProperty("4") CODE_4(4),
    @JsonProperty("5") CODE_5(5);

    private final int value;

    Code(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
