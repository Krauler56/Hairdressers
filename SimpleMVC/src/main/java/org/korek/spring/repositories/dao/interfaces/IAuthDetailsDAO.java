package org.korek.spring.repositories.dao.interfaces;

import org.korek.spring.repositories.entities.AuthDetails;

public interface IAuthDetailsDAO 
{
	AuthDetails getAuthDetailsByLogin(String login);
	
	long getClientID(String login);
	
	long getEmployeeID(String login);
}
