package org.korek.spring.services.employee.helpers;

public class HasUpcomingVisitsException extends EditEmployeeException
{
	private static final long serialVersionUID = -4317117163409728290L;

	@Override
	public String getDataToModel()
	{
		return "hasUpcomingVisits";
	}

}
