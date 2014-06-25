package org.korek.spring.controllers.helpers;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

public class ConstraintHandlerDB
{

	public static void handleHibernateException(Exception e, RedirectAttributes attr)
	{
		if(e instanceof DataIntegrityViolationException){
			DataIntegrityViolationException exception = (DataIntegrityViolationException) e;
			
			Throwable c = exception.getCause();
			if(c instanceof ConstraintViolationException)
			{
				ConstraintViolationException cause = (ConstraintViolationException) c;
				
				String constraintName = cause.getConstraintName();
				attr.addFlashAttribute(constraintName, true);
			}
		}
	}
}
