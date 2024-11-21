package com.erp.maisPraTi.service;

import com.erp.maisPraTi.dto.deliveries.DeliveryItemsRequest;
import com.erp.maisPraTi.dto.deliveries.DeliveryItemsResponse;
import com.erp.maisPraTi.model.DeliveryItem;
import com.erp.maisPraTi.model.SaleItem;
import com.erp.maisPraTi.repository.DeliveryItemRepository;
import com.erp.maisPraTi.service.exceptions.ProductException;
import com.erp.maisPraTi.service.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

import static com.erp.maisPraTi.util.EntityMapper.convertToDto;
import static com.erp.maisPraTi.util.EntityMapper.convertToEntity;

@Service
public class DeliveryItemService {

    @Autowired
    private DeliveryItemRepository repository;

    @Autowired
    private SaleService saleService;

    @Autowired
    private SaleItemService saleItemService;

    @Autowired
    private ProductService productService;

    @Transactional
    public DeliveryItemsResponse insert(Long deliveryId, DeliveryItemsRequest request) {

        SaleItem saleItem = convertToEntity(saleItemService.findById(request.getSaleItemId()), SaleItem.class);

        // Verifica se o item tem pendencia para entrega e cria um item na entrega
        verifyAvailabilityForDelivery(saleItem, request.getQuantityDelivery());

        // Cria um novo item de entrega
        DeliveryItem newDeliveryItem = convertToEntity(request, DeliveryItem.class);

        // Atualiza a quantidade entregue no item de venda
        saleItemService.updateItemDeliveryQuantity(request.getSaleItemId(), request.getQuantityDelivery());

        // Atualiza a quantidade entregue nos produtos e tira do estoque e da reserva
        productService.updateItemDeliveryQuantity(saleItem.getProduct().getId(), request.getQuantityDelivery());

        // Verifica se a quantidade entregue irá concluir as entregas da venda.
        saleService.verifySalePending(saleItem.getSale().getId(), request.getQuantityDelivery());

        newDeliveryItem = repository.save(newDeliveryItem);

        return convertToDto(newDeliveryItem, DeliveryItemsResponse.class);
    }

    private void verifyAvailabilityForDelivery(SaleItem saleItem, BigDecimal quantityDelivery){
        if(saleItem.getQuantityPending().compareTo(quantityDelivery) < 0){
            throw new ProductException("Quantidade informada não disponivel para entrega desse item.");
        }
    }

    public Optional<DeliveryItemsResponse> findById(Long deliveryId) {
        DeliveryItem item = repository.findById(deliveryId)
                .orElseThrow(() -> new ResourceNotFoundException("Item de entrega não localizado."));
        return Optional.of(convertToDto(item, DeliveryItemsResponse.class));
    }

    public Page<DeliveryItemsResponse> findAll(Long deliveryId, Pageable pageable){
        Page<DeliveryItem> items = repository.findAllByDeliveryId(deliveryId, pageable);
        return items.map(item -> convertToDto(item, DeliveryItemsResponse.class));
    }

}
