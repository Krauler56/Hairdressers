package org.korek.spring.repositories.entities;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;
import org.korek.spring.controllers.models.NewPlace;

@NamedQueries({
	@NamedQuery(
			name = Hairdressers.ALL_WITH_AT_LEAST_1_EMPLOYEE,
			query = "from Hairdressers where currentEmployees > 0 order by name"
	)
})

@Entity
public class Hairdressers
{

	public static final String ALL_WITH_AT_LEAST_1_EMPLOYEE = "Hairdressers1";

	@Id
	@GeneratedValue
	private long id;

	private int currentEmployees;

	private int maxEmployees;

	@Column(length = 30)
	private String name;

	@Column(length = 5)
	private String openFrom;

	@Column(length = 5)
	private String openTo;

	@Embedded
	private Address address;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "hairdressers")
	private List<Employee> employees;

	public Hairdressers()
	{
		super();
	}

	public Hairdressers(NewPlace newPlace)
	{
		super();
		this.name = newPlace.getName();
		Address address = new Address(newPlace.getDoorNumber(), newPlace.getBuildingNumber(), newPlace.getStreet(), newPlace.getPostCode(), newPlace.getCity());
		this.address = address;
		this.openFrom = newPlace.getOpenFrom();
		this.openTo = newPlace.getOpenTo();
		this.maxEmployees = newPlace.getMaxEmployees();
		this.currentEmployees = 0;
	}

	public long getId()
	{
		return id;
	}

	public void setId(long id)
	{
		this.id = id;
	}

	public Address getAddress()
	{
		return address;
	}

	public void setAddress(Address address)
	{
		this.address = address;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public List<Employee> getEmployees()
	{
		return employees;
	}

	public void setEmployees(List<Employee> employees)
	{
		this.employees = employees;
	}



	public String getOpenFrom()
	{
		return openFrom;
	}

	public void setOpenFrom(String openFrom)
	{
		this.openFrom = openFrom;
	}

	public String getOpenTo()
	{
		return openTo;
	}

	public void setOpenTo(String openTo)
	{
		this.openTo = openTo;
	}

	public int getMaxEmployees()
	{
		return maxEmployees;
	}

	public void setMaxEmployees(int maxEmployees)
	{
		this.maxEmployees = maxEmployees;
	}

	public int getCurrentEmployees()
	{
		return currentEmployees;
	}

	public void setCurrentEmployees(int currentEmployees)
	{
		this.currentEmployees = currentEmployees;
	}
	
	

}
