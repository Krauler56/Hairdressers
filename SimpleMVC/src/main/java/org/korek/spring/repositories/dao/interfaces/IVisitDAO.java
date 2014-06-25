package org.korek.spring.repositories.dao.interfaces;

import java.util.Date;
import java.util.List;

import org.korek.spring.repositories.dao.base.IBaseDAO;
import org.korek.spring.repositories.entities.Employee;
import org.korek.spring.repositories.entities.Visit;

public interface IVisitDAO extends IBaseDAO<Visit>
{
	boolean isDateAvailableEmployee(Date start, Date finish, long employeeID);
	
	List<Visit> getAllEmployeeVisitsAfterDate(Date after, long employeeID);
	
	List<Visit> getClientVisits(long clientID);
	
	List<Visit> getClientVisitsBefore(long clientID, Date before);
	
	List<Visit> getClientVisitsAfter(long clientID, Date after);
	
	List<Visit> getEmployeeVisitsBefore(long employeeID, Date before);
	
	List<Visit> getEmployeeVisitsAfter(long employeeID, Date after);
	
	List<Visit> getEmployeeVisitsBetween(long employeeID, Date after, Date before);
	
	List<Visit> getEmployeeVisits(long employeeID);

	List<Visit> getEmployeesVisits(List<Employee> employees);

	List<Visit> getEmployeesVisitsBefore(List<Employee> employees, Date before);

	List<Visit> getEmployeesVisitsAfter(List<Employee> employees, Date after);

	List<Visit> getClientVisitToSentEmail();
	
	boolean hasAnyUpcomingVisits(long employeeID);
	
	List<Visit> getPlaceVisits(long placeID);

	List<Visit> getPlaceVisitsBefore(long placeID, Date before);

	List<Visit> getPlaceVisitsAfter(long placeID, Date after);

	boolean isDateAvailableClient(Date start, Date finish, long clientID);

	List<Visit> getVisitsWhichCoincidesWithDate(long employeeID, Date startDate, Date finishDate);
	
}
