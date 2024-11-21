package com.erp.maisPraTi.repository;

import com.erp.maisPraTi.model.DeliveryItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeliveryItemRepository extends JpaRepository<DeliveryItem, Long> {

    Page<DeliveryItem> findAllByDeliveryId(Long deliveryId, Pageable pageable);

}
