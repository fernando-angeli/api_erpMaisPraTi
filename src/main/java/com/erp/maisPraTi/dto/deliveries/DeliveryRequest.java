package com.erp.maisPraTi.dto.deliveries;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class DeliveryRequest {

    private Long saleId;
    private LocalDateTime dateDelivery;

}
