package org.korek.spring.repositories.entities;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import org.korek.spring.controllers.models.NewClient;
import org.korek.spring.services.common.helpers.RolesEnum;

@Entity
public class Client
{

	@Id
	@GeneratedValue
	private long id;
	
	@Column(length = 30)
	private String firstName;
	
	@Column(length = 30)
	private String lastName;
	
	private String email;
	
	@OneToOne(cascade=CascadeType.ALL)
	private AuthDetails authDetails;

	public Client()
	{
		super();
	}

	public Client(NewClient newClient)
	{
		this.firstName = newClient.getFirstName();
		this.lastName = newClient.getLastName();
		this.authDetails = new AuthDetails(newClient.getLogin(), newClient.getPassword(), RolesEnum.USER);
		this.email = "";
		
	}

	public long getId()
	{
		return id;
	}

	public void setId(long id)
	{
		this.id = id;
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

	public AuthDetails getAuthDetails()
	{
		return authDetails;
	}

	public void setAuthDetails(AuthDetails authDetails)
	{
		this.authDetails = authDetails;
	}

	public String getEmail()
	{
		return email;
	}

	public void setEmail(String email)
	{
		this.email = email;
	}

	
}
