package com.erp.maisPraTi.controller;

import com.erp.maisPraTi.dto.users.CardDto;
import com.erp.maisPraTi.dto.users.UserDto;
import com.erp.maisPraTi.dto.users.UserInsertDto;
import com.erp.maisPraTi.dto.users.UserUpdateDto;
import com.erp.maisPraTi.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Optional;

@Tag(name = "Usuários", description = "Operações relacionadas aos Usuários do sistema.")
@RestController
@CrossOrigin(origins = "http://44.209.71.20:3000")
@RequestMapping("/api/usuarios")
public class UserController {

    @Autowired
    private UserService userService;

    @Operation(summary = "Cria um novo usuário")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Usuário criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    @PostMapping
    public ResponseEntity<UserDto> insert(@RequestBody UserInsertDto userInsertDto){
        UserDto newUserDto = userService.insert(userInsertDto);
        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(newUserDto.getId())
                .toUri();
        return ResponseEntity.created(uri).body(newUserDto);
    }

    @Operation(summary = "Obtém um usuário por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuário encontrado"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Optional<UserDto>> findById(@PathVariable Long id){
        return ResponseEntity.ok().body(userService.findById(id));
    }

    @Operation(summary = "Obtém uma lista páginada de usuários")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuário encontrado"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    })
    @GetMapping
    public ResponseEntity<Page<UserDto>> findAll(Pageable pageable){
        Page<UserDto> users = userService.findAll(pageable);
        return ResponseEntity.ok().body(users);
    }

    @Operation(summary = "Atualiza um usuário informando o ID e os dados por parâmetro")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuário encontrado"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    })
    @PutMapping("/{id}")
    public ResponseEntity<UserDto> update(@PathVariable Long id, @RequestBody UserUpdateDto userUpdateDto){
        UserDto userUpdatedDto = userService.update(id, userUpdateDto);
        return ResponseEntity.ok().body(userUpdatedDto);
    }

    @Operation(summary = "Deleta um usuário informando o ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Usuário deletado"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado"),
            @ApiResponse(responseCode = "409", description = "Para manter integridade do BD não permite a exclusão")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        userService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Insere as informações de cards para cada usuário.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Informações inseridas."),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado.")
    })
    @PostMapping("/{id}/cards")
    public ResponseEntity<Void> insert(@PathVariable Long id, @RequestBody CardDto cardDto){
        userService.insertCard(id, cardDto);
        return ResponseEntity.noContent().build();
    }

}
