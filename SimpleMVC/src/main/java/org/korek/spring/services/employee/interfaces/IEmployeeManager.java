package org.korek.spring.services.employee.interfaces;

import java.util.List;

import org.korek.spring.controllers.models.EmployeeTO;
import org.korek.spring.controllers.models.EmployeeVisit;
import org.korek.spring.controllers.models.NewEmployee;
import org.korek.spring.controllers.models.WorkPlace;
import org.korek.spring.services.employee.helpers.EditEmployeeException;
import org.springframework.transaction.annotation.Transactional;

public interface IEmployeeManager
{
	
	@Transactional(readOnly = true)
	List<EmployeeTO> getAllEmployees();
	
	@Transactional(readOnly = true)
	EmployeeTO getEmployeeTO(long employeeID);
	
	@Transactional(readOnly = true)
	List<EmployeeVisit> getAllEmployeeVisits(long id);

	@Transactional(readOnly = true)
	List<EmployeeVisit> getUpcomingEmployeeVisits(long id);
	
	@Transactional(readOnly = true)
	List<EmployeeVisit> getPastEmployeeVisits(long id);

	@Transactional(readOnly = true)
	WorkPlace getEmployeesWorkPlace(long employeeID);
	
	@Transactional
	boolean saveChanges(EmployeeTO employeeOld, EmployeeTO employee) throws EditEmployeeException;
	
	@Transactional
	boolean addEmployee(NewEmployee newEmployee) throws Exception;
	
	@Transactional
	void confirmPastVisit(long visitID, long employeeID);
	
	@Transactional
	void confirmPastVisitAdmin(long visitID);

	@Transactional
	void cancelVisitEmployee(long visitID, long employeeID);
	
	@Transactional
	void cancelVisitAdmin(long visitID);

}
