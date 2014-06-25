package org.korek.spring.repositories.entities;

import javax.persistence.Column;

public class Address
{
	private int doorNumber;
	private int buildingNumber;
	
	@Column(length = 30)
	private String street;
	
	@Column(length = 30)
	private String postCode;
	
	@Column(length = 30)
	private String city;

	public Address()
	{
		super();
	}

	public Address(int doorNumber, int buildingNumber, String street, String postCode, String city)
	{
		super();
		this.doorNumber = doorNumber;
		this.buildingNumber = buildingNumber;
		this.street = street;
		this.postCode = postCode;
		this.city = city;
	}

	public int getDoorNumber()
	{
		return doorNumber;
	}

	public void setDoorNumber(int doorNumber)
	{
		this.doorNumber = doorNumber;
	}

	public int getBuildingNumber()
	{
		return buildingNumber;
	}

	public void setBuildingNumber(int buildingNumber)
	{
		this.buildingNumber = buildingNumber;
	}

	public String getStreet()
	{
		return street;
	}

	public void setStreet(String street)
	{
		this.street = street;
	}

	public String getPostCode()
	{
		return postCode;
	}

	public void setPostCode(String postCode)
	{
		this.postCode = postCode;
	}

	public String getCity()
	{
		return city;
	}

	public void setCity(String city)
	{
		this.city = city;
	}

}
