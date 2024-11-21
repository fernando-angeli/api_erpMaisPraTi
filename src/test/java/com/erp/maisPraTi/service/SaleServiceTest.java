package com.erp.maisPraTi.service;

import com.erp.maisPraTi.dto.partyDto.clients.ClientDto;
import com.erp.maisPraTi.dto.sales.SaleDto;
import com.erp.maisPraTi.dto.sales.SaleInsertDto;
import com.erp.maisPraTi.dto.sales.SaleUpdateDto;
import com.erp.maisPraTi.enums.PartyStatus;
import com.erp.maisPraTi.enums.SaleStatus;
import com.erp.maisPraTi.model.Client;
import com.erp.maisPraTi.model.Sale;
import com.erp.maisPraTi.repository.SaleRepository;
import com.erp.maisPraTi.service.exceptions.DatabaseException;
import com.erp.maisPraTi.service.exceptions.NotActivateException;
import com.erp.maisPraTi.service.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.erp.maisPraTi.util.EntityMapper.convertToDto;
import static com.erp.maisPraTi.util.EntityMapper.convertToEntity;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SaleServiceTest {

    @InjectMocks
    private SaleService saleService;

    @Mock
    private SaleRepository saleRepository;

    @Mock
    private ClientService clientService;

    private Sale sale;
    private SaleDto saleDto;
    private SaleInsertDto saleInsertDto;
    private SaleUpdateDto saleUpdateDto;
    private ClientDto clientDto;
    private Client client;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);

        // Inicializar dados de teste
        sale = new Sale();
        sale.setId(1L);
        sale.setSaleNumber(1001L);
        sale.setSaleStatus(SaleStatus.PENDING);
        sale.setSaleDate(LocalDateTime.now());

        saleDto = new SaleDto();
        saleDto.setId(1L);
        saleDto.setSaleNumber(1001L);
        saleDto.setSaleStatus(SaleStatus.PENDING);

        saleInsertDto = new SaleInsertDto();
        saleInsertDto.setClientId(1L);

        saleUpdateDto = new SaleUpdateDto();

        clientDto = new ClientDto();
        clientDto.setId(1L);
        clientDto.setStatus(PartyStatus.ACTIVE);

        client = new Client();
        client.setId(1L);
    }

    @Test
    void inserirVenda_DeveInserirComSucesso() {
        when(clientService.findById(anyLong())).thenReturn(Optional.of(clientDto));
        when(saleRepository.save(any(Sale.class))).thenReturn(sale);

        SaleDto resultado = saleService.insert(saleInsertDto);

        assertNotNull(resultado);
        assertEquals(SaleStatus.PENDING, resultado.getSaleStatus());
        verify(saleRepository, times(1)).save(any(Sale.class));
    }

    @Test
    void inserirVenda_DeveLancarExcecaoSeClienteInativo() {
        clientDto.setStatus(PartyStatus.INACTIVE);
        when(clientService.findById(anyLong())).thenReturn(Optional.of(clientDto));

        assertThrows(NotActivateException.class, () -> saleService.insert(saleInsertDto));
        verify(saleRepository, never()).save(any(Sale.class));
    }

    @Test
    void buscarVendaPorId_DeveRetornarVendaSeEncontrada() {
        when(saleRepository.findById(anyLong())).thenReturn(Optional.of(sale));

        Optional<SaleDto> resultado = saleService.findById(1L);

        assertTrue(resultado.isPresent());
        assertEquals(sale.getSaleNumber(), resultado.get().getSaleNumber());
        verify(saleRepository, times(1)).findById(anyLong());
    }

    @Test
    void buscarVendaPorId_DeveLancarExcecaoSeNaoEncontrada() {
        when(saleRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> saleService.findById(1L));
        verify(saleRepository, times(1)).findById(anyLong());
    }

    @Test
    void buscarTodasVendas_DeveRetornarPaginaDeVendas() {
        PageRequest pageRequest = PageRequest.of(0, 10);
        Page<Sale> page = new PageImpl<>(List.of(sale));
        when(saleRepository.findAll(pageRequest)).thenReturn(page);

        Page<SaleDto> resultado = saleService.findAll(pageRequest);

        assertNotNull(resultado);
        assertEquals(1, resultado.getTotalElements());
        verify(saleRepository, times(1)).findAll(pageRequest);
    }

    @Test
    void atualizarVenda_DeveAtualizarComSucesso() {
        when(saleRepository.existsById(anyLong())).thenReturn(true);
        when(saleRepository.getReferenceById(anyLong())).thenReturn(sale);
        when(saleRepository.save(any(Sale.class))).thenReturn(sale);

        SaleDto resultado = saleService.update(1L, saleUpdateDto);

        assertNotNull(resultado);
        assertEquals(sale.getSaleNumber(), resultado.getSaleNumber());
        verify(saleRepository, times(1)).save(any(Sale.class));
    }

    @Test
    void atualizarVenda_DeveLancarExcecaoSeNaoEncontrada() {
        when(saleRepository.existsById(anyLong())).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> saleService.update(1L, saleUpdateDto));
        verify(saleRepository, never()).save(any(Sale.class));
    }

    @Test
    void atualizarVenda_DeveLancarExcecaoSeDataIntegrityViolada() {
        when(saleRepository.existsById(anyLong())).thenReturn(true);
        when(saleRepository.getReferenceById(anyLong())).thenReturn(sale);
        when(saleRepository.save(any(Sale.class))).thenThrow(DataIntegrityViolationException.class);

        assertThrows(DatabaseException.class, () -> saleService.update(1L, saleUpdateDto));
        verify(saleRepository, times(1)).save(any(Sale.class));
    }

    @Test
    void deletarVenda_DeveDeletarComSucesso() {
        when(saleRepository.existsById(anyLong())).thenReturn(true);

        assertDoesNotThrow(() -> saleService.delete(1L));
        verify(saleRepository, times(1)).deleteById(anyLong());
    }

    @Test
    void deletarVenda_DeveLancarExcecaoSeNaoEncontrada() {
        when(saleRepository.existsById(anyLong())).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> saleService.delete(1L));
        verify(saleRepository, never()).deleteById(anyLong());
    }

    @Test
    void deveLancarExcecaoGenericaAoExcluirVenda() {
        Long saleId = 1L;

        // Configura o comportamento do repositório para que o ID exista
        when(saleRepository.existsById(saleId)).thenReturn(true);

        // Configura o comportamento do repositório para lançar uma exceção genérica ao excluir
        doThrow(new RuntimeException("Erro inesperado"))
                .when(saleRepository).deleteById(saleId);

        // Verifica se o método lança a DatabaseException com a mensagem esperada
        DatabaseException exception = assertThrows(DatabaseException.class, () -> saleService.delete(saleId));
        assertEquals("Erro inesperado ao tentar excluir a venda.", exception.getMessage());

        // Verifica se o método foi chamado
        verify(saleRepository).deleteById(saleId);
    }

    @Test
    void deletarVenda_DeveLancarDatabaseExceptionSeDataIntegrityViolada() {
        when(saleRepository.existsById(anyLong())).thenReturn(true);
        doThrow(DataIntegrityViolationException.class).when(saleRepository).deleteById(anyLong());

        assertThrows(DatabaseException.class, () -> saleService.delete(1L));
        verify(saleRepository, times(1)).deleteById(anyLong());
    }
}

