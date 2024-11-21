package com.erp.maisPraTi.controller;

import com.erp.maisPraTi.dto.sales.SaleDto;
import com.erp.maisPraTi.dto.sales.SaleInsertDto;
import com.erp.maisPraTi.dto.sales.SaleUpdateDto;
import com.erp.maisPraTi.service.SaleService;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.Optional;

@Tag(name = "Vendas", description = "Operações relacionadas as vendas.")
@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping(value = "api/vendas")
class SaleController {

    @Autowired
    private SaleService saleService;

    @Operation(summary = "Cria uma nova venda")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Venda criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    @PostMapping
    public ResponseEntity<SaleDto> insert (@Valid @RequestBody SaleInsertDto saleDto){
        SaleDto newSaleDto = saleService.insert(saleDto);
        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(newSaleDto.getId())
                .toUri();
        return ResponseEntity.created(uri).body(newSaleDto);
    }

    @Operation(summary = "Obtém uma venda por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Venda encontrada"),
            @ApiResponse(responseCode = "404", description = "Venda não encontrada")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Optional<SaleDto>> findById(@PathVariable Long id){
        Optional<SaleDto> saleDto = saleService.findById(id);
        return ResponseEntity.ok().body(saleDto);
    }

    @Operation(summary = "Obtém uma lista páginada de vendas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Venda encontrada"),
            @ApiResponse(responseCode = "404", description = "Venda não encontrada")
    })
    @GetMapping
    public ResponseEntity<Page<SaleDto>> findAll(Pageable pageable){
        Page<SaleDto> sales = saleService.findAll(pageable);
        return ResponseEntity.ok().body(sales);
    }

    @Operation(summary = "Atualiza uma venda informando o ID e os dados por parâmetro")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Venda encontrada"),
            @ApiResponse(responseCode = "404", description = "Venda não encontrada")
    })
    @PutMapping("/{id}")
    public ResponseEntity<SaleDto> update(@PathVariable Long id, @Valid @RequestBody SaleUpdateDto saleUpdateDto){
        SaleDto saleUpdatedDto = saleService.update(id, saleUpdateDto);
        return ResponseEntity.ok().body(saleUpdatedDto);
    }

    @Operation(summary = "Deleta uma venda informando o ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Venda deletada"),
            @ApiResponse(responseCode = "404", description = "Venda não encontrada"),
            @ApiResponse(responseCode = "409", description = "Para manter integridade do BD não permite a exclusão")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        saleService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
