package com.erp.maisPraTi.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Gender {

    F("FEMININO"),
    M("MASCULINO"),
    N("NAO INFORMADO");

    private final String gender;

    Gender(String gender) {
        this.gender = gender;
    }

    @JsonValue
    public String getGender(){
        return this.gender;
    }

}
