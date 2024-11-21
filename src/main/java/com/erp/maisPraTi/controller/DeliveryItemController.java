package com.erp.maisPraTi.controller;

import com.erp.maisPraTi.dto.deliveries.DeliveryItemsRequest;
import com.erp.maisPraTi.dto.deliveries.DeliveryItemsResponse;
import com.erp.maisPraTi.service.DeliveryItemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Optional;

@Tag(name = "Itens de entregas", description = "Operações relacionadas aos itens de entregas.")
@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping(value = "api/entregas")
public class DeliveryItemController {

    @Autowired
    private DeliveryItemService deliveryItemService;

    @Operation(summary = "Faz uma inserção de itens em uma entrega")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Inserção de itens feita com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    @PostMapping("{deliveryId}/itens")
    public ResponseEntity<DeliveryItemsResponse> insert (@PathVariable Long deliveryId, @Valid @RequestBody DeliveryItemsRequest request){
        DeliveryItemsResponse deliveryResponse = deliveryItemService.insert(deliveryId, request);
        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(deliveryResponse.getId())
                .toUri();
        return ResponseEntity.created(uri).body(deliveryResponse);
    }

    @Operation(summary = "Obtém um item especifico da entrega por ID do item de entrega")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Item encontrado"),
            @ApiResponse(responseCode = "404", description = "Item não encontrado")
    })
    @GetMapping("/itens/{itemId}")
    public ResponseEntity<Optional<DeliveryItemsResponse>> findByDeliveryIdAndDeliveryItemId(@PathVariable Long itemId){
        Optional<DeliveryItemsResponse> deliveryItemsResponse = deliveryItemService.findById(itemId);
        return ResponseEntity.ok().body(deliveryItemsResponse);
    }

    @Operation(summary = "Obtém uma lista de itens de uma entrega.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Itens encontrados"),
            @ApiResponse(responseCode = "404", description = "Itens não encontrados")
    })
    @GetMapping("/{deliveryId}/itens")
    public ResponseEntity<Page<DeliveryItemsResponse>> findAllBySaleId(@PathVariable Long deliveryId, Pageable pageable){
        Page<DeliveryItemsResponse> sales = deliveryItemService.findAll(deliveryId, pageable);
        return ResponseEntity.ok().body(sales);
    }

}
