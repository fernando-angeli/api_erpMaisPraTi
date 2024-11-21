package com.erp.maisPraTi.dto.sales;

import com.erp.maisPraTi.dto.partyDto.clients.ClientSimpleDto;
import com.erp.maisPraTi.dto.saleItems.SaleItemRequestDto;
import com.erp.maisPraTi.enums.SaleStatus;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SaleDto {

    private Long id;

    private Long saleNumber;

    private LocalDateTime saleDate;
    @NotNull(message = "Informe a data estimada para a entrega.")
    private LocalDate expectedDeliveryDate;

    private LocalDateTime saleDeliveryDate;

    @NotNull(message = "É obrigatório informar o cliente.")
    private ClientSimpleDto client;

    private List<SaleItemRequestDto> saleItems;

    private BigDecimal totalSaleValue;

    private BigDecimal getTotalPendingDelivery;

    @Enumerated(EnumType.STRING)
    private SaleStatus saleStatus;

}
