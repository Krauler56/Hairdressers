package org.korek.spring.controllers.models;

import java.util.Date;

import org.korek.spring.repositories.entities.Visit;

public class ClientVisit
{

	private long id;
	private String placeName;
	private long placeID;

	private String employeeFirstName;
	private String employeeName;
	private long employeeID;

	private Date startDate;
	
	private int rate;
	private boolean confirmed;
	

	public ClientVisit(Visit visit)
	{
		super();
		this.id = visit.getId();
		this.placeName = visit.getHairdressers().getName();
		this.placeID = visit.getHairdressers().getId();
		this.employeeFirstName = visit.getEmployee().getFirstName();
		this.employeeName = visit.getEmployee().getLastName();
		this.employeeID = visit.getEmployee().getId();
		this.startDate = visit.getStartDate().getTime();
		this.rate = visit.getServiceRate();
		this.confirmed = visit.isTookPlace();
	}

	public String getPlaceName()
	{
		return placeName;
	}

	public void setPlaceName(String placeName)
	{
		this.placeName = placeName;
	}

	public long getPlaceID()
	{
		return placeID;
	}

	public void setPlaceID(long placeID)
	{
		this.placeID = placeID;
	}

	public String getEmployeeName()
	{
		return employeeName;
	}

	public void setEmployeeName(String employeeName)
	{
		this.employeeName = employeeName;
	}

	public long getEmployeeID()
	{
		return employeeID;
	}

	public void setEmployeeID(long employeeID)
	{
		this.employeeID = employeeID;
	}

	public Date getStartDate()
	{
		return startDate;
	}

	public void setStartDate(Date startDate)
	{
		this.startDate = startDate;
	}

	public long getId()
	{
		return id;
	}

	public void setId(long id)
	{
		this.id = id;
	}

	public int getRate()
	{
		return rate;
	}

	public void setRate(int rate)
	{
		this.rate = rate;
	}

	public boolean isConfirmed()
	{
		return confirmed;
	}

	public void setConfirmed(boolean confirmed)
	{
		this.confirmed = confirmed;
	}

	public String getEmployeeFirstName()
	{
		return employeeFirstName;
	}

	public void setEmployeeFirstName(String employeeFirstName)
	{
		this.employeeFirstName = employeeFirstName;
	}
}
