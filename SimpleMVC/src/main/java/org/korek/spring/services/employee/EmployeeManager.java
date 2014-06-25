package org.korek.spring.services.employee;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.korek.spring.controllers.models.EmployeeTO;
import org.korek.spring.controllers.models.EmployeeVisit;
import org.korek.spring.controllers.models.NewEmployee;
import org.korek.spring.controllers.models.WorkPlace;
import org.korek.spring.repositories.dao.interfaces.IEmployeeDAO;
import org.korek.spring.repositories.dao.interfaces.IHairdressersDAO;
import org.korek.spring.repositories.dao.interfaces.IVisitDAO;
import org.korek.spring.repositories.entities.Employee;
import org.korek.spring.repositories.entities.Hairdressers;
import org.korek.spring.repositories.entities.Visit;
import org.korek.spring.services.common.interfaces.IPassEncoder;
import org.korek.spring.services.employee.helpers.EditEmployeeException;
import org.korek.spring.services.employee.helpers.HasUpcomingVisitsException;
import org.korek.spring.services.employee.helpers.NoVacancyAtWorkPlace;
import org.korek.spring.services.employee.interfaces.IEmployeeManager;
import org.korek.spring.utils.CommonUtils;
import org.korek.spring.utils.SynchronizationProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;

@Service
public class EmployeeManager implements IEmployeeManager
{

	@Autowired
	IEmployeeDAO employeeDAO;

	@Autowired
	IHairdressersDAO hairdressersDAO;

	@Autowired
	IVisitDAO visitDAO;
	
	@Autowired
	IPassEncoder passEncoder;
	
	@Override
	public boolean addEmployee(NewEmployee newEmployee) throws Exception
	{

		boolean added = false;

		String password = newEmployee.getPassword();
		newEmployee.setPassword(passEncoder.encode(password));
		Employee employee = new Employee(newEmployee);

		long idWorkPlace = newEmployee.getIdWorkPlace();

		if (idWorkPlace != 0) // set work place
		{
			boolean success = assingEmployeeToWorkPlace(idWorkPlace, employee);

			if (success)
			{
				employeeDAO.save(employee);
				added = true;
			}

		}
		else // no work place selected
		{
			employeeDAO.save(employee);
			added = true;
		}

		return added;
	}

	private boolean assingEmployeeToWorkPlace(long idWorkPlace, Employee employee)
	{
		boolean success = false;

		if (idWorkPlace == 0)
		{
			employee.setHairdressers(null);
			success = true;
		}
		else
		{
			try
			{
				Object lock = SynchronizationProvider.getSynchoObj(Hairdressers.class, idWorkPlace);
				synchronized (lock)
				{
					Hairdressers hairdressers = hairdressersDAO.findOne(idWorkPlace);

					int currentEmployees = hairdressers.getCurrentEmployees();
					int maxEmployees = hairdressers.getMaxEmployees();

					if (currentEmployees < maxEmployees)
					{
						hairdressers.setCurrentEmployees(currentEmployees + 1);
						employee.setHairdressers(hairdressers);
						success = true;
					}
				}
			}
			finally
			{
				SynchronizationProvider.relaseSynchoObj(Hairdressers.class, idWorkPlace);
			}
		}

		return success;
	}

	

	@Override
	public List<EmployeeTO> getAllEmployees()
	{

		List<Employee> all = employeeDAO.findAll();

		Lists.newArrayList();
		List<EmployeeTO> allEmployees = Lists.newArrayListWithCapacity(all.size());

		for (Employee employee : all)
		{
			allEmployees.add(new EmployeeTO(employee));
		}

		return allEmployees;
	}

	@Override
	public EmployeeTO getEmployeeTO(long employeeID)
	{
		Employee employee = employeeDAO.findOne(employeeID);

		return new EmployeeTO(employee);
	}

	@Override
	public boolean saveChanges(EmployeeTO employeeOld, EmployeeTO employee) throws EditEmployeeException
	{
		boolean changed = false;

		boolean needSaveChangesToDB = CommonUtils.needSaveChangesToDB(employeeOld, employee);

		if (needSaveChangesToDB)
		{
			long employeeID = employeeOld.getId();
			Employee employeeDB = employeeDAO.load(employeeID);

			long newPlaceID = employee.getPlaceID();
			long oldPlaceID = employeeOld.getPlaceID();

			if (newPlaceID != oldPlaceID)
			{
				changeWorkPlaceIfPossible(newPlaceID, employeeDB);

				if (oldPlaceID != 0)
				{
					try
					{
						Object lock = SynchronizationProvider.getSynchoObj(Hairdressers.class, oldPlaceID);
						synchronized (lock)
						{
							Hairdressers hairdressers = hairdressersDAO.findOne(oldPlaceID);
							int currentEmployees = hairdressers.getCurrentEmployees();
							hairdressers.setCurrentEmployees(currentEmployees - 1);
						}
					}
					finally
					{
						SynchronizationProvider.relaseSynchoObj(Hairdressers.class, oldPlaceID);
					}
				}

				employeeDB.setFirstName(employee.getFirstName());
				employeeDB.setLastName(employee.getLastName());
				changed = true;
			}
			else
			{
				employeeDB.setFirstName(employee.getFirstName());
				employeeDB.setLastName(employee.getLastName());
				changed = true;
			}
		}

		return changed;
	}

	private void changeWorkPlaceIfPossible(long newPlaceID, Employee employeeDB) throws EditEmployeeException
	{
		long employeeID = employeeDB.getId();
		try
		{
			Object synchoObj = SynchronizationProvider.getSynchoObj(Visit.class, employeeID);
			synchronized (synchoObj)
			{
				if (!visitDAO.hasAnyUpcomingVisits(employeeID))
				{
					if (!assingEmployeeToWorkPlace(newPlaceID, employeeDB))
					{
						throw new NoVacancyAtWorkPlace();
					}
				}
				else
				{
					throw new HasUpcomingVisitsException();
				}
			}

		}
		finally
		{
			SynchronizationProvider.relaseSynchoObj(Visit.class, employeeID);
		}

	}

	@Override
	public List<EmployeeVisit> getAllEmployeeVisits(long id)
	{
		List<Visit> employeeVisitsDB = visitDAO.getEmployeeVisits(id);

		return convertDBVisitsToEmployeeVisits(employeeVisitsDB);
	}

	@Override
	public List<EmployeeVisit> getUpcomingEmployeeVisits(long id)
	{
		List<Visit> employeeVisitsDB = visitDAO.getEmployeeVisitsAfter(id, new Date());

		return convertDBVisitsToEmployeeVisits(employeeVisitsDB);
	}

	@Override
	public List<EmployeeVisit> getPastEmployeeVisits(long id)
	{
		List<Visit> employeeVisitsDB = visitDAO.getEmployeeVisitsBefore(id, new Date());

		return convertDBVisitsToEmployeeVisits(employeeVisitsDB);
	}

	private List<EmployeeVisit> convertDBVisitsToEmployeeVisits(List<Visit> employeeVisitsDB)
	{
		List<EmployeeVisit> employeeVisits = Lists.newArrayListWithCapacity(employeeVisitsDB.size());
		for (Visit visit : employeeVisitsDB)
		{
			employeeVisits.add(new EmployeeVisit(visit));
		}

		return employeeVisits;

	}

	@Override
	public WorkPlace getEmployeesWorkPlace(long employeeID)
	{
		Hairdressers hairdresser = employeeDAO.load(employeeID).getHairdressers();

		return hairdresser != null ? new WorkPlace(hairdresser) : null;
	}

	@Override
	public void confirmPastVisit(long visitID, long loggedUserId)
	{
		Visit visit = visitDAO.load(visitID);
		if (visit.getEmployee().getId() == loggedUserId)
		{
			Calendar startDate = visit.getStartDate();
			if (startDate.before(Calendar.getInstance()))
			{
				visit.setTookPlace(true);
				visitDAO.update(visit);
			}
		}

	}

	@Override
	public void cancelVisitEmployee(long visitID, long employeeID)
	{
		Visit visit = visitDAO.load(visitID);
		if (visit.getEmployee().getId() == employeeID)
		{
			if (visit.getClient() == null)
			{
				if (visit.getStartDate().after(Calendar.getInstance()))
				{
					visitDAO.delete(visit);
				}
			}
		}

	}

	@Override
	public void cancelVisitAdmin(long visitID)
	{
		Visit visit = visitDAO.load(visitID);
		if (visit.getStartDate().after(Calendar.getInstance()))
		{
			visitDAO.delete(visit);
		}

	}

	@Override
	public void confirmPastVisitAdmin(long visitID)
	{
		Visit visit = visitDAO.load(visitID);
		Calendar startDate = visit.getStartDate();
		if (startDate.before(Calendar.getInstance()))
		{
			visit.setTookPlace(true);
			visitDAO.update(visit);
		}

	}

}
