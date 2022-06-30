package com.simplonsuivi.co.listener;

import java.util.concurrent.ExecutionException;

import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.stereotype.Component;

import com.simplonsuivi.co.service.LoginAttemptService;

@Component
public class AuthenticationFailureListener {

	private LoginAttemptService loginAttemptService;

	public AuthenticationFailureListener(LoginAttemptService loginAttemptService) {
		super();
		this.loginAttemptService = loginAttemptService;
	}
	
	@EventListener
	public void onAuthenticationFailure(AuthenticationFailureBadCredentialsEvent event) throws ExecutionException {
		Object principal=event.getAuthentication().getPrincipal();
		
		if(principal instanceof String) {
			String username=(String) event.getAuthentication().getPrincipal();
			loginAttemptService.addUserToLoginAttemptCache(username);
		}
	}
}
