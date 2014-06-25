package org.korek.spring.services.common.helpers;

import java.util.Date;
import java.util.List;

public class SearchingForVisitResult
{

	private boolean isDateAvailable;

	private final Date startDate;

	private final Date finishDate;

	private final long placeID;

	private long employeeID;

	private List<SuggestedDate> suggestedDates;

	public SearchingForVisitResult(Date startDate, Date finishDate, long placeID)
	{
		super();
		this.startDate = startDate;
		this.finishDate = finishDate;
		this.placeID = placeID;
	}

	public boolean isDateAvailable()
	{
		return isDateAvailable;
	}

	public void setDateAvailable(boolean isDateAvailable)
	{
		this.isDateAvailable = isDateAvailable;
	}

	public long getEmployeeID()
	{
		return employeeID;
	}

	public void setEmployeeID(long employeeID)
	{
		this.employeeID = employeeID;
	}

	public List<SuggestedDate> getSuggestedDates()
	{
		return suggestedDates;
	}

	public void setSuggestedDates(List<SuggestedDate> suggestedDates)
	{
		this.suggestedDates = suggestedDates;
	}

	public Date getStartDate()
	{
		return startDate;
	}

	public Date getFinishDate()
	{
		return finishDate;
	}

	public long getPlaceID()
	{
		return placeID;
	}

	@Override
	public String toString()
	{
		return "SearchingForVisitResult [isDateAvailable=" + isDateAvailable + ", startDate=" + startDate + ", finishDate=" + finishDate + ", placeID=" + placeID + ", employeeID=" + employeeID + ", suggestedDates=" + suggestedDates + "]";
	}

}
