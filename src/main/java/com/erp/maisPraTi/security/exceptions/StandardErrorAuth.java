package com.erp.maisPraTi.security.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StandardErrorAuth {
    private Integer status;
    private String error;
    private String message;
}
