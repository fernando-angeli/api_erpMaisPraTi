package com.erp.maisPraTi.dto.deliveries;

import com.erp.maisPraTi.dto.saleItems.SaleItemResponseDto;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class DeliveryItemsResponse {

    private Long id;
    private Long deliveryId;
    private SaleItemResponseDto saleItem;
    private BigDecimal quantityDelivery;

}
