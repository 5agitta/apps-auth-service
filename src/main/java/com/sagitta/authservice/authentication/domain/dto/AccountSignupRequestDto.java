package com.sagitta.authservice.authentication.domain.dto;

import lombok.Data;

@Data
public class AccountSignupRequestDto {
    private String etin;
    private String password;
}
