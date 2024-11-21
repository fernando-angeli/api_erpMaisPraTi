package com.erp.maisPraTi.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum TypePfOrPj {

    PF("PF"),
    PJ("PJ");

    private final String typePfOrPj;

    TypePfOrPj(String typePfOrPj) {
        this.typePfOrPj = typePfOrPj;
    }

    @JsonValue
    public String getTypePfOrPj() {
        return this.typePfOrPj;
    }
}
