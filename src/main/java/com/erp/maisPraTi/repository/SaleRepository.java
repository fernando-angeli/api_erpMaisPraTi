package com.erp.maisPraTi.repository;

import com.erp.maisPraTi.model.Sale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface SaleRepository extends JpaRepository<Sale, Long> {

    @Query("SELECT MAX(s.saleNumber) FROM Sale s")
    Long findMaxSaleNumber();

}
