package com.erp.maisPraTi.dto.partyDto.clients;

import com.erp.maisPraTi.dto.partyDto.PartyDto;
import com.erp.maisPraTi.enums.Gender;
import com.erp.maisPraTi.service.validations.DocumentsValid;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@EqualsAndHashCode(callSuper = true)
@DocumentsValid
@AllArgsConstructor
@NoArgsConstructor
public class ClientUpdateDto extends PartyDto {

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private LocalDate birthDate;

    private BigDecimal creditLimit;

    private String notes;

}
