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
public class SaleItemResponseDto {

    private Long id;
    private Long saleId;
    private ProductSimpleDto product;
    private BigDecimal quantitySold;  // Alterado para BigDecimal
    private BigDecimal salePrice;
    private UnitOfMeasure unitOfMeasure;
    private Long quantityDelivered;
    private Long quantityPending = getQuantityPending();

}
