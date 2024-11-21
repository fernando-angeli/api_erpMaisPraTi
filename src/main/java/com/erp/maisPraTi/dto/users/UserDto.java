package com.erp.maisPraTi.dto.users;

import com.erp.maisPraTi.enums.PartyStatus;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @NotBlank(message = "Campo obrigatório.")
    private String fullName;

    @Column(unique = true)
    @Email(message="E-mail é obrigatório.")
    private String email;

    private LocalDate birthDate;

    private String phoneNumber;

    @Column(unique = true)
    private String cpf;

    private String address;

    private String number;

    private String district;

    private String zipCode;

    private String city;

    private String state;

    private String country;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private List<RoleDto> roles;

    @Enumerated(EnumType.STRING)
    private PartyStatus status;

    private Map<String, String> cards;

}
