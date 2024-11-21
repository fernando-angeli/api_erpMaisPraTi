package com.erp.maisPraTi.dto.partyDto.clients;

import com.erp.maisPraTi.dto.partyDto.PartyDto;
import com.erp.maisPraTi.enums.Gender;
import com.erp.maisPraTi.service.validations.DocumentsValid;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@EqualsAndHashCode(callSuper = true)
@DocumentsValid
@AllArgsConstructor
@NoArgsConstructor
public class ClientDto extends PartyDto {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private LocalDate birthDate;

}
