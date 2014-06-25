package org.korek.spring.services.common.helpers;

import org.korek.spring.controllers.models.NewPlace;
import org.korek.spring.controllers.models.WorkPlace;
import org.korek.spring.repositories.entities.Hairdressers;

public class OpenTime
{
	private final int hourFrom;
	private final int minFrom;

	private final int hourTo;
	private final int minTo;

	
	private OpenTime(String openFrom, String openTo)
	{
		super();
		String[] splitFrom = openFrom.split(":");
		String[] splitTo = openTo.split(":");

		this.hourFrom = Integer.parseInt(splitFrom[0]);
		this.minFrom = Integer.parseInt(splitFrom[1]);

		this.hourTo = Integer.parseInt(splitTo[0]);
		this.minTo = Integer.parseInt(splitTo[1]);
		
	}

	public OpenTime(WorkPlace workPlace)
	{
		this(workPlace.getOpenFrom(), workPlace.getOpenTo());
	}

	public OpenTime(NewPlace newPlace)
	{
		this(newPlace.getOpenFrom(), newPlace.getOpenTo());
	}

	public OpenTime(Hairdressers hairdressers)
	{
		this(hairdressers.getOpenFrom(), hairdressers.getOpenTo());
	}

	public int getHourFrom()
	{
		return hourFrom;
	}

	public int getMinFrom()
	{
		return minFrom;
	}

	public int getHourTo()
	{
		return hourTo;
	}

	public int getMinTo()
	{
		return minTo;
	}

	@Override
	public String toString()
	{
		return "OpenTime [hourFrom=" + hourFrom + ", minFrom=" + minFrom + ", hourTo=" + hourTo + ", minTo=" + minTo + "]";
	}

	
}
