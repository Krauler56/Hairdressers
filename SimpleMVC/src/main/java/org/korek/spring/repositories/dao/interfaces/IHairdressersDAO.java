package org.korek.spring.repositories.dao.interfaces;

import java.util.List;

import org.korek.spring.repositories.dao.base.IBaseDAO;
import org.korek.spring.repositories.entities.Hairdressers;

public interface IHairdressersDAO extends IBaseDAO<Hairdressers> 
{
	List<Hairdressers> getAllPlacesWithAtLeastOneEmployee();

	List<Hairdressers> getAllWithAvailableVacancy();

}
