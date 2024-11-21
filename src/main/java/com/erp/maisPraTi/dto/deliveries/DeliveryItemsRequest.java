package com.erp.maisPraTi.dto.deliveries;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class DeliveryItemsRequest {

    private Long saleItemId;
    private BigDecimal quantityDelivery;

}
