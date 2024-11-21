package com.erp.maisPraTi.service;

import com.erp.maisPraTi.dto.auth.LoginRequest;
import com.erp.maisPraTi.dto.auth.LoginResponse;
import com.erp.maisPraTi.dto.auth.ResetPasswordRequest;
import com.erp.maisPraTi.dto.auth.ValidationUserRequest;
import com.erp.maisPraTi.enums.PartyStatus;
import com.erp.maisPraTi.model.User;
import com.erp.maisPraTi.repository.UserRepository;
import com.erp.maisPraTi.security.JwtTokenProvider;
import com.erp.maisPraTi.security.service.UserDetailsServiceImpl;
import com.erp.maisPraTi.service.exceptions.AuthenticationUserException;
import com.erp.maisPraTi.service.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    @Autowired
    private UserDetailsServiceImpl userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private EmailService emailService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public LoginResponse login(LoginRequest request){
        verifyUserActive(request.getEmail());
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );
            LoginResponse response = new LoginResponse();
            String token = jwtTokenProvider.generateToken(authentication);
            response.setToken(token);
            return response;
        } catch (AuthenticationUserException error) {
            throw new AuthenticationUserException("Credenciais inválidas.");
        }
    }

    public void requestPasswordReset(String email){
        verifyUserActive(email);
        String token = jwtTokenProvider.generateTokenWithUserEmail(email);
        emailService.sendPasswordResetEmail(email, token);
    }

    public void resetPassword(String token, ResetPasswordRequest request){
        Optional<User> user = userRepository.findByEmail(jwtTokenProvider.getUserEmailFromToken(token));
        if(user.isPresent() && !request.getNewPassword().isEmpty()){
            user.get().setPassword(passwordEncoder.encode(request.getNewPassword()));
            userRepository.save(user.get());
        } else{
            throw new ResourceNotFoundException("Usuário não encontrado.");
        }
    }

    public boolean validateUser(String token, ValidationUserRequest request) {
        Optional<User> user = userRepository.findByEmail(jwtTokenProvider.getUserEmailFromToken(token));
        if(user.isPresent() && user.get().getCpf().equals(request.getCpf()))
            return true;
        else if(user.isEmpty())
            throw new ResourceNotFoundException("Usuário não encontrado.");
        else
            throw new ResourceNotFoundException("CPF inválido.");
    }

    public void verifyUserActive(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        if(user.isPresent() && user.get().getStatus() != PartyStatus.ACTIVE)
            throw new AuthenticationUserException("Usuário inativo ou suspenso, entre em contato com o administrador do sistema.");
        if(user.isEmpty())
            throw new ResourceNotFoundException("Usuário não encontrado.");
    }

}
