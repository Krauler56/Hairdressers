package org.korek.spring.web.config;

import org.korek.spring.services.common.helpers.LoginDetails;
import org.korek.spring.services.common.interfaces.IAuthManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider
{

	@Autowired
	IAuthManager authManager;

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException
	{
		LoginDetails loginDetails = authManager.doAuth(authentication);

		if (loginDetails.isAuthenticated() == false)
		{
			throw new BadCredentialsException("Login failed.");
		}
		
		return loginDetails;
	}

	@Override
	public boolean supports(Class<?> authentication)
	{
		boolean supports = false;

		if (authentication.equals(UsernamePasswordAuthenticationToken.class))
		{
			supports = true;
		}

		return supports;
	}

}
