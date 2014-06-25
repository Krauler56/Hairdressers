package org.korek.spring.repositories.entities;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import org.korek.spring.controllers.models.NewEmployee;
import org.korek.spring.services.common.helpers.RolesEnum;

@Entity
public class Employee
{

	@Id
	@GeneratedValue
	private long id;
	
	@Column(length = 30)
	private String firstName;
	
	@Column(length = 30)
	private String lastName;

	@OneToOne(cascade = CascadeType.ALL)
	private AuthDetails authDetails;

	@ManyToOne
	@JoinColumn(name = "hairdresserId")
	private Hairdressers hairdressers;
	
	private int rateSum;
	private int rateNumber;

	public Employee()
	{
		super();
	}

	public Employee(NewEmployee employee)
	{
		super();
		this.firstName = employee.getFirstName();
		this.lastName = employee.getLastName();
		RolesEnum role = employee.isAdmin() ? RolesEnum.ADMIN : RolesEnum.EMPLOYEE;
		this.authDetails = new AuthDetails(employee.getLogin(), employee.getPassword(), role);
	}

	public Employee(String firstName, String lastName, AuthDetails authDetails)
	{
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.authDetails = authDetails;
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

	public Hairdressers getHairdressers()
	{
		return hairdressers;
	}

	public void setHairdressers(Hairdressers hairdressers)
	{
		this.hairdressers = hairdressers;
	}

	public int getRateSum()
	{
		return rateSum;
	}

	public void setRateSum(int rateSum)
	{
		this.rateSum = rateSum;
	}

	public int getRateNumber()
	{
		return rateNumber;
	}

	public void setRateNumber(int rateNumber)
	{
		this.rateNumber = rateNumber;
	}
	
	
}
