package com.erp.maisPraTi.service;

import com.erp.maisPraTi.dto.partyDto.clients.ClientDto;
import com.erp.maisPraTi.dto.partyDto.clients.ClientUpdateDto;
import com.erp.maisPraTi.enums.TypePfOrPj;
import com.erp.maisPraTi.model.Client;
import com.erp.maisPraTi.repository.ClientRepository;
import com.erp.maisPraTi.service.exceptions.DatabaseException;
import com.erp.maisPraTi.service.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ClientServiceTest {

    @Mock
    private ClientRepository clientRepository;

    @InjectMocks
    private ClientService clientService;

    @Test
    void deveVerificarSeOsDocumentosExistemParaAlgumClienteQuandoInformadoCpfETipoPF() {
        String cpf = "000.111.222.333-44";
        String stateRegistration = "isento";
        TypePfOrPj typePfOrPj = TypePfOrPj.PF;

        when(clientRepository.existsByCpfCnpj(cpf)).thenReturn(true);

        DatabaseException exception = assertThrows(DatabaseException.class, () -> {
            clientService.verifyExistsDocuments(cpf, stateRegistration, typePfOrPj);
        });

        assertEquals("CPF já cadastrado no sistema.", exception.getMessage());
    }

    @Test
    void deveVerificarSeOsDocumentosExistemParaAlgumClienteQuandoInformadoCnpjExistenteETipoPJ() {
        String cnpj = "12555888/0001-99";
        String stateRegistration = "909/8328356";
        TypePfOrPj typePfOrPj = TypePfOrPj.PJ;

        when(clientRepository.existsByCpfCnpj(cnpj)).thenReturn(true);

        DatabaseException exception = assertThrows(DatabaseException.class, () -> {
            clientService.verifyExistsDocuments(cnpj, stateRegistration, typePfOrPj);
        });

        assertEquals("CNPJ já cadastrado no sistema.", exception.getMessage());
    }

    @Test
    void deveVerificarSeOsDocumentosExistemParaAlgumClienteQuandoInformadoInscricaoEstadualExistenteETipoPJ() {
        String cnpj = "55.222.555/0001-55";
        String stateRegistration = "909/8328356";
        TypePfOrPj typePfOrPj = TypePfOrPj.PJ;

        when(clientRepository.existsByCpfCnpj(cnpj)).thenReturn(false);
        when(clientRepository.existsByStateRegistration(stateRegistration)).thenReturn(true);

        DatabaseException exception = assertThrows(DatabaseException.class, () -> {
            clientService.verifyExistsDocuments(cnpj, stateRegistration, typePfOrPj);
        });

        assertEquals("Inscrição estadual já cadastrada no sistema.", exception.getMessage());
    }

    @Test
    void deveInserirUmNovoCliente() {
        ClientDto clientDto = new ClientDto();
        clientDto.setCpfCnpj("12345678909");
        clientDto.setStateRegistration("isento");
        clientDto.setTypePfOrPj(TypePfOrPj.PF);  // Define o tipo como PF

        Client client = new Client();
        client.setCpfCnpj(clientDto.getCpfCnpj());
        client.setStateRegistration(clientDto.getStateRegistration());
        client.setTypePfOrPj(clientDto.getTypePfOrPj());  // Define o tipo no cliente

        when(clientRepository.save(any(Client.class))).thenReturn(client);

        ClientDto result = clientService.insert(clientDto);

        assertEquals(clientDto.getCpfCnpj(), result.getCpfCnpj());
    }

    @Test
    void deveEncontrarClientePorId() {
        Long id = 1L;
        Client client = new Client();
        client.setId(id);
        client.setCpfCnpj("12345678909");

        when(clientRepository.findById(id)).thenReturn(Optional.of(client));

        Optional<ClientDto> result = clientService.findById(id);

        assertTrue(result.isPresent());
        assertEquals("12345678909", result.get().getCpfCnpj());
    }

    @Test
    void deveRetornarErroQuandoIdNaoExistir() {
        Long id = 1L;
        when(clientRepository.findById(id)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            clientService.findById(id);
        });

        assertEquals("Id não localizado: " + id, exception.getMessage());
    }

    @Test
    void deveRetornarPaginaDeClientes() {
        Client client = new Client();
        client.setCpfCnpj("12345678909");
        Page<Client> page = new PageImpl<>(List.of(client));

        when(clientRepository.findAll(any(Pageable.class))).thenReturn(page);

        Page<ClientDto> result = clientService.findAll(PageRequest.of(0, 10));

        assertFalse(result.isEmpty());
        assertEquals("12345678909", result.getContent().get(0).getCpfCnpj());
    }

    @Test
    void deveAtualizarCliente() {
        // Arrange
        Long id = 1L;
        ClientUpdateDto clientUpdateDto = new ClientUpdateDto();
        clientUpdateDto.setCpfCnpj("98765432100");
        clientUpdateDto.setTypePfOrPj(TypePfOrPj.PJ);
        clientUpdateDto.setStateRegistration("909/8328356");

        Client client = new Client();
        client.setId(id);
        client.setCpfCnpj("12345678909");
        client.setTypePfOrPj(TypePfOrPj.PF);

        // Mock para o método verifyExistsId (verificando que o ID existe)
        when(clientRepository.existsById(id)).thenReturn(true); // Mock para a verificação do ID

        // Mock para `getReferenceById`
        when(clientRepository.getReferenceById(id)).thenReturn(client);

        // Mock para salvar o cliente
        when(clientRepository.save(any(Client.class))).thenReturn(client);

        // Action
        ClientDto result = clientService.update(id, clientUpdateDto);

        // Assert
        assertEquals("98765432100", result.getCpfCnpj());
        assertEquals(TypePfOrPj.PJ, result.getTypePfOrPj());

        // Verifique se `getReferenceById` foi chamado com o ID correto
        verify(clientRepository).getReferenceById(id);
    }

    @Test
    void deveLancarErroAoAtualizarClienteComViolacaoDeIntegridade() {
        // Arrange
        Long id = 1L;
        ClientUpdateDto clientUpdateDto = new ClientUpdateDto();
        clientUpdateDto.setCpfCnpj("98765432100");
        clientUpdateDto.setTypePfOrPj(TypePfOrPj.PJ);
        clientUpdateDto.setStateRegistration("909/8328356");

        Client client = new Client();
        client.setId(id);
        client.setCpfCnpj("12345678909");

        when(clientRepository.existsById(id)).thenReturn(true);
        when(clientRepository.getReferenceById(id)).thenReturn(client);

        // Simula a violação de integridade ao salvar o cliente
        when(clientRepository.save(any(Client.class))).thenThrow(new DataIntegrityViolationException("Erro"));

        // Action & Assert
        DatabaseException exception = assertThrows(DatabaseException.class, () -> {
            clientService.update(id, clientUpdateDto);
        });

        assertEquals("Não foi possível fazer a alteração neste cliente.", exception.getMessage());

        // Verifique se `save` foi chamado durante o teste
        verify(clientRepository, times(1)).save(client);
    }

    @Test
    void deveExcluirCliente() {
        Long id = 1L;
        when(clientRepository.existsById(id)).thenReturn(true);
        doNothing().when(clientRepository).deleteById(id);

        assertDoesNotThrow(() -> clientService.delete(id));
        verify(clientRepository, times(1)).deleteById(id);
    }

    @Test
    void deveLancarErroGenericoAoExcluirCliente() {
        Long id = 1L;
        when(clientRepository.existsById(id)).thenReturn(true);

        // Simulando uma exceção genérica ao tentar deletar o cliente
        doThrow(new RuntimeException("Erro inesperado")).when(clientRepository).deleteById(id);

        DatabaseException exception = assertThrows(DatabaseException.class, () -> {
            clientService.delete(id);
        });

        assertEquals("Erro inesperado ao tentar excluir o cliente.", exception.getMessage());
    }

    @Test
    void deveLancarResourceNotFoundExceptionQuandoIdNaoExistirNoVerifyExistsId() {
        Long id = 1L;

        // Configura o mock para retornar `false`, simulando que o ID não existe
        when(clientRepository.existsById(id)).thenReturn(false);

        // Verifica se o método lança a exceção `ResourceNotFoundException` com a mensagem esperada
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            clientService.verifyExistsId(id);
        });

        assertEquals("Id não localizado: " + id, exception.getMessage());
    }


    @Test
    void deveLancarErroAoExcluirClienteVinculado() {
        Long id = 1L;
        when(clientRepository.existsById(id)).thenReturn(true);
        doThrow(new DataIntegrityViolationException("Erro")).when(clientRepository).deleteById(id);

        DatabaseException exception = assertThrows(DatabaseException.class, () -> {
            clientService.delete(id);
        });

        assertEquals("Não foi possível excluir este cliente. Ele pode estar vinculado a outros registros.", exception.getMessage());
    }

    @Test
    void deveVerificarExistenciaDeId() {
        Long id = 1L;
        when(clientRepository.existsById(id)).thenReturn(true);

        assertDoesNotThrow(() -> clientService.verifyExistsId(id));
    }

    @Test
    void deveNormalizarInscricaoEstadual() {
        String stateRegistration = "EXEMPLO";
        String normalized = clientService.stateRegistrationNormalize(stateRegistration);

        assertEquals("exemplo", normalized);
    }
}





