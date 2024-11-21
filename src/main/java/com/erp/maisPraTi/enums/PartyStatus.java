package com.erp.maisPraTi.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum PartyStatus {

    ACTIVE("ativo"),
    INACTIVE("inativo"),
    SUSPENDED("suspenso");

    private final String status;

    PartyStatus(String status){
        this.status = status;
    }

    @JsonValue
    public String getStatus(){
        return this.status;
    }
}
