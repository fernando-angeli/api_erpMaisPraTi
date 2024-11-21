package com.erp.maisPraTi.service;

import com.erp.maisPraTi.dto.auth.LoginRequest;
import com.erp.maisPraTi.dto.auth.LoginResponse;
import com.erp.maisPraTi.dto.auth.ResetPasswordRequest;
import com.erp.maisPraTi.dto.auth.ValidationUserRequest;
import com.erp.maisPraTi.enums.PartyStatus;
import com.erp.maisPraTi.fixture.LoginFixture;
import com.erp.maisPraTi.fixture.UserFixture;
import com.erp.maisPraTi.model.User;
import com.erp.maisPraTi.repository.UserRepository;
import com.erp.maisPraTi.security.JwtTokenProvider;
import com.erp.maisPraTi.service.exceptions.AuthenticationUserException;
import com.erp.maisPraTi.service.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private UserRepository userRepository;

    @Mock
    private EmailService emailService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthService authService;

    @Test
    void deveFazerLoginERetornarUmTokenQuandoAsCredenciaisDoUsuarioForemValidasEEstiverAtivo(){
        //Arrange
        LoginRequest request = LoginFixture.loginRequest();
        Authentication authentication = mock(Authentication.class);
        String expectedToken = "fake_jwt_token";
        User userAtivo = UserFixture.userAdmin();
        // Definir o comportamento do mock
        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(userAtivo));
        when(authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())))
                .thenReturn(authentication);
        when(jwtTokenProvider.generateToken(authentication)).thenReturn(expectedToken);
        //Action
        LoginResponse response = authService.login(request);
        //Assert
        assertNotNull(response);
        assertEquals(expectedToken, response.getToken());
    }

    @Test
    void deveGerarUmaExceptionQuandoTentarFazerLoginENaoLocalizarOUsuario(){
        //Arrange
        LoginRequest request = LoginFixture.loginRequest();
        // Definir o comportamento do mock
        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.empty());
        // Action
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            authService.login(request);
        });

        // Assert
        assertEquals("Usuário não encontrado.", exception.getMessage());
    }

    @Test
    void deveGerarUmaExceptionQuandoTentarFazerLoginOuRecuperacaoDeSenhaDeUsuarioInativo(){
        // Arrange
        LoginRequest request = LoginFixture.loginRequest();
        User inactiveUser = mock(User.class);
        // Definir o comportamento do mock
        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(inactiveUser));
        when(inactiveUser.getStatus()).thenReturn(PartyStatus.INACTIVE);  // Simula um usuário inativo
        // Action & Assert
        AuthenticationUserException exception = assertThrows(AuthenticationUserException.class, () -> {
            authService.verifyUserActive(request.getEmail());  // Chama o método que lança a exceção
        });
        assertEquals("Usuário inativo ou suspenso, entre em contato com o administrador do sistema.", exception.getMessage());
    }

    @Test
    void deveGerarUmaExceptionQuandoTentarFazerLoginOuRecuperacaoDeSenhaDeUsuarioSuspenso(){
        // Arrange
        LoginRequest request = LoginFixture.loginRequest();
        User suspendedUser = mock(User.class);
        // Definir o comportamento do mock
        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(suspendedUser));
        when(suspendedUser.getStatus()).thenReturn(PartyStatus.SUSPENDED);  // Simula um usuário suspenso
        // Action & Assert
        AuthenticationUserException exception = assertThrows(AuthenticationUserException.class, () -> {
            authService.verifyUserActive(request.getEmail());  // Chama o método que lança a exceção
        });
        assertEquals("Usuário inativo ou suspenso, entre em contato com o administrador do sistema.", exception.getMessage());
    }

    @Test
    void deveGerarUmaExceptionQuandoAsCredenciaisNaoForemValidas(){
        //Arrange
        LoginRequest request = LoginFixture.loginRequest();
        User userAtivo = UserFixture.userAdmin();
        // Definir o comportamento do mock
        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(userAtivo));
        when(authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())))
                .thenThrow(new AuthenticationUserException("Credenciais inválidas."));
        // Action
        AuthenticationUserException exception = assertThrows(AuthenticationUserException.class, () -> {
            authService.login(request);
        });
        // Assert
        assertEquals("Credenciais inválidas.", exception.getMessage());
    }

    @Test
    void deveEnviarUmEmailDeRecuperacaoQuandoOUsuarioExistir(){
        // Arrange
        User user = UserFixture.userAdmin();
        String token = "fake_token";
        // Definir o comportamento do mock
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(jwtTokenProvider.generateTokenWithUserEmail(user.getEmail())).thenReturn(token);
        // Action
        authService.requestPasswordReset(user.getEmail());
        // Assert
        verify(emailService).sendPasswordResetEmail(user.getEmail(), token);
    }

    @Test
    void deveRetornarTrueSeOCpfInformadoForOMesmoDoUsuarioLocalizadoPeloEmail(){
        // Arrange
        User user = UserFixture.userAdmin();
        String email = user.getEmail();
        String cpf = user.getCpf();
        ValidationUserRequest request = new ValidationUserRequest(email, cpf);
        String token = "fake_token";

        // Comportamento do Mock
        when(jwtTokenProvider.getUserEmailFromToken(token)).thenReturn(email);
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        // Action
        authService.validateUser(token, request);

        // Assert
        assertTrue(authService.validateUser(token, request));
    }

    @Test
    void deveGerarUmaExceptionQuandoOUsuarioNaoForEncontrado(){
        // Arrange
        User user = UserFixture.userAdmin();
        String email = "invalid-email@email.com";
        String cpf = user.getCpf();
        ValidationUserRequest request = new ValidationUserRequest(email, cpf);
        String token = "fake_token";

        // Comportamento do Mock
        when(jwtTokenProvider.getUserEmailFromToken(token)).thenReturn(user.getEmail());
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.empty());

        // Action
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            authService.validateUser(token, request);
        });

        // Assert
        assertEquals("Usuário não encontrado.", exception.getMessage());
    }

    @Test
    void deveGerarUmaExceptionQuandoOUsuarioForValidoMasOCpfNaoEstiverCorreto(){
        // Arrange
        User user = UserFixture.userAdmin();
        String email = user.getEmail();
        String cpf = "invalid-cpf";
        ValidationUserRequest request = new ValidationUserRequest(email, cpf);
        String token = "fake_token";

        // Comportamento do Mock
        when(jwtTokenProvider.getUserEmailFromToken(token)).thenReturn(user.getEmail());
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        // Action
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            authService.validateUser(token, request);
        });

        // Assert
        assertEquals("CPF inválido.", exception.getMessage());
    }

    @Test
    void deveFazerATrocaDeSenhaQuandoOUsuarioForValidoEOCpfForCorreto(){
        // Arrange
        User user = UserFixture.userAdmin();
        String email = user.getEmail();
        String newPassword = "novasenha";
        ResetPasswordRequest request = new ResetPasswordRequest();
        request.setNewPassword(newPassword);
        String token = "fake_token";

        // Comportamento do Mock
        when(jwtTokenProvider.getUserEmailFromToken(token)).thenReturn(user.getEmail());
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        // Action
        authService.resetPassword(token, request);

        // Assert
        verify(passwordEncoder).encode(newPassword);
        verify(userRepository).save(user);
    }

    // refatorar
    @Test
    void deveGerarUmaExceptionQuandoOUsuarioNaoExistirNoResetDeSenha(){
        // Arrange
        User user = UserFixture.userAdmin();
        String email = "invalid-email@email.com";
        String newPassword = "novasenha";
        ResetPasswordRequest request = new ResetPasswordRequest();
        request.setNewPassword(newPassword);
        String token = "fake_token";

        // Comportamento do Mock
        when(jwtTokenProvider.getUserEmailFromToken(token)).thenReturn(email);
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        // Action
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            authService.resetPassword(token, request);
        });

        // Assert
        assertEquals("Usuário não encontrado.", exception.getMessage());
    }

}
