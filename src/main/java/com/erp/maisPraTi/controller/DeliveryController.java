package com.erp.maisPraTi.controller;

import com.erp.maisPraTi.dto.deliveries.DeliveryRequest;
import com.erp.maisPraTi.dto.deliveries.DeliveryResponse;
import com.erp.maisPraTi.dto.sales.SaleDto;
import com.erp.maisPraTi.dto.sales.SaleInsertDto;
import com.erp.maisPraTi.service.DeliveryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@Tag(name = "Entregas", description = "Operações relacionadas as entregas de vendas.")
@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping(value = "api/entregas")
public class DeliveryController {

    @Autowired
    private DeliveryService deliveryService;

    @Operation(summary = "Cria uma nova entrega")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Entrega criada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    @PostMapping
    public ResponseEntity<DeliveryResponse> insert (@Valid @RequestBody DeliveryRequest request){
        DeliveryResponse deliveryResponse = deliveryService.insert(request);
        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(deliveryResponse.getId())
                .toUri();
        return ResponseEntity.created(uri).body(deliveryResponse);
    }
}
