package org.korek.spring.services.common;

import org.korek.spring.services.common.interfaces.IPassEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class PassEncoder implements IPassEncoder
{

	private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);
	
	public PassEncoder()
	{
		super();
	}

	@Override
	public String encode(String rawPass)
	{
		return encoder.encode(rawPass);
	}

	@Override
	public boolean maches(String rawPass, String encodedPass)
	{
		return encoder.matches(rawPass, encodedPass);
	}

}
