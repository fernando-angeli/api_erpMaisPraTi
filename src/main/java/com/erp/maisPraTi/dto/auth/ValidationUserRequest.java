package com.erp.maisPraTi.dto.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ValidationUserRequest {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String email;
    private String cpf;
}
