package com.erp.maisPraTi.dto.deliveries;

import com.erp.maisPraTi.dto.sales.SaleSimpleDto;
import com.erp.maisPraTi.model.DeliveryItem;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class DeliveryResponse {

    private Long id;
    private SaleSimpleDto sale;
    private LocalDateTime dateDelivery;
    private List<DeliveryItem> deliveryItems = new ArrayList<>();

}
