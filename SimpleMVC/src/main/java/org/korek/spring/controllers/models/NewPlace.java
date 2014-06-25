package org.korek.spring.controllers.models;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;

public class NewPlace
{

	@Size(min = 1, max = 30)
	private String name;

	@NotNull
	@DecimalMin("1")
	private int maxEmployees;

	@NotEmpty
	private String openFrom;

	@NotEmpty
	private String openTo;

	@Size(min = 1, max = 30)
	private String city;

	@Size(min = 1, max = 30)
	private String postCode;

	@Size(min = 1, max = 30)
	private String street;

	@NotNull
	@DecimalMin("1")
	private int buildingNumber;

	@NotNull
	@DecimalMin("1")
	private int doorNumber;

	public NewPlace()
	{
		super();
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getCity()
	{
		return city;
	}

	public void setCity(String city)
	{
		this.city = city;
	}

	public String getPostCode()
	{
		return postCode;
	}

	public void setPostCode(String postCode)
	{
		this.postCode = postCode;
	}

	public String getStreet()
	{
		return street;
	}

	public void setStreet(String street)
	{
		this.street = street;
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

	public int getBuildingNumber()
	{
		return buildingNumber;
	}

	public void setBuildingNumber(int buildingNumber)
	{
		this.buildingNumber = buildingNumber;
	}

	public int getDoorNumber()
	{
		return doorNumber;
	}

	public void setDoorNumber(int doorNumber)
	{
		this.doorNumber = doorNumber;
	}
 
}
