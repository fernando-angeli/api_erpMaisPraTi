package com.erp.maisPraTi.repository;

import com.erp.maisPraTi.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {

    boolean existsById(Long id);
}
