package com.erp.maisPraTi.dto.partyDto;

import com.erp.maisPraTi.enums.PartyStatus;
import com.erp.maisPraTi.enums.TypePfOrPj;
import com.erp.maisPraTi.service.validations.DocumentsValid;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@DocumentsValid
public abstract class PartyDto {

    @NotBlank(message = "O nome ou razão social é obrigatório.")
    private String fullName;

    @NotNull(message = "O tipo de cadastro é obrigatório (PF ou PJ).")
    @Enumerated(EnumType.STRING)
    private TypePfOrPj typePfOrPj;

    @NotBlank(message = "O CPF ou CNPJ é obrigatório.")
    private String cpfCnpj;

    @JsonProperty(defaultValue = "isento")
    private String stateRegistration;

    @NotBlank(message = "O telefone é obrigatório.")
    private String phoneNumber;

    @NotBlank(message = "O e-mail é obrigatório.")
    private String email;

    @NotBlank(message = "O endereço é obrigatório.")
    private String address;

    @NotNull(message = "O número é obrigatório.")
    private String number;

    @NotBlank(message = "O bairro é obrigatório.")
    private String district;

    @NotBlank(message = "O CEP é obrigatório.")
    private String zipCode;

    @NotBlank(message = "A cidade é obrigatória.")
    private String city;

    @NotBlank(message = "O estado é obrigatório.")
    private String state;

    @NotBlank(message = "O país é obrigatório.")
    private String country;

    @Enumerated(EnumType.STRING)
    private PartyStatus status;

    private BigDecimal creditLimit;

    private String notes;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDateTime createdAt;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDateTime updatedAt;
}
