package com.sagitta.authservice.authentication.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service
public interface AuthenticationService {
	


	String signup(String etin, String password);

	String login(String etin, String password);


	String logout(String etin, String accessToken);
}
