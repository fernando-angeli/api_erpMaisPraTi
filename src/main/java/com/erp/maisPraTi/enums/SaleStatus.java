package com.erp.maisPraTi.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum SaleStatus {

    PENDING("pendente"),
    DELIVERED("entregue"),
    CANCELLED("cancelada");

    private final String status;

    SaleStatus(String status){
        this.status = status;
    }

    @JsonValue
    public String getStatus(){
        return this.status;
    }

}
