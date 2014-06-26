package org.korek.spring.repositories.dao;

import java.util.List;

import org.korek.spring.repositories.dao.base.BaseDAO;
import org.korek.spring.repositories.dao.interfaces.IHairdressersDAO;
import org.korek.spring.repositories.entities.Hairdressers;
import org.springframework.stereotype.Repository;

@Repository
public class HairdressersDAO extends BaseDAO<Hairdressers> implements IHairdressersDAO
{

	public HairdressersDAO()
	{
		super(Hairdressers.class);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Hairdressers> getAllPlacesWithAtLeastOneEmployee()
	{
		List<Hairdressers> list = getCurrentSession().getNamedQuery(Hairdressers.ALL_WITH_AT_LEAST_1_EMPLOYEE).list();

		return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Hairdressers> getAllWithAvailableVacancy()
	{
		List<Hairdressers> list = getCurrentSession().getNamedQuery(Hairdressers.ALL_WITH_AVAILABLE_VACANCY).list();
		
		return list;
	}

}
