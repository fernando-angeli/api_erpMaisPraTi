package com.erp.maisPraTi.repository;

import com.erp.maisPraTi.model.SaleItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.Optional;

public interface SaleItemRepository extends JpaRepository <SaleItem, Long> {

    Optional<SaleItem> findByIdAndSaleId(Long id, Long saleId);

    Optional<SaleItem> findByProductIdAndSaleIdAndSalePrice(Long id, Long saleId, BigDecimal salePrice);

    @Query("SELECT si FROM SaleItem si WHERE si.sale.id = :saleId")
    Page <SaleItem> findBySaleId(@Param("saleId") Long saleId, Pageable pageable);

    //@Query("SELECT si FROM SaleItem si WHERE si.sale.id = :saleId")
    Page <SaleItem> findBySaleIdAndProductId(Long saleId, Long productId, Pageable pageable);


}
