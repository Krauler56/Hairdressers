package org.korek.spring.services.common;

import java.util.List;

import org.korek.spring.controllers.models.EmployeeTO;
import org.korek.spring.controllers.models.NewPlace;
import org.korek.spring.controllers.models.WorkPlace;
import org.korek.spring.repositories.dao.interfaces.IHairdressersDAO;
import org.korek.spring.repositories.entities.Employee;
import org.korek.spring.repositories.entities.Hairdressers;
import org.korek.spring.services.common.helpers.OpenTime;
import org.korek.spring.services.common.helpers.exceptions.IllegalNumberOfMaxEmployyesException;
import org.korek.spring.services.common.helpers.exceptions.PlaceCloseBeforeOpenException;
import org.korek.spring.services.common.helpers.exceptions.base.PlaceException;
import org.korek.spring.services.common.interfaces.IHairdressersManager;
import org.korek.spring.utils.CommonUtils;
import org.korek.spring.utils.SynchronizationProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;

@Service
public class HairdressersManager implements IHairdressersManager
{

	@Autowired
	IHairdressersDAO hairdressersDAO;

	@Override
	public List<WorkPlace> getAllWorkPlaceWithAvailableVacancy()
	{
		List<Hairdressers> hairdressersDB = hairdressersDAO.getAllWithAvailableVacancy();
		
		List<WorkPlace> allPlaces = Lists.newArrayListWithCapacity(hairdressersDB.size());

		for (Hairdressers hairdressers : hairdressersDB)
		{
			allPlaces.add(new WorkPlace(hairdressers.getId(), hairdressers.getName()));
		}

		return allPlaces;
	}

	@Override
	public List<WorkPlace> getAllWorkPlaceWithAtLeastOneEmployee()
	{
		List<Hairdressers> allhairdressersDB = hairdressersDAO.getAllPlacesWithAtLeastOneEmployee();

		List<WorkPlace> allWorkPlaces = Lists.newArrayListWithCapacity(allhairdressersDB.size());

		for (Hairdressers hairdressers : allhairdressersDB)
		{
			allWorkPlaces.add(new WorkPlace(hairdressers));
		}

		return allWorkPlaces;
	}

	@Override
	public List<WorkPlace> getAllWorkPlace()
	{

		List<Hairdressers> allhairdressersDB = hairdressersDAO.findAll();

		List<WorkPlace> allWorkPlaces = Lists.newArrayListWithCapacity(allhairdressersDB.size());

		for (Hairdressers hairdresser : allhairdressersDB)
		{
			allWorkPlaces.add(new WorkPlace(hairdresser));
		}

		return allWorkPlaces;
	}

	@Override
	public List<EmployeeTO> getAllEmployees(long placeID)
	{
		Hairdressers hairdressers = hairdressersDAO.load(placeID);
		List<Employee> employeesDB = hairdressers.getEmployees();

		List<EmployeeTO> employees = Lists.newArrayListWithCapacity(employeesDB.size());

		for (Employee employee : employeesDB)
		{
			employees.add(new EmployeeTO(employee));
		}

		return employees;
	}

	@Override
	public WorkPlace getWorkPlace(long id)
	{
		Hairdressers hairdressers = hairdressersDAO.findOne(id);

		return new WorkPlace(hairdressers);
	}

	@Override
	public WorkPlace getWorkPlaceDetails(long id)
	{
		Hairdressers hairdressers = hairdressersDAO.findOne(id);
		List<Employee> employees = hairdressers.getEmployees();

		WorkPlace place = new WorkPlace(hairdressers);

		float placeAvgRate = CommonUtils.calcPlaceAvgRate(employees);
		place.setRate(placeAvgRate);

		return place;
	}
	
	@Override
	public void addPlace(NewPlace newPlace) throws PlaceException
	{
		validNewPlace(newPlace);
		
		Hairdressers hairdressers = new Hairdressers(newPlace);

		hairdressersDAO.save(hairdressers);
	}

	@Override
	public void saveChanges(WorkPlace workPlaceOld, WorkPlace workPlace) throws PlaceException
	{
		if (CommonUtils.needSaveChangesToDB(workPlaceOld, workPlace))
		{
			long placeID = workPlaceOld.getId();
			Hairdressers hairdressersDB = hairdressersDAO.load(placeID);

			try
			{
				Object synchoObj = SynchronizationProvider.getSynchoObj(Hairdressers.class, placeID);

				synchronized (synchoObj)
				{
					validChanges(workPlace, hairdressersDB);
					applyChanges(hairdressersDB, workPlace);
					hairdressersDAO.update(hairdressersDB);
				}
			}
			finally
			{
				SynchronizationProvider.relaseSynchoObj(Hairdressers.class, placeID);
			}
		}
	}

	private void validChanges(WorkPlace workPlace, Hairdressers hairdressersDB) throws PlaceException
	{
		OpenTime openTime = new OpenTime(workPlace);
		validOpenTime(openTime);
		validNumberOfEmployees(workPlace, hairdressersDB);
		
	}

	private void validNumberOfEmployees(WorkPlace workPlace, Hairdressers hairdressersDB) throws IllegalNumberOfMaxEmployyesException
	{
		int newMaxEmployees = workPlace.getMaxEmployees();
		int currentEmployees = hairdressersDB.getCurrentEmployees();
		if(newMaxEmployees < currentEmployees)
		{
			throw new IllegalNumberOfMaxEmployyesException();
		}
		
	}

	private void applyChanges(Hairdressers hairdressersDB, WorkPlace workPlace)
	{
		hairdressersDB.getAddress().setBuildingNumber(workPlace.getBuildingNumber());
		hairdressersDB.getAddress().setCity(workPlace.getCity());
		hairdressersDB.getAddress().setDoorNumber(workPlace.getDoorNumber());
		hairdressersDB.getAddress().setPostCode(workPlace.getPostCode());
		hairdressersDB.getAddress().setStreet(workPlace.getStreet());
		hairdressersDB.setMaxEmployees(workPlace.getMaxEmployees());
		hairdressersDB.setName(workPlace.getName());
		hairdressersDB.setOpenFrom(workPlace.getOpenFrom());
		hairdressersDB.setOpenTo(workPlace.getOpenTo());
	}
	
	private void validNewPlace(NewPlace newPlace) throws PlaceException 
	{
		OpenTime openTime = new OpenTime(newPlace);
		validOpenTime(openTime);
		
	}

	private void validOpenTime(OpenTime openTime) throws PlaceCloseBeforeOpenException
	{
		int hourFrom = openTime.getHourFrom();
		int hourTo = openTime.getHourTo();
		
		boolean valid = true;
		
		if(hourFrom > hourTo)
		{
			valid = false;
		}
		else if(hourFrom == hourTo)
		{
			int minFrom = openTime.getMinFrom();
			int minTo = openTime.getMinTo();
			if(minFrom >= minTo)
			{
				valid = false;
			}
		}
		
		if(valid == false)
		{
			throw new PlaceCloseBeforeOpenException();
		}
		
	}
}
