package ru.sovcomcheck.back_end.checkservice.enums;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum First {
    @JsonProperty("0") FIRST_0(0),
    @JsonProperty("1") FIRST_1(1);

    private final int value;

    First(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
