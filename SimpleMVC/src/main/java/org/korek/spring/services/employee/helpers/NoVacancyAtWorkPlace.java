package org.korek.spring.services.employee.helpers;

public class NoVacancyAtWorkPlace extends EditEmployeeException
{

	private static final long serialVersionUID = 5084641596245364977L;

	@Override
	public String getDataToModel()
	{
		return "noVacancy";
	}

}
