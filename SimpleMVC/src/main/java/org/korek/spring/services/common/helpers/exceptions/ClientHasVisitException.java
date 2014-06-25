package org.korek.spring.services.common.helpers.exceptions;

import org.korek.spring.services.common.helpers.exceptions.base.NewVisitException;

public class ClientHasVisitException extends NewVisitException
{

	private static final long serialVersionUID = -6917888282686629837L;

	@Override
	public String getDataToModel()
	{
		return "hasVisitsClient";
	}

}
