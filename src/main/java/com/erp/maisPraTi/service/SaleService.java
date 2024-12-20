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
import com.erp.maisPraTi.service.exceptions.InvalidValueException;
import com.erp.maisPraTi.service.exceptions.NotActivateException;
import com.erp.maisPraTi.service.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static com.erp.maisPraTi.util.EntityMapper.convertToDto;
import static com.erp.maisPraTi.util.EntityMapper.convertToEntity;

@Service
public class SaleService {

    @Autowired
    private SaleRepository saleRepository;

    @Autowired
    private ClientService clientService;

    @Transactional
    public SaleDto insert(SaleInsertDto dto) {
        if(dto.getExpectedDeliveryDate().isBefore(LocalDate.now()))
            throw  new InvalidValueException("A data de entrega não pode ser anterior a data de hoje.");

        Sale sale = new Sale();
        sale = convertToDto(dto, Sale.class);
        sale.setSaleDate(LocalDateTime.now());
        Long saleNumber = saleRepository.findMaxSaleNumber();
        sale.setSaleNumber(saleNumber != null ? saleNumber + 1 : 1);
        sale.setSaleStatus(SaleStatus.PENDING);
        sale.setClient(searchClient(dto.getClientId()));
        sale.setTotalSaleValue(new BigDecimal(0));
        sale = saleRepository.save(sale);
        return convertToDto(sale, SaleDto.class);
    }

    @Transactional(readOnly = true)
    public Optional<SaleDto> findById(Long id) {
        Sale sale = saleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Venda de id " + id + " não localizada."));
        return Optional.of(convertToDto(sale, SaleDto.class));
    }

    public Page<SaleDto> findAll(Pageable pageable) {
        Page<Sale> sales = saleRepository.findAll(pageable);
        return sales.map(c -> convertToDto(c, SaleDto.class));
    }

    @Transactional
    public SaleDto update(Long id, SaleUpdateDto saleUpdateDto) {
        verifyExistsId(id);
        try {
            Sale sale = saleRepository.getReferenceById(id);
            convertToEntity(saleUpdateDto, sale);
            sale = saleRepository.save(sale);
            return convertToDto(sale, SaleDto.class);
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException("Não foi possível fazer a alteração nesta venda.");
        }
    }

    @Transactional
    public void delete(Long id) {
        verifyExistsId(id);
        try {
            saleRepository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException("Não foi possível excluir esta venda. Ela pode estar vinculado a outros registros.");
        } catch (Exception e) {
            throw new DatabaseException("Erro inesperado ao tentar excluir a venda.");
        }
    }

    private void verifyExistsId(Long id) {
        if (!saleRepository.existsById(id)) {
            throw new ResourceNotFoundException("Venda de id " + id + " não localizada.");
        }
    }

    private Client searchClient(Long id){
        Optional<ClientDto> client = clientService.findById(id);
        if(client.isPresent() && client.get().getStatus().equals(PartyStatus.ACTIVE))
            return convertToEntity(client, Client.class);
        throw new NotActivateException("Cliente inativo ou suspenso.");
    }

    public void verifySalePending(Long saleId, BigDecimal quantityProductsDelivery){
        Sale sale = saleRepository.findById(saleId)
                .orElseThrow(() -> new ResourceNotFoundException("Venda de id " + saleId + " não localizada."));
        processDelivery(sale, quantityProductsDelivery);
    }

    public void processDelivery(Sale sale, BigDecimal quantityProductsDelivery){
        BigDecimal totalPending = sale.getTotalPendingDelivery();
        if(totalPending.compareTo(BigDecimal.ZERO) == 0){
            sale.setSaleStatus(SaleStatus.DELIVERED);
            sale.setSaleDelivery(LocalDateTime.now());
            saleRepository.save(sale);
        }
    }

}
