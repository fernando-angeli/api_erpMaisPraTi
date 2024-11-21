package com.erp.maisPraTi.config;

import com.erp.maisPraTi.controller.exceptions.StandardError;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.io.IOException;

@Configuration
public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.setContentType("application/json");
        StandardError error = new StandardError();
        error.setStatus(HttpStatus.FORBIDDEN.value());
        error.setError("Access denied");
        error.setMessage("Acesso negado, usuário sem autorização para o recurso solicitado.");
        error.setPath(request.getRequestURI());
        String jsonResponse = new ObjectMapper().writeValueAsString(error);
        response.getWriter().write(jsonResponse);
    }
}
