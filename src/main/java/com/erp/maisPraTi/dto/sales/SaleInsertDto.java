package com.erp.maisPraTi.dto.sales;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SaleInsertDto {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;
    private LocalDate expectedDeliveryDate;
    private Long clientId;
    private Long saleNumber;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String sellerName;

}
