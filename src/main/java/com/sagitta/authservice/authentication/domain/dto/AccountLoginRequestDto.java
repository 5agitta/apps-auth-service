package com.sagitta.authservice.authentication.domain.dto;

import lombok.Data;

@Data
public class AccountLoginRequestDto {
    private String etin;
    private String password;
}
