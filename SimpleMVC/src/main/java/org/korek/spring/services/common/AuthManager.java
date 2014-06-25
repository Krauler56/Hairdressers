package org.korek.spring.services.common;

import java.util.Collection;

import org.korek.spring.controllers.models.ChangePassword;
import org.korek.spring.repositories.dao.interfaces.IAuthDetailsDAO;
import org.korek.spring.repositories.dao.interfaces.IClientDAO;
import org.korek.spring.repositories.dao.interfaces.IEmployeeDAO;
import org.korek.spring.repositories.entities.AuthDetails;
import org.korek.spring.services.common.helpers.LoginDetails;
import org.korek.spring.services.common.helpers.RolesEnum;
import org.korek.spring.services.common.interfaces.IAuthManager;
import org.korek.spring.services.common.interfaces.IPassEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

@Service
public class AuthManager implements IAuthManager
{

	@Autowired
	IAuthDetailsDAO authDetailsDAO; 
	
	@Autowired
	IEmployeeDAO employeeDAO;
	
	@Autowired
	IClientDAO clientDAO;
	
	@Autowired
	IPassEncoder passEncoder;

	public LoginDetails doAuth(Authentication authentication)
	{
		LoginDetails loginDetails = null;

		String login = authentication.getName();
		String password = authentication.getCredentials() == null ? null : authentication.getCredentials().toString();

		AuthDetails authDetailsFromDB = getAuthDetailsFromDB(login);

		if (authDetailsFromDB != null && /*authDetailsFromDB.getPassword().equals(password)*/ passEncoder.maches(password, authDetailsFromDB.getPassword()))
		{
			RolesEnum roles = authDetailsFromDB.getRole();
			
			long userID;
			
			if(roles.equals(RolesEnum.USER))
			{
				userID = authDetailsDAO.getClientID(login);
			}
			else
			{
				userID = authDetailsDAO.getEmployeeID(login);
			}
			
			Collection<? extends GrantedAuthority> role = roles.getGrantedAuthorities();
			
			loginDetails = new LoginDetails(login, password, role, userID);
		}

		else
		{
			loginDetails = new LoginDetails();
		}

		return loginDetails;
	}
	
	private AuthDetails getAuthDetailsFromDB(String login)
	{
		AuthDetails authDetails = authDetailsDAO.getAuthDetailsByLogin(login);

		return authDetails;
	}


	@Override
	public boolean changePass(ChangePassword changePassword, Authentication authentication)
	{
		boolean changed = false;
		
		if (changePassword.getNewPass().equals(changePassword.getNewPassRepeat()))
		{
			if (authentication instanceof LoginDetails)
			{
				LoginDetails loginDetails = (LoginDetails) authentication;

				Collection<? extends GrantedAuthority> roles = loginDetails.getRoles();

				AuthDetails authDetails = null;
				if (roles.contains(RolesEnum.USER.getGrantedAuthority()))
				{
					authDetails = clientDAO.load(loginDetails.getId()).getAuthDetails();
				}
				else
				{
					authDetails = employeeDAO.load(loginDetails.getId()).getAuthDetails();
				}
				
				String oldPasswordDB = authDetails.getPassword();
				String oldPassword = changePassword.getOldPass();
				
				if(/*oldPasswordDB.equals(oldPassword)*/ passEncoder.maches(oldPassword, oldPasswordDB))
				{
					authDetails.setPassword(passEncoder.encode(changePassword.getNewPass()));
					changed = true;
				}
				
			}

		}

		return changed;
	}
	
}
