package com.erp.maisPraTi.controller;

import com.erp.maisPraTi.dto.auth.*;
import com.erp.maisPraTi.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Autenticação de usuários", description = "Operações relacionadas as autenticações de usuários.")
@CrossOrigin(origins = "http://44.209.71.20:3000")
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService service;

    @Operation(summary = "Realiza o login do usuário.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Usuário logado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Credenciais inválidas")
    })
    @PostMapping("/login")
    public ResponseEntity<String> login (@RequestBody LoginRequest request){
        LoginResponse response = service.login(request);
        return ResponseEntity.ok().body(response.getToken());
    }

    @Operation(summary = "Envio de e-mail de recuperação de senha.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "E-mail enviado com sucesso."),
            @ApiResponse(responseCode = "400", description = "Usuário inválido")
    })
    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestBody ForgotPasswordRequest request){
        service.requestPasswordReset(request.getEmail());
        return ResponseEntity.ok("Link de recuperação enviado para o e-mail.");
    }

    @Operation(summary = "Faz a validação do usuário que está solicitando recuperação de senha.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "True para dados válidos."),
            @ApiResponse(responseCode = "400", description = "False para dados inválidos.")
    })
    @PostMapping("/validation-user")
    public ResponseEntity<Boolean> validationUser(@RequestParam String token, @RequestBody ValidationUserRequest request){
        boolean response = service.validateUser(token, request);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Reset de senha do usuário.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Senha gerada com sucesso."),
            @ApiResponse(responseCode = "400", description = "Dados inválidos.")
    })
    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestParam String token, @RequestBody ResetPasswordRequest request){
        service.resetPassword(token, request);
        return ResponseEntity.ok("Senha redefinida com sucesso.");
    }

}
