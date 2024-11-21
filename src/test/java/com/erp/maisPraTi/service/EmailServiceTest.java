package com.erp.maisPraTi.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.ArgumentCaptor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class EmailServiceTest {

    @Mock
    private JavaMailSender mailSender; // Simula o JavaMailSender que será usado para enviar o e-mail

    @InjectMocks
    private EmailService emailService; // O serviço que estamos testando

    // Teste para enviar o e-mail de recuperação de senha com sucesso
    @Test
    void deveEnviarEmailDeRecuperacaoDeSenhaComSucesso() {
        String to = "teste@dominio.com";
        String token = "abc123";

        // Simula o envio do e-mail
        doNothing().when(mailSender).send(any(SimpleMailMessage.class));

        // Verifica se o método sendPasswordResetEmail não lança exceções
        assertDoesNotThrow(() -> emailService.sendPasswordResetEmail(to, token));

        // Verifica se o método send foi chamado com o SimpleMailMessage correto
        verify(mailSender, times(1)).send(any(SimpleMailMessage.class));
    }

    // Teste para falha ao enviar o e-mail de recuperação de senha
    @Test
    void deveLancarExcecaoQuandoErroAoEnviarEmail() {
        String to = "teste@dominio.com";
        String token = "abc123";

        // Simula uma falha no envio do e-mail
        doThrow(new RuntimeException("Erro ao enviar e-mail")).when(mailSender).send(any(SimpleMailMessage.class));

        // Verifica se o erro é tratado corretamente
        RuntimeException exception = assertThrows(RuntimeException.class, () -> emailService.sendPasswordResetEmail(to, token));
        assertEquals("Erro ao enviar e-mail", exception.getMessage());
    }

    // Teste para verificar se o e-mail é enviado com o conteúdo correto
    @Test
    void deveCriarEmailDeRecuperacaoComParametrosCorretos() {
        String to = "teste@dominio.com";
        String token = "abc123";
        String link = "http://localhost:5173/resetpassword?token=" + token;
        String subject = "Recuperação de Senha";
        String body = "Clique no link abaixo para redefinir a sua senha:\n" + link;

        // ArgumentCaptor para capturar o SimpleMailMessage enviado
        ArgumentCaptor<SimpleMailMessage> captor = ArgumentCaptor.forClass(SimpleMailMessage.class);

        // Chama o método para enviar o e-mail
        emailService.sendPasswordResetEmail(to, token);

        // Verifica se o método send foi chamado com o SimpleMailMessage
        verify(mailSender, times(1)).send(captor.capture());

        // Recupera o SimpleMailMessage capturado
        SimpleMailMessage capturedMessage = captor.getValue();

        // Verifica se o destinatário, assunto e corpo do e-mail estão corretos
        assertEquals(to, capturedMessage.getTo()[0]);
        assertEquals(subject, capturedMessage.getSubject());
        assertEquals(body, capturedMessage.getText());
    }
}

