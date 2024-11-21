package com.erp.maisPraTi.service;

import com.erp.maisPraTi.dto.deliveries.DeliveryRequest;
import com.erp.maisPraTi.dto.deliveries.DeliveryResponse;
import com.erp.maisPraTi.model.Delivery;
import com.erp.maisPraTi.model.Sale;
import com.erp.maisPraTi.repository.DeliveryRepository;
import com.erp.maisPraTi.service.exceptions.ProductException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static com.erp.maisPraTi.util.EntityMapper.convertToDto;
import static com.erp.maisPraTi.util.EntityMapper.convertToEntity;

@Service
public class DeliveryService {

    @Autowired
    private DeliveryRepository repository;

    @Autowired
    private SaleService saleService;

    @Transactional
    public DeliveryResponse insert(DeliveryRequest request) {

        Sale sale = convertToEntity(saleService.findById(request.getSaleId()), Sale.class);

        if(sale.getTotalPendingDelivery().compareTo(BigDecimal.ZERO) >= 0){
            Delivery newDelivery = convertToEntity(request, Delivery.class);
            if(request.getDateDelivery() == null)
                newDelivery.setDateDelivery(LocalDateTime.now());
            newDelivery.setId(null);
            newDelivery = repository.save(newDelivery);
            return convertToDto(newDelivery, DeliveryResponse.class);
        }
        throw new ProductException("Não existem itens pendentes de entrega para esta venda.");
    }
}
