package org.korek.spring.services.common.helpers;

import java.util.Collection;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

public class LoginDetails implements Authentication
{
	private static final long serialVersionUID = 1L;
	
	private boolean authenticated;
	private final String login;
	private final String password;
	private final Collection<? extends GrantedAuthority> roles;
	
	private final long id;
	
	public LoginDetails(String login, String password, Collection<? extends GrantedAuthority> roles, long id)
	{
		super();
		this.authenticated = true;
		this.login = login;
		this.password = password;
		this.roles = roles;
		this.id = id;
	}

	public LoginDetails()
	{
		super();
		this.authenticated = false;
		this.login = null;
		this.password = null;
		this.roles = null;
		this.id = 0L;
	}

	public boolean isAuthenticated()
	{
		return authenticated;
	}

	public String getLogin()
	{
		return login;
	}

	public String getPassword()
	{
		return password;
	}

	public Collection<? extends GrantedAuthority> getRoles()
	{
		return roles;
	}

	public long getId()
	{
		return id;
	}

	@Override
	public String getName()
	{
		return getLogin();
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities()
	{
		return getRoles();
	}

	@Override
	public Object getCredentials()
	{
		return getPassword();
	}

	@Override
	public Object getDetails()
	{
		return null;
	}

	@Override
	public Object getPrincipal()
	{
		return getLogin();
	}

	@Override
	public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException
	{
		if(isAuthenticated == false)
		{
			this.authenticated = false;
		}
		else throw new IllegalArgumentException();
	}
	
}
