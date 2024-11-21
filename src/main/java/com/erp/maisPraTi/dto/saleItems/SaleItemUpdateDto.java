package com.erp.maisPraTi.dto.saleItems;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SaleItemUpdateDto {

    private Long productId;
    private BigDecimal quantitySold;
    private BigDecimal salePrice;

}
