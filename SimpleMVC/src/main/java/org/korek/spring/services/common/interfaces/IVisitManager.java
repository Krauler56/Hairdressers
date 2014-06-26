package org.korek.spring.services.common.interfaces;

import java.util.List;

import org.korek.spring.controllers.models.EmployeeVisit;
import org.korek.spring.controllers.models.NewVisit;
import org.korek.spring.services.common.helpers.SearchingForVisitResult;
import org.korek.spring.services.common.helpers.exceptions.base.NewVisitException;
import org.springframework.transaction.annotation.Transactional;

public interface IVisitManager
{
	@Transactional(readOnly = true)
	List<EmployeeVisit> getAllPlaceVisit(long placeID);

	@Transactional(readOnly = true)
	List<EmployeeVisit> getPastPlaceVisit(long placeID);

	@Transactional(readOnly = true)
	List<EmployeeVisit> getUpcomingPlaceVisit(long placeID);

	@Transactional(readOnly = true)
	SearchingForVisitResult checkForVisit(NewVisit newVisit) throws NewVisitException;

	@Transactional
	boolean bookVisit(NewVisit newVisit, Long suggestedDateID);
}
