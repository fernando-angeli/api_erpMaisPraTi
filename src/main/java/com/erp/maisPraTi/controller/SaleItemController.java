package com.erp.maisPraTi.controller;

import com.erp.maisPraTi.dto.saleItems.SaleInsertItemDto;
import com.erp.maisPraTi.dto.saleItems.SaleItemResponseDto;
import com.erp.maisPraTi.dto.saleItems.SaleItemUpdateDto;
import com.erp.maisPraTi.service.SaleItemService;
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

@Tag(name = "Itens de vendas", description = "Operações relacionadas aos Itens de venda.")
@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping(value = "api/vendas")
public class SaleItemController {

    @Autowired
    private SaleItemService saleItemService;

    @Operation(summary = "Inserir item em uma venda.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Item inserido com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    @PostMapping("/{saleId}/itens")
    public ResponseEntity<SaleItemResponseDto> insert (@PathVariable Long saleId, @Valid @RequestBody SaleInsertItemDto saleInsertItemDto){
        SaleItemResponseDto newSaleItemDto = saleItemService.insert(saleId, saleInsertItemDto);
        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{saleId}")
                .buildAndExpand(newSaleItemDto.getId())
                .toUri();
        return ResponseEntity.created(uri).body(newSaleItemDto);
    }

    @Operation(summary = "Obtém um item especifico da venda por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Item encontrado"),
            @ApiResponse(responseCode = "404", description = "Item não encontrado")
    })
    @GetMapping("/{saleId}/itens/{itemId}")
    public ResponseEntity<Optional<SaleItemResponseDto>> findBySaleIdAndSaleItemId(@PathVariable Long saleId, @PathVariable Long itemId){
        Optional<SaleItemResponseDto> saleItemDto = saleItemService.findBySaleIdAndSaleItemId(saleId, itemId);
        return ResponseEntity.ok().body(saleItemDto);
    }

    @Operation(summary = "Obtém uma venda e sua lista de itens e entregas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Itens encontrados"),
            @ApiResponse(responseCode = "404", description = "Itens não encontrados")
    })
    @GetMapping("/{saleId}/itens")
    public ResponseEntity<Page<SaleItemResponseDto>> findAllBySaleId(@PathVariable Long saleId, Pageable pageable){
        Page<SaleItemResponseDto> sales = saleItemService.findAllBySaleId(saleId, pageable);
        return ResponseEntity.ok().body(sales);
    }

    @Operation(summary = "Obtém uma lista páginada dos itens da venda filtrados pelo ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Itens encontrados"),
            @ApiResponse(responseCode = "404", description = "Itens não encontrados")
    })
    @GetMapping("/{saleId}/itens/produtos/{productId}")
    public ResponseEntity<Page<SaleItemResponseDto>> findAllBySaleIdAndProductId(@PathVariable Long saleId, @PathVariable Long productId, Pageable pageable){
        Page<SaleItemResponseDto> sales = saleItemService.findAllByProductId(saleId, productId, pageable);
        return ResponseEntity.ok().body(sales);
    }

    @Operation(summary = "Obtém uma lista páginada de todos os itens vendidos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Itens encontrados"),
            @ApiResponse(responseCode = "404", description = "Itens não encontrados")
    })
    @GetMapping("/itens")
    public ResponseEntity<Page<SaleItemResponseDto>> findAll(Pageable pageable){
        Page<SaleItemResponseDto> sales = saleItemService.findAll(pageable);
        return ResponseEntity.ok().body(sales);
    }

    @Operation(summary = "Atualiza um item de venda informando o ID e os dados por parâmetro")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Item e venda encontrados"),
            @ApiResponse(responseCode = "404", description = "Item ou venda não encontrados")
    })
    @PutMapping("/itens/{itemId}")
    public ResponseEntity<SaleItemResponseDto> update(@PathVariable Long itemId, @Valid @RequestBody SaleItemUpdateDto saleItemUpdateDto){
        SaleItemResponseDto saleUpdatedDto = saleItemService.update(itemId, saleItemUpdateDto);
        return ResponseEntity.ok().body(saleUpdatedDto);
    }

    @Operation(summary = "Deleta uma venda informando o ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Item deletado."),
            @ApiResponse(responseCode = "404", description = "Item não encontrado"),
            @ApiResponse(responseCode = "409", description = "Para manter integridade do BD não permite a exclusão")
    })
    @DeleteMapping("/itens/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        saleItemService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
