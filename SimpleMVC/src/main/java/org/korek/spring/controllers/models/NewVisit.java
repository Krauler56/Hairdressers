package org.korek.spring.controllers.models;

import org.korek.spring.services.common.helpers.SearchingForVisitResult;
import org.korek.spring.services.common.helpers.SuggestedDate;

public class NewVisit
{

	private long workPlaceID;
	private long clientID;
	private long employeeID;

	private String startTime;
	private String date;

	private SuggestedDate suggestedDate;

	private String hairType;
	
	SearchingForVisitResult searchingForVisitResult;

	public NewVisit()
	{
		super();
	}

	public long getClientID()
	{
		return clientID;
	}

	public void setClientID(long clientID)
	{
		this.clientID = clientID;
	}

	public long getEmployeeID()
	{
		return employeeID;
	}

	public void setEmployeeID(long employeeID)
	{
		this.employeeID = employeeID;
	}

	public String getStartTime()
	{
		return startTime;
	}

	public void setStartTime(String startTime)
	{
		this.startTime = startTime;
	}

	public String getDate()
	{
		return date;
	}

	public void setDate(String date)
	{
		this.date = date;
	}

	public String getHairType()
	{
		return hairType;
	}

	public void setHairType(String hairType)
	{
		this.hairType = hairType;
	}

	public long getWorkPlaceID()
	{
		return workPlaceID;
	}

	public void setWorkPlaceID(long workPlaceID)
	{
		this.workPlaceID = workPlaceID;
	}

	public SuggestedDate getSuggestedDate()
	{
		return suggestedDate;
	}

	public void setSuggestedDate(SuggestedDate suggestedDate)
	{
		this.suggestedDate = suggestedDate;
	}

	public SearchingForVisitResult getSearchingForVisitResult()
	{
		return searchingForVisitResult;
	}

	public void setSearchingForVisitResult(SearchingForVisitResult searchingForVisitResult)
	{
		this.searchingForVisitResult = searchingForVisitResult;
	}

	@Override
	public String toString()
	{
		return "NewVisit [workPlaceID=" + workPlaceID + ", clientID=" + clientID + ", employeeID=" + employeeID + ", startTime=" + startTime + ", date=" + date + ", suggestedDate=" + suggestedDate + ", hairType=" + hairType + ", searchingForVisitResult=" + searchingForVisitResult + "]";
	}

	
	

}
