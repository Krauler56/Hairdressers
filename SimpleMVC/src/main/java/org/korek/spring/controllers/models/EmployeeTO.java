package org.korek.spring.controllers.models;

import javax.validation.constraints.Size;

import org.korek.spring.repositories.entities.Employee;
import org.korek.spring.repositories.entities.Hairdressers;

public class EmployeeTO
{

	private long id;
	
	@Size(min=1, max=30)
	private String firstName;
	
	@Size(min=1, max=30)
	private String lastName;
	
	private long placeID;
	private String placeName;
	
	private float rate;

	public EmployeeTO()
	{
		super();
	}

	public EmployeeTO(Employee employee)
	{
		super();
		this.id = employee.getId();
		this.firstName = employee.getFirstName();
		this.lastName = employee.getLastName();
		
		Hairdressers hairdressers = employee.getHairdressers();
		if(hairdressers != null)
		{
			this.placeID = hairdressers.getId();
			this.placeName = hairdressers.getName(); 
		}
		
		int rateNumber = employee.getRateNumber();
		if(rateNumber > 0)
		{
			this.rate = employee.getRateSum() / (float) rateNumber;
		}
		
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

	public long getPlaceID()
	{
		return placeID;
	}

	public void setPlaceID(long placeID)
	{
		this.placeID = placeID;
	}

	public String getPlaceName()
	{
		return placeName;
	}

	public void setPlaceName(String placeName)
	{
		this.placeName = placeName;
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
		result = prime * result + ((firstName == null) ? 0 : firstName.hashCode());
		result = prime * result + ((lastName == null) ? 0 : lastName.hashCode());
		result = prime * result + (int) (placeID ^ (placeID >>> 32));
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
		EmployeeTO other = (EmployeeTO) obj;
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
		if (placeID != other.placeID)
			return false;
		return true;
	}

	@Override
	public String toString()
	{
		return "EmployeeTO [id=" + id + ", firstName=" + firstName + ", lastName=" + lastName + ", placeID=" + placeID + ", placeName=" + placeName + ", rate=" + rate + "]";
	}

	
	
}
