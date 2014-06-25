package org.korek.spring.services.common.interfaces;

import java.util.List;

import org.korek.spring.controllers.models.NewPlace;
import org.korek.spring.controllers.models.WorkPlace;
import org.korek.spring.controllers.models.EmployeeTO;
import org.korek.spring.services.common.helpers.exceptions.base.PlaceException;
import org.springframework.transaction.annotation.Transactional;

public interface IHairdressersManager
{
	@Transactional
	List<WorkPlace> getAllWorkPlaceWithAvailableVacancy();
	
	@Transactional
	List<WorkPlace> getAllWorkPlaceWithAtLeastOneEmployee();
	
	@Transactional
	List<WorkPlace> getAllWorkPlace();
	
	@Transactional
	List<EmployeeTO> getAllEmployees(long placeID); 
	
	@Transactional
	WorkPlace getWorkPlace(long id);
	
	@Transactional
	WorkPlace getWorkPlaceDetails(long id);
	
	@Transactional
	void addPlace(NewPlace newPlace) throws PlaceException ;

	@Transactional
	void saveChanges(WorkPlace workPlaceOld, WorkPlace workPlace) throws PlaceException;
}
