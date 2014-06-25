package org.korek.spring.services.common.helpers.exceptions;

import org.korek.spring.services.common.helpers.exceptions.base.PlaceException;

public class PlaceCloseBeforeOpenException extends PlaceException
{

	private static final long serialVersionUID = 1L;

	@Override
	public String getDataToModel()
	{
		return "closeBeforeOpen";
	}

}
