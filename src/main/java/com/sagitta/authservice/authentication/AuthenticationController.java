package com.sagitta.authservice.authentication;

import com.sagitta.authservice.authentication.domain.dto.AccountLoginRequestDto;
import com.sagitta.authservice.authentication.domain.dto.AccountSignupRequestDto;
import com.sagitta.authservice.authentication.domain.dto.UserPrincipalDto;
import com.sagitta.authservice.authentication.service.external.UserManagementService;

import com.sagitta.authservice.authentication.service.AuthenticationService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

import static org.springframework.http.HttpHeaders.SET_COOKIE;

@RestController
@RequestMapping("/authenticate")
public class AuthenticationController {


    private final UserManagementService userManagementService;

    private final AuthenticationService authenticationService;

    public AuthenticationController(UserManagementService userManagementService,
                                    AuthenticationService authenticationService) {
        this.userManagementService = userManagementService;
        this.authenticationService = authenticationService;
    }

    @PostMapping("/signup")
    public String signup(AccountSignupRequestDto accountSignupRequestDto) {
        return authenticationService.signup(accountSignupRequestDto.getEtin(), accountSignupRequestDto.getPassword());
    }

    @PostMapping("/login")
    public String signup(AccountLoginRequestDto accountLoginRequestDto) {
        return authenticationService.login(accountLoginRequestDto.getEtin(), accountLoginRequestDto.getPassword());
    }


}
