package org.korek.spring.repositories.dao;

import java.util.Date;
import java.util.List;

import org.korek.spring.repositories.dao.base.BaseDAO;
import org.korek.spring.repositories.dao.interfaces.IVisitDAO;
import org.korek.spring.repositories.entities.Employee;
import org.korek.spring.repositories.entities.Visit;
import org.korek.spring.utils.CommonUtils;
import org.springframework.stereotype.Repository;

@Repository
public class VisitDAO extends BaseDAO<Visit> implements IVisitDAO
{

	public VisitDAO()
	{
		super(Visit.class);
	}

	@Override
	public boolean isDateAvailableEmployee(Date start, Date finish, long employeeID)
	{
		
		 Object result = getCurrentSession().getNamedQuery(Visit.IS_DATE_AVAILABLE_EMPLOYEE)
				 .setTimestamp("startDate", start)
				 .setTimestamp("finishDate", finish)
				 .setLong("employeeID", employeeID)
				 .uniqueResult();
		
		
		return result == null ? true : false;
	}

	@Override
	public List<Visit> getAllEmployeeVisitsAfterDate(Date after, long employeeID)
	{
		@SuppressWarnings("unchecked")
		List<Visit> list = getCurrentSession().getNamedQuery(Visit.GET_ALL_EMPLOYEE_VISITS_AFTER_DATE)
				.setTimestamp("after", after)
				.setLong("employeeID", employeeID)
				.list();
				
		
		return list;
	}

	@Override
	public List<Visit> getClientVisits(long clientID)
	{
		@SuppressWarnings("unchecked")
		List<Visit> list = getCurrentSession().getNamedQuery(Visit.GET_CLIENT_VISITS)
				.setLong("clientID", clientID)
				.list();
		
		return list;
	}

	@Override
	public List<Visit> getClientVisitsBefore(long clientID, Date before)
	{
		@SuppressWarnings("unchecked")
		List<Visit> list = getCurrentSession().getNamedQuery(Visit.GET_CLIENT_VISITS_BEFORE)
				.setLong("clientID", clientID)
				.setTimestamp("before", before)
				.list();
		
		return list;
	}

	@Override
	public List<Visit> getClientVisitsAfter(long clientID, Date after)
	{
		@SuppressWarnings("unchecked")
		List<Visit> list = getCurrentSession().getNamedQuery(Visit.GET_CLIENT_VISITS_AFTER)
				.setLong("clientID", clientID)
				.setTimestamp("after", after)
				.list();
		
		return list;
	}

	@Override
	public List<Visit> getEmployeeVisitsBefore(long employeeID, Date before)
	{
		@SuppressWarnings("unchecked")
		List<Visit> list = getCurrentSession().getNamedQuery(Visit.GET_EMPLOYEE_VISITS_BEFORE)
				.setLong("employeeID", employeeID)
				.setTimestamp("before", before)
				.list();
		
		return list;
	}

	@Override
	public List<Visit> getEmployeeVisitsAfter(long employeeID, Date after)
	{
		@SuppressWarnings("unchecked")
		List<Visit> list = getCurrentSession().getNamedQuery(Visit.GET_EMPLOYEE_VISITS_AFTER)
				.setLong("employeeID", employeeID)
				.setTimestamp("after", after)
				.list();
		
		return list;
	}
	
	@Override
	public List<Visit> getEmployeeVisitsBetween(long employeeID, Date after, Date before)
	{
		@SuppressWarnings("unchecked")
		List<Visit> list = getCurrentSession().getNamedQuery(Visit.GET_EMPLOYEE_VISITS_BETWEEN)
				.setLong("employeeID", employeeID)
				.setTimestamp("after", after)
				.setTimestamp("before", before)
				.list();
		
		return list;
	}

	

	@Override
	public List<Visit> getEmployeeVisits(long employeeID)
	{
		@SuppressWarnings("unchecked")
		List<Visit> list = getCurrentSession().getNamedQuery(Visit.GET_EMPLOYEE_VISITS)
				.setLong("employeeID", employeeID)
				.list();
		
		return list;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Visit> getEmployeesVisits(List<Employee> employees)
	{
		List<Visit> list = null;
		
		List<Long> ids = CommonUtils.getIDs(employees);
		
		if(!ids.isEmpty())
		{
			list = getCurrentSession().getNamedQuery(Visit.GET_EMPLOYEES_VISITS)
				.setParameterList("ids", ids)
				.list();
		}
		
		return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Visit> getEmployeesVisitsBefore(List<Employee> employees, Date before)
	{
		List<Visit> list = null;
		
		List<Long> ids = CommonUtils.getIDs(employees);
		
		if(!ids.isEmpty())
		{
			list = getCurrentSession().getNamedQuery(Visit.GET_EMPLOYEES_VISITS_BEFORE)
				.setParameterList("ids", ids)
				.setTimestamp("before", before)
				.list();
		}
		
		return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Visit> getEmployeesVisitsAfter(List<Employee> employees, Date after)
	{
		List<Visit> list = null;
		
		List<Long> ids = CommonUtils.getIDs(employees);
		
		if(!ids.isEmpty())
		{
			list = getCurrentSession().getNamedQuery(Visit.GET_EMPLOYEES_VISITS_AFTER)
				.setParameterList("ids", ids)
				.setTimestamp("after", after)
				.list();
		}
		
		return list;
	}

	@Override
	public List<Visit> getClientVisitToSentEmail()
	{
		@SuppressWarnings("unchecked")
		List<Visit> list = getCurrentSession().getNamedQuery(Visit.GET_CLIENT_VISIT_TO_SENT_EMAIL)
			.setTimestamp("date", CommonUtils.getTommorow())
			.setTimestamp("now", new Date())
			.list();
		
		return list;
	}

	@Override
	public boolean hasAnyUpcomingVisits(long employeeID)
	{
		 Object result = getCurrentSession().getNamedQuery(Visit.HAS_ANY_UPCOMING_VISITS)
				.setLong("employeeID", employeeID)
				.setTimestamp("date", new Date())
				.uniqueResult();
		 
		return result != null ? true : false;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Visit> getPlaceVisits(long placeID)
	{
		 List<Visit> list = getCurrentSession().getNamedQuery(Visit.GET_PLACE_VISITS)
				.setLong("placeID", placeID)
				.list();
		
		return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Visit> getPlaceVisitsBefore(long placeID, Date before)
	{
		 List<Visit> list = getCurrentSession().getNamedQuery(Visit.GET_PLACE_VISITS_BEFORE)
					.setLong("placeID", placeID)
					.setTimestamp("before", before)
					.list();
			
		return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Visit> getPlaceVisitsAfter(long placeID, Date after)
	{
		 List<Visit> list = getCurrentSession().getNamedQuery(Visit.GET_PLACE_VISITS_AFTER)
					.setLong("placeID", placeID)
					.setTimestamp("after", after)
					.list();
			
		return list;
	}

	@Override
	public boolean isDateAvailableClient(Date start, Date finish, long clientID)
	{
		 Object result = getCurrentSession().getNamedQuery(Visit.IS_DATE_AVAILABLE_CLIENT)
					.setLong("clientID", clientID)
					.setTimestamp("startDate", start)
					.setTimestamp("finishDate", finish)
					.uniqueResult();
			 
		return result == null ? true : false;
	}

	@Override
	public List<Visit> getVisitsWhichCoincidesWithDate(long employeeID, Date startDate, Date finishDate)
	{
		@SuppressWarnings("unchecked")
		List<Visit> result = getCurrentSession().getNamedQuery(Visit.GET_VISITS_WHICH_COINCIDES_WITHDATE)
				.setTimestamp("startDate", startDate)
				.setTimestamp("finishDate", finishDate)
				.setLong("employeeID", employeeID)
				.list();
		
		return result;
	}
}
