package org.korek.spring.services.common.helpers.exceptions;

import org.korek.spring.services.common.helpers.exceptions.base.NewVisitException;

public class NoEmployeeAtWorkPlace extends NewVisitException
{

	private static final long serialVersionUID = 8729433595513982352L;

	@Override
	public String getDataToModel()
	{
		return "noEmployeeAtWorkPlace";
	}

}
