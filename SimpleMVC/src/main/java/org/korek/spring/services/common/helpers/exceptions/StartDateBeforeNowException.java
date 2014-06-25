package org.korek.spring.services.common.helpers.exceptions;

import org.korek.spring.services.common.helpers.exceptions.base.NewVisitException;

public class StartDateBeforeNowException extends NewVisitException
{

	private static final long serialVersionUID = -9043792662418818432L;

	@Override
	public String getDataToModel()
	{
		return "dateBeforeNow";
	}

}
