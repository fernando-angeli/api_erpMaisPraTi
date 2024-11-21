package com.erp.maisPraTi.fixture;

import com.erp.maisPraTi.enums.UnitOfMeasure;
import com.erp.maisPraTi.model.Product;
import com.erp.maisPraTi.model.Supplier;

import java.math.BigDecimal;
import java.util.List;

public class ProductFixture {

    public static Product productFixture() {
        Product product = new Product();
        product.setSupplierCode("SUP123");
        product.setName("Produto Exemplo");
        product.setDescription("Descrição do produto exemplo.");
        product.setUnitOfMeasure(UnitOfMeasure.UNIT);  // Substitua por uma unidade válida de medida
        product.setProductPrice(new BigDecimal("150.00"));
        product.setStock(new BigDecimal("50"));
        product.setReservedStock(new BigDecimal("10"));
        product.setIncomingStock(new BigDecimal("20"));
        product.setSuppliers(List.of(SupplierFixture.supplierFixture()));  // Exemplo com fornecedor associado

        return product;
    }
}
