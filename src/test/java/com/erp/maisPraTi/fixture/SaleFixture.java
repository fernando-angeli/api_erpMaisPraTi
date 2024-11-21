package com.erp.maisPraTi.fixture;

import com.erp.maisPraTi.enums.SaleStatus;
import com.erp.maisPraTi.model.Client;
import com.erp.maisPraTi.model.Sale;
import com.erp.maisPraTi.model.SaleItem;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;

public class SaleFixture {

    public static Sale saleFixture() {
        // Criando um cliente simples de exemplo
        Client client = new Client();
        client.setId(1L);
        client.setFullName("Cliente Teste");
        client.setEmail("cliente@teste.com");

        // Criando itens de venda (SaleItem)
        SaleItem item1 = new SaleItem();
        item1.setId(1L);
        item1.setSalePrice(new BigDecimal(100.00));
        item1.setQuantitySold(new BigDecimal(2));

        SaleItem item2 = new SaleItem();
        item2.setId(2L);
        item2.setSalePrice(new BigDecimal(50.00));
        item2.setQuantitySold(new BigDecimal(3));

        // Criando a venda
        Sale sale = new Sale();
        sale.setSaleNumber(123456L);
        sale.setSaleDate(LocalDateTime.now());
        sale.setExpectedDeliveryDate(LocalDate.now().plusDays(7));  // Data de entrega estimada
        sale.setClient(client);
        sale.setSaleItems(Arrays.asList(item1, item2));
        sale.setSaleStatus(SaleStatus.PENDING);

        return sale;
    }

}