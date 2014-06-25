package org.korek.spring.controllers.models;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;
import org.korek.spring.repositories.entities.Hairdressers;

public class WorkPlace
{

	private long id;
	
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

	private float rate;

	public WorkPlace()
	{
		super();
	}

	public WorkPlace(long id, String name)
	{
		super();
		this.id = id;
		this.name = name;
	}

	public WorkPlace(Hairdressers hairdresser)
	{
		super();
		this.id = hairdresser.getId();
		this.name = hairdresser.getName();
		this.openFrom = hairdresser.getOpenFrom();
		this.openTo = hairdresser.getOpenTo();
		this.doorNumber = hairdresser.getAddress().getDoorNumber();
		this.buildingNumber = hairdresser.getAddress().getBuildingNumber();
		this.postCode = hairdresser.getAddress().getPostCode();
		this.street = hairdresser.getAddress().getStreet();
		this.city = hairdresser.getAddress().getCity();
		this.maxEmployees = hairdresser.getMaxEmployees();
	}

	public long getId()
	{
		return id;
	}

	public void setId(long id)
	{
		this.id = id;
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

	public String getPostCode()
	{
		return postCode;
	}

	public void setPostCode(String postCode)
	{
		this.postCode = postCode;
	}

	public int getMaxEmployees()
	{
		return maxEmployees;
	}

	public void setMaxEmployees(int maxEmployees)
	{
		this.maxEmployees = maxEmployees;
	}

	public float getRate()
	{
		return rate;
	}

	public void setRate(float rate)
	{
		this.rate = rate;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + buildingNumber;
		result = prime * result + ((city == null) ? 0 : city.hashCode());
		result = prime * result + doorNumber;
		result = prime * result + maxEmployees;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((openFrom == null) ? 0 : openFrom.hashCode());
		result = prime * result + ((openTo == null) ? 0 : openTo.hashCode());
		result = prime * result + ((postCode == null) ? 0 : postCode.hashCode());
		result = prime * result + ((street == null) ? 0 : street.hashCode());
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
		WorkPlace other = (WorkPlace) obj;
		if (buildingNumber != other.buildingNumber)
			return false;
		if (city == null)
		{
			if (other.city != null)
				return false;
		}
		else if (!city.equals(other.city))
			return false;
		if (doorNumber != other.doorNumber)
			return false;
		if (maxEmployees != other.maxEmployees)
			return false;
		if (name == null)
		{
			if (other.name != null)
				return false;
		}
		else if (!name.equals(other.name))
			return false;
		if (openFrom == null)
		{
			if (other.openFrom != null)
				return false;
		}
		else if (!openFrom.equals(other.openFrom))
			return false;
		if (openTo == null)
		{
			if (other.openTo != null)
				return false;
		}
		else if (!openTo.equals(other.openTo))
			return false;
		if (postCode == null)
		{
			if (other.postCode != null)
				return false;
		}
		else if (!postCode.equals(other.postCode))
			return false;
		if (street == null)
		{
			if (other.street != null)
				return false;
		}
		else if (!street.equals(other.street))
			return false;
		return true;
	}

}
