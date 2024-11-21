package com.erp.maisPraTi.fixture;

import com.erp.maisPraTi.enums.UnitOfMeasure;
import com.erp.maisPraTi.model.Product;
import com.erp.maisPraTi.model.Sale;
import com.erp.maisPraTi.model.SaleItem;

import java.math.BigDecimal;

public class SaleItemFixture {

    public static SaleItem createSaleItem() {
        SaleItem saleItem = new SaleItem();
        saleItem.setId(1L);
        saleItem.setProduct(createDefaultProduct());
        saleItem.setQuantitySold(new BigDecimal("10.00"));
        saleItem.setSalePrice(new BigDecimal("50.00"));
        saleItem.setUnitOfMeasure(UnitOfMeasure.UNIT);
        saleItem.setQuantityDelivered(new BigDecimal("5.00"));
        saleItem.setSale(createDefaultSale());
        return saleItem;
    }

    public static SaleItem createSaleItemWithCustomValues(BigDecimal quantitySold, BigDecimal salePrice, BigDecimal quantityDelivered, UnitOfMeasure unitOfMeasure) {
        SaleItem saleItem = new SaleItem();
        saleItem.setId(1L);
        saleItem.setProduct(createDefaultProduct());
        saleItem.setQuantitySold(quantitySold);
        saleItem.setSalePrice(salePrice);
        saleItem.setUnitOfMeasure(unitOfMeasure);
        saleItem.setQuantityDelivered(quantityDelivered);
        saleItem.setSale(createDefaultSale());
        return saleItem;
    }

    private static Product createDefaultProduct() {
        Product product = new Product();
        product.setId(1L);
        product.setName("Product Example");
        return product;
    }

    private static Sale createDefaultSale() {
        Sale sale = new Sale();
        sale.setId(1L);
        sale.setSaleNumber(1L);
        return sale;
    }
}

