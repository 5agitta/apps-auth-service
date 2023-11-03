package com.sagitta.authservice.authentication.domain.dto;

import lombok.Data;

@Data
public class AccountLogoutRequestDto {
    private String etin;
    private String accessToken;
}
