package org.korek.spring.services.common.helpers.exceptions;

import org.korek.spring.services.common.helpers.exceptions.base.PlaceException;

public class IllegalNumberOfMaxEmployyesException extends PlaceException
{

	private static final long serialVersionUID = -1123770086532909088L;

	@Override
	public String getDataToModel()
	{
		return "exceededMaxEmployees";
	}

}
