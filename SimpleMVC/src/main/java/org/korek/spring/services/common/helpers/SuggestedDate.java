package org.korek.spring.services.common.helpers;

import java.util.Date;


public class SuggestedDate 
{

	private long id;
	private long employeeID;
	private Date startDate;
	private Date finishDate;

	public SuggestedDate()
	{
		super();
	}

	public SuggestedDate(long employeeID, Date startDate, Date finishDate)
	{
		super();
		this.employeeID = employeeID;
		this.startDate = startDate;
		this.finishDate = finishDate;
	}

	public long getId()
	{
		return id;
	}

	public void setId(long id)
	{
		this.id = id;
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

	public Date getFinishDate()
	{
		return finishDate;
	}

	public void setFinishDate(Date finishDate)
	{
		this.finishDate = finishDate;
	}
	
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((startDate == null) ? 0 : startDate.hashCode());
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
		SuggestedDate other = (SuggestedDate) obj;
		if (startDate == null)
		{
			if (other.startDate != null)
				return false;
		}
		else if (!startDate.equals(other.startDate))
			return false;
		return true;
	}

	@Override
	public String toString()
	{
		return "SuggestedDate [id=" + id + ", employeeID=" + employeeID + ", startDate=" + startDate + ", finishDate=" + finishDate + "]";
	}

	
	

}
