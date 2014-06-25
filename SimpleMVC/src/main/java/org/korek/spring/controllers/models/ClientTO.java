package org.korek.spring.controllers.models;

import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;
import org.korek.spring.repositories.entities.Client;

public class ClientTO
{

	private long id;
	
	@Size(min=1, max=30)
	private String firstName;
	
	@Size(min=1, max=30)
	private String lastName;
	
	@Email
	private String email;

	public ClientTO()
	{
		super();
	}

	public ClientTO(Client client)
	{
		super();
		this.id = client.getId();
		this.firstName = client.getFirstName();
		this.lastName = client.getLastName();
		this.email = client.getEmail();

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

	public String getEmail()
	{
		return email;
	}

	public void setEmail(String email)
	{
		this.email = email;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result + ((firstName == null) ? 0 : firstName.hashCode());
		result = prime * result + ((lastName == null) ? 0 : lastName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ClientTO other = (ClientTO) obj;
		if (email == null)
		{
			if (other.email != null)
				return false;
		}
		else if (!email.equals(other.email))
			return false;
		if (firstName == null)
		{
			if (other.firstName != null)
				return false;
		}
		else if (!firstName.equals(other.firstName))
			return false;
		if (lastName == null)
		{
			if (other.lastName != null)
				return false;
		}
		else if (!lastName.equals(other.lastName))
			return false;
		return true;
	}

	@Override
	public String toString()
	{
		return "ClientTO [id=" + id + ", firstName=" + firstName + ", lastName=" + lastName + ", email=" + email + "]";
	}

	
	
	

}
