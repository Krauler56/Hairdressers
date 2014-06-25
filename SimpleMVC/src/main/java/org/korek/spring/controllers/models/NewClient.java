package org.korek.spring.controllers.models;

import javax.validation.constraints.Size;


public class NewClient
{

	@Size(min=1, max=30)
	private String login;
	
	@Size(min=1, max=30)
	private String password;
	
	@Size(min=1, max=30)
	private String firstName;
	
	@Size(min=1, max=30)
	private String lastName;

	public NewClient()
	{
		super();
	}

	public String getLogin()
	{
		return login;
	}

	public void setLogin(String login)
	{
		this.login = login;
	}

	public String getPassword()
	{
		return password;
	}

	public void setPassword(String password)
	{
		this.password = password;
	}

	public String getFirstName()
	{
		return firstName;
	}

	public void setFirstName(String firstName)
	{
		this.firstName = firstName;
	}

	public String getLastName()
	{
		return lastName;
	}

	public void setLastName(String lastName)
	{
		this.lastName = lastName;
	}

}
