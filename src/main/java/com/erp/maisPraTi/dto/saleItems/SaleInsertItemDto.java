package com.erp.maisPraTi.dto.saleItems;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SaleInsertItemDto {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;
    private Long productId;
    private BigDecimal quantitySold;
    private BigDecimal salePrice;

}
