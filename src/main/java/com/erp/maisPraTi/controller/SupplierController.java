package com.erp.maisPraTi.controller;

import com.erp.maisPraTi.dto.partyDto.suppliers.SupplierDto;
import com.erp.maisPraTi.dto.partyDto.suppliers.SupplierUpdateDto;
import com.erp.maisPraTi.service.SupplierService;
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

@Tag(name = "Fornecedores", description = "Operações relacionadas aos Fornecedores.")
@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping(value = "/api/fornecedores")
public class SupplierController {

    @Autowired
    private SupplierService supplierService;

    @Operation(summary = "Cria um novo fornecedor")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Fornecedor criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    @PostMapping
    public ResponseEntity<SupplierDto> insert (@Valid @RequestBody SupplierDto supplierDto) {
        SupplierDto newSupplierDto = supplierService.insert(supplierDto);
        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(newSupplierDto.getId())
                .toUri();
        return ResponseEntity.created(uri).body(newSupplierDto);
    }

    @Operation(summary = "Obtém um fornecedor por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Fornecedor encontrado"),
            @ApiResponse(responseCode = "404", description = "Fornecedor não encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Optional<SupplierDto>> findById(@PathVariable Long id) {
        Optional<SupplierDto> supplierDto = supplierService.findById(id);
        return ResponseEntity.ok().body(supplierDto);
    }

    @Operation(summary = "Obtém uma lista páginada de fornecedors")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Fornecedor encontrado"),
            @ApiResponse(responseCode = "404", description = "Fornecedor não encontrado")
    })
    @GetMapping
    public ResponseEntity<Page<SupplierDto>> findAll(Pageable pageable) {
        Page<SupplierDto> suppliers = supplierService.findAll(pageable);
        return ResponseEntity.ok().body(suppliers);
    }

    @Operation(summary = "Atualiza um fornecedor informando o ID e os dados por parâmetro")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Fornecedor encontrado"),
            @ApiResponse(responseCode = "404", description = "Fornecedor não encontrado")
    })
    @PutMapping("/{id}")
    public ResponseEntity<SupplierDto> update(@PathVariable Long id, @Valid @RequestBody SupplierUpdateDto supplierUpdateDto) {
        SupplierDto supplierUpdatedDto = supplierService.update(id, supplierUpdateDto);
        return ResponseEntity.ok().body(supplierUpdatedDto);
    }

    @Operation(summary = "Deleta um fornecedor informando o ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Fornecedor deletado"),
            @ApiResponse(responseCode = "404", description = "Fornecedor não encontrado"),
            @ApiResponse(responseCode = "409", description = "Para manter integridade do BD não permite a exclusão")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        supplierService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
