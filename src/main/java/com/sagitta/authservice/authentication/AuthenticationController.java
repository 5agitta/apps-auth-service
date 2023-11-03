package com.sagitta.authservice.authentication;

import com.sagitta.authservice.authentication.domain.dto.AccountLoginRequestDto;
import com.sagitta.authservice.authentication.domain.dto.AccountLogoutRequestDto;
import com.sagitta.authservice.authentication.domain.dto.AccountSignupRequestDto;
import com.sagitta.authservice.authentication.service.AuthenticationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/authenticate")
public class AuthenticationController {


    private final AuthenticationService authenticationService;

    public AuthenticationController(
            AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody AccountSignupRequestDto accountSignupRequestDto) {
        return authenticationService.signup(accountSignupRequestDto.getEtin(), accountSignupRequestDto.getPassword());
    }

    @PostMapping("/login")
    public ResponseEntity<String> signup(@RequestBody AccountLoginRequestDto accountLoginRequestDto) {
        return authenticationService.login(accountLoginRequestDto.getEtin(), accountLoginRequestDto.getPassword());
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestBody AccountLogoutRequestDto accountLogoutRequestDto) {
        return authenticationService.logout(accountLogoutRequestDto.getEtin(), accountLogoutRequestDto.getAccessToken());
    }


}
