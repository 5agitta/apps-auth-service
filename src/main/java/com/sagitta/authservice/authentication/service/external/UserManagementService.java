package com.sagitta.authservice.authentication.service.external;

import com.sagitta.authservice.authentication.domain.dto.UserPrincipalDto;

import javax.servlet.http.HttpServletRequest;

public interface UserManagementService {
	
	UserPrincipalDto getUserPrincipal(HttpServletRequest originalRequest);
}
