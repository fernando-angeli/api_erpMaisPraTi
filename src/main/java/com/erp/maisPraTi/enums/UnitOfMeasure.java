package com.erp.maisPraTi.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum UnitOfMeasure {
    UNIT("unidade"),
    BAR("barra"),
    KG("quilo"),
    LINEAR_METER("metro linear"),
    SQUARE_METER("metro quadrado");

    private final String description;

    UnitOfMeasure(String description) {
        this.description = description;
    }

    @JsonValue
    public String getDescription() {
        return description;
    }
}