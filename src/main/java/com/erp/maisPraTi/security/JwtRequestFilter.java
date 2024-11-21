package com.erp.maisPraTi.security;

import com.erp.maisPraTi.security.model.CustomUserDetails;
import com.erp.maisPraTi.security.exceptions.StandardErrorAuth;
import com.erp.maisPraTi.security.service.UserDetailsServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private UserDetailsServiceImpl userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain)
            throws ServletException, IOException {

        final String authorizationHeader = request.getHeader("Authorization");
        String jwtToken = null;
        String email = null;

        // Verifica se o token JWT está presente no cabeçalho
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwtToken = authorizationHeader.substring(7);
            try {
                email = jwtTokenProvider.extractUsername(jwtToken);
            } catch (ExpiredJwtException e) {
                handleException(response, "Expired token", "Token expirado", HttpServletResponse.SC_UNAUTHORIZED);
            }
            catch (IllegalArgumentException e) {
                handleException(response, "Token error", "Token inválido ou ausente", HttpServletResponse.SC_UNAUTHORIZED);
            }
        }

        // Valida o token e carrega o usuário
        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            CustomUserDetails userDetails = (CustomUserDetails) userService.loadUserByUsername(email);
            if (jwtTokenProvider.validateToken(jwtToken, userDetails.getUsername())) {
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }
        chain.doFilter(request, response);
    }

    private void handleException(HttpServletResponse response, String errorDescription, String message, Integer status) throws IOException {
        StandardErrorAuth error = new StandardErrorAuth();
        error.setStatus(status);
        error.setError(HttpStatus.valueOf(status).getReasonPhrase());
        error.setMessage(message);

        response.setStatus(status);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        new ObjectMapper().writeValue(response.getWriter(), error);
    }
}
