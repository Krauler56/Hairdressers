package org.korek.spring.repositories.dao;

import java.util.List;

import org.korek.spring.repositories.dao.base.BaseDAO;
import org.korek.spring.repositories.dao.interfaces.IAuthDetailsDAO;
import org.korek.spring.repositories.entities.AuthDetails;
import org.springframework.stereotype.Repository;

@Repository
public class AuthDetailsDAO extends BaseDAO<AuthDetails> implements IAuthDetailsDAO
{

	public AuthDetailsDAO()
	{
		super(AuthDetails.class);
	}

	@SuppressWarnings("unchecked")
	@Override
	public AuthDetails getAuthDetailsByLogin(String login)
	{
		AuthDetails authDetails = null;

		List<AuthDetails> list = getCurrentSession().getNamedQuery(AuthDetails.GET_PASS_AND_ROLES_BY_LOGIN).setString("login", login).list();

		if (!list.isEmpty())
		{
			authDetails = list.get(0);
		}

		return authDetails;
	}

	@SuppressWarnings("unchecked")
	@Override 
	public long getClientID(String login)
	{
		List<Long> list = getCurrentSession().createQuery("select c.id from Client c where c.authDetails.login = :login").setString("login", login).list();

		if (!list.isEmpty())
		{
			return list.get(0);
		}

		return 0;
	}

	@SuppressWarnings("unchecked")
	@Override
	public long getEmployeeID(String login)
	{
		List<Long> list = getCurrentSession().createQuery("select e.id from Employee e where e.authDetails.login = :login").setString("login", login).list();

		if (!list.isEmpty())
		{
			return list.get(0);
		}

		return 0;
	}


}
