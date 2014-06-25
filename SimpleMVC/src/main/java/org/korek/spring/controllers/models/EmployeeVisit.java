package org.korek.spring.controllers.models;

import java.util.Date;

import org.korek.spring.repositories.entities.Client;
import org.korek.spring.repositories.entities.Employee;
import org.korek.spring.repositories.entities.Hairdressers;
import org.korek.spring.repositories.entities.Visit;

public class EmployeeVisit
{
	private long visitID;
	private Date startDate;
	private boolean confirmed;
	private int rate;

	private long employeeID;
	private String employeeFirstName;
	private String employeeLastName;

	private long clientID;
	private String clientFirstName;
	private String clientLastName;

	private long placeID;
	private String placeName;

	public EmployeeVisit()
	{
		super();
	}

	public EmployeeVisit(Visit visit)
	{
		super();
		this.visitID = visit.getId();
		this.startDate = visit.getStartDate().getTime();
		this.confirmed = visit.isTookPlace();
		this.rate = visit.getServiceRate();

		Employee employee = visit.getEmployee();
		this.employeeID = employee.getId();
		this.employeeFirstName = employee.getFirstName();
		this.employeeLastName = employee.getLastName();

		Hairdressers hairdressers = visit.getHairdressers();
		this.placeID = hairdressers.getId();
		this.placeName = hairdressers.getName();

		Client client = visit.getClient();
		if (client != null)
		{
			this.clientID = client.getId();
			this.clientFirstName = client.getFirstName();
			this.clientLastName = client.getLastName();
		}
	}

	public Date getStartDate()
	{
		return startDate;
	}

	public void setStartDate(Date startDate)
	{
		this.startDate = startDate;
	}

	public long getEmployeeID()
	{
		return employeeID;
	}

	public void setEmployeeID(long employeeID)
	{
		this.employeeID = employeeID;
	}

	public String getEmployeeFirstName()
	{
		return employeeFirstName;
	}

	public void setEmployeeFirstName(String employeeFirstName)
	{
		this.employeeFirstName = employeeFirstName;
	}

	public boolean isConfirmed()
	{
		return confirmed;
	}

	public void setConfirmed(boolean confirmed)
	{
		this.confirmed = confirmed;
	}

	public long getVisitID()
	{
		return visitID;
	}

	public void setVisitID(long visitID)
	{
		this.visitID = visitID;
	}

	public String getEmployeeLastName()
	{
		return employeeLastName;
	}

	public void setEmployeeLastName(String employeeLastName)
	{
		this.employeeLastName = employeeLastName;
	}

	public String getClientFirstName()
	{
		return clientFirstName;
	}

	public void setClientFirstName(String clientFirstName)
	{
		this.clientFirstName = clientFirstName;
	}

	public String getClientLastName()
	{
		return clientLastName;
	}

	public void setClientLastName(String clientLastName)
	{
		this.clientLastName = clientLastName;
	}

	public int getRate()
	{
		return rate;
	}

	public void setRate(int rate)
	{
		this.rate = rate;
	}

	public long getClientID()
	{
		return clientID;
	}

	public void setClientID(long clientID)
	{
		this.clientID = clientID;
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

}
