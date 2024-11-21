package com.erp.maisPraTi.dto.saleItems;

import com.erp.maisPraTi.dto.products.ProductSimpleDto;
import com.erp.maisPraTi.enums.UnitOfMeasure;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SaleItemRequestDto {

    private Long id;
    private ProductSimpleDto product;
    private Long quantitySold;
    private BigDecimal salePrice;
    private UnitOfMeasure unitOfMeasure;
    private Long quantityDelivered;
    private Long quantityPending = getQuantityPending();

}
