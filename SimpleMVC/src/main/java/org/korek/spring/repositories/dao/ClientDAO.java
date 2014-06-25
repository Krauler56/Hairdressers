package org.korek.spring.repositories.dao;

import org.korek.spring.repositories.dao.base.BaseDAO;
import org.korek.spring.repositories.dao.interfaces.IClientDAO;
import org.korek.spring.repositories.entities.Client;
import org.springframework.stereotype.Repository;

@Repository
public class ClientDAO extends BaseDAO<Client> implements IClientDAO
{
	public ClientDAO()
	{
		super(Client.class);
	}
}
