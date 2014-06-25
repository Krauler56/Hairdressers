package org.korek.spring.services.common;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.javatuples.Pair;
import org.korek.spring.controllers.models.EmployeeVisit;
import org.korek.spring.controllers.models.NewVisit;
import org.korek.spring.repositories.dao.interfaces.IClientDAO;
import org.korek.spring.repositories.dao.interfaces.IEmployeeDAO;
import org.korek.spring.repositories.dao.interfaces.IHairdressersDAO;
import org.korek.spring.repositories.dao.interfaces.IVisitDAO;
import org.korek.spring.repositories.entities.Employee;
import org.korek.spring.repositories.entities.Hairdressers;
import org.korek.spring.repositories.entities.Visit;
import org.korek.spring.services.client.helpers.HairType;
import org.korek.spring.services.common.helpers.OpenTime;
import org.korek.spring.services.common.helpers.SearchingForVisitResult;
import org.korek.spring.services.common.helpers.SuggestedDate;
import org.korek.spring.services.common.helpers.exceptions.ClientHasVisitException;
import org.korek.spring.services.common.helpers.exceptions.NoEmployeeAtWorkPlace;
import org.korek.spring.services.common.helpers.exceptions.StartDateBeforeNowException;
import org.korek.spring.services.common.helpers.exceptions.base.NewVisitException;
import org.korek.spring.services.common.interfaces.IVisitManager;
import org.korek.spring.utils.CommonUtils;
import org.korek.spring.utils.SynchronizationProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;

@Service
public class VisitManager implements IVisitManager
{

	private static final Logger logger = LoggerFactory.getLogger(VisitManager.class);

	private static final SimpleDateFormat DATE_FORMATER = new SimpleDateFormat("yyyy-MM-ddHH:mm");

	@Autowired
	IVisitDAO visitDAO;

	@Autowired
	IClientDAO clientDAO;

	@Autowired
	IEmployeeDAO employeeDAO;

	@Autowired
	IHairdressersDAO hairdressersDAO;

	@Override
	public SearchingForVisitResult checkForVisit(NewVisit newVisit) throws NewVisitException
	{

		/* prepare data */
		long employeeID = newVisit.getEmployeeID();
		long clientID = newVisit.getClientID();
		long workPlaceID = newVisit.getWorkPlaceID();
		HairType hairType = HairType.valueOf(newVisit.getHairType());
		String date = newVisit.getDate();
		String startTime = newVisit.getStartTime();

		Date startDate = convertDate(date, startTime);
		startDateOk(startDate);
		Date finishDate = calcFinishDate(startDate, hairType);
		/* prepare data */
		
		clientOkForNewVisit(clientID, startDate, finishDate);

		SearchingForVisitResult searchingForVisitResult = processNewVisit(employeeID, workPlaceID, startDate, finishDate);

		if (searchingForVisitResult.isDateAvailable() == false)
		{
			Hairdressers hairdressers = hairdressersDAO.load(workPlaceID);
			OpenTime openTime = new OpenTime(hairdressers);
			List<SuggestedDate> dates = searchForNearestAvaiableDates(startDate, finishDate, clientID, employeeID, workPlaceID, openTime);
			long id = 1;
			for (SuggestedDate suggestedDate : dates)
			{
				suggestedDate.setId(id++);
			}
			
			searchingForVisitResult.setSuggestedDates(dates);
		}

		return searchingForVisitResult;

	}

	private void startDateOk(Date startDate) throws StartDateBeforeNowException
	{
		Date now = new Date();
		
		if(startDate == null || startDate.before(now))
		{
			throw new StartDateBeforeNowException();
		}
		
	}

	private SearchingForVisitResult processNewVisit(long employeeID, long workPlaceID, Date startDate, Date finishDate) throws NoEmployeeAtWorkPlace
	{
		SearchingForVisitResult searchingForVisitResult = new SearchingForVisitResult(startDate, finishDate, workPlaceID);

		if (employeeID == 0) // check all employees from place
		{
			Hairdressers hairdressers = hairdressersDAO.load(workPlaceID);
			List<Employee> employees = hairdressers.getEmployees();
			if (!employees.isEmpty())
			{
				for (Employee employee : employees)
				{
					long id = employee.getId();
					if (visitDAO.isDateAvailableEmployee(startDate, finishDate, id))
					{
						searchingForVisitResult.setEmployeeID(id);
						searchingForVisitResult.setDateAvailable(true);
						break;
					}
				}
			}
			else
			{
				throw new NoEmployeeAtWorkPlace();
			}
		}
		else // check concrete employee
		{
			if (visitDAO.isDateAvailableEmployee(startDate, finishDate, employeeID))
			{
				searchingForVisitResult.setEmployeeID(employeeID);
				searchingForVisitResult.setDateAvailable(true);
			}
		}

		return searchingForVisitResult;
	}

	private Date calcFinishDate(Date startDate, HairType hairType)
	{
		Calendar finishDateCal = new GregorianCalendar();
		finishDateCal.setTime(startDate);
		finishDateCal.add(Calendar.MINUTE, hairType.getDuration());

		return finishDateCal.getTime();
	}

	private void clientOkForNewVisit(long clientID, Date start, Date finish) throws ClientHasVisitException
	{
		if (clientID != 0)
		{
			boolean hasVisit = visitDAO.isDateAvailableClient(start, finish, clientID);
			if (hasVisit == false)
			{
				throw new ClientHasVisitException();
			}
		}
	}

	private List<SuggestedDate> searchForNearestAvaiableDates(Date startDate, Date finishDate, long clientID, long employeeID, long workPlaceID, OpenTime openTime)
	{
		List<SuggestedDate> suggestedDates = Lists.newArrayList();

		if (employeeID == 0)
		{
			checkPlaceForNearestAvaiableDates(suggestedDates, workPlaceID, startDate, finishDate, openTime);
		}
		else
		{
			Pair<SuggestedDate,SuggestedDate> dates = checkEmployeeForNearestAvaiableDates(employeeID, startDate, finishDate, openTime);
			addSuggestedDateToList(suggestedDates, dates);
		}

		return suggestedDates;
	}

	private void addSuggestedDateToList(List<SuggestedDate> suggestedDates, Pair<SuggestedDate, SuggestedDate> dates)
	{
		SuggestedDate after = dates.getValue1();
		if(after != null && !suggestedDates.contains(after))
		{
			suggestedDates.add(after);
		}

		SuggestedDate before = dates.getValue0();
		if(before != null && !suggestedDates.contains(before))
		{
			suggestedDates.add(before);
		}
	}

	private List<SuggestedDate> checkPlaceForNearestAvaiableDates(List<SuggestedDate> suggestedDates, long workPlaceID, Date startDate, Date finishDate, OpenTime openTime)
	{
		Hairdressers hairdressers = hairdressersDAO.load(workPlaceID);
		List<Employee> employees = hairdressers.getEmployees();
		for (Employee employee : employees)
		{
			Pair<SuggestedDate,SuggestedDate> dates = checkEmployeeForNearestAvaiableDates(employee.getId(), startDate, finishDate, openTime);
			addSuggestedDateToList(suggestedDates, dates);
		}

		return suggestedDates;
	}

	private Pair<SuggestedDate, SuggestedDate> checkEmployeeForNearestAvaiableDates(long employeeID, final Date startDate, final Date finishDate, OpenTime openTime)
	{
		final int duration = CommonUtils.diffInDates(finishDate, startDate);

		SuggestedDate after = lookForDateAfter(duration, startDate, finishDate, employeeID, openTime);
		SuggestedDate before = lookForDateBefore(duration, startDate, finishDate, employeeID, openTime);
		
		return new Pair<SuggestedDate, SuggestedDate>(before, after);

	}

	private SuggestedDate lookForDateBefore(int duration, Date startDate, Date finishDate, long employeeID, OpenTime openTime)
	{
		final Calendar now = Calendar.getInstance();

		Calendar searchStart = new GregorianCalendar();
		searchStart.setTime(startDate);

		Calendar searchEnd = new GregorianCalendar();
		searchEnd.setTime(finishDate);

		Date finalStartDate = null;
		Date finalFinishDate = null;
		boolean keepLooking = true;
		while (keepLooking == true)
		{
			List<Visit> coincides = visitDAO.getVisitsWhichCoincidesWithDate(employeeID, searchStart.getTime(), searchEnd.getTime());

			if (!coincides.isEmpty())
			{
				Visit visit = coincides.get(0);
				final Calendar startDate2 = visit.getStartDate();

				searchEnd = startDate2;
				searchStart.setTime(searchEnd.getTime());
				searchStart.add(Calendar.MINUTE, -duration);

				boolean afterOpen = isStartDateAfterOpen(searchStart, openTime);

				if (afterOpen == false) // try day earlier
				{
					searchStart.add(Calendar.DAY_OF_MONTH, -1);
					searchStart.set(Calendar.HOUR_OF_DAY, openTime.getHourTo());
					searchStart.set(Calendar.MINUTE, openTime.getMinTo());
					searchEnd.setTime(searchStart.getTime());
					searchEnd.add(Calendar.MINUTE, duration);
				}

				if (searchStart.after(now))
				{
					boolean dateAvailableEmployee = visitDAO.isDateAvailableEmployee(searchStart.getTime(), searchEnd.getTime(), employeeID);
					if (dateAvailableEmployee)
					{
						finalStartDate = searchStart.getTime();
						finalFinishDate = searchEnd.getTime();
						keepLooking = false;
					}
				}
				else
				{
					keepLooking = false;
				}

			}
			else
			{
				keepLooking = false;
				boolean dateAvailableEmployee = visitDAO.isDateAvailableEmployee(searchStart.getTime(), searchEnd.getTime(), employeeID);
				if (dateAvailableEmployee)
				{
					finalStartDate = searchStart.getTime();
					finalFinishDate = searchEnd.getTime();
				}
			}
		}

		if (finalStartDate == null)
		{
			return null;
		}

		return new SuggestedDate(employeeID, finalStartDate, finalFinishDate);
	}

	

	private SuggestedDate lookForDateAfter(int duration, Date startDate, Date finishDate, long employeeID, OpenTime openTime)
	{
		Calendar searchStart = new GregorianCalendar();
		searchStart.setTime(startDate);

		Calendar searchEnd = new GregorianCalendar();
		searchEnd.setTime(finishDate);

		Date finalStartDate = null;
		Date finalFinishDate = null;
		boolean keepLooking = true;
		while (keepLooking == true)
		{
			List<Visit> coincides = visitDAO.getVisitsWhichCoincidesWithDate(employeeID, searchStart.getTime(), searchEnd.getTime());

			if (!coincides.isEmpty())
			{
				Visit visit = coincides.get(0);
				final Calendar finishDate2 = visit.getFinishDate();

				boolean beforeClose = isFinishDateBeforeClose(finishDate2, openTime);

				searchStart = finishDate2;

				if (beforeClose == true) // try after last visit
				{
					searchEnd.setTime(searchStart.getTime());
					searchEnd.add(Calendar.MINUTE, duration);
				}
				else // try next day
				{
					searchStart.add(Calendar.DAY_OF_MONTH, 1);
					searchStart.set(Calendar.HOUR_OF_DAY, openTime.getHourFrom());
					searchStart.set(Calendar.MINUTE, openTime.getMinFrom());
					searchEnd.setTime(searchStart.getTime());
					searchEnd.add(Calendar.MINUTE, duration);
				}

				boolean dateAvailableEmployee = visitDAO.isDateAvailableEmployee(searchStart.getTime(), searchEnd.getTime(), employeeID);
				if (dateAvailableEmployee)
				{
					finalStartDate = searchStart.getTime();
					finalFinishDate = searchEnd.getTime();
					keepLooking = false;
				}
			}
			else
			{
				keepLooking = false;
				boolean dateAvailableEmployee = visitDAO.isDateAvailableEmployee(searchStart.getTime(), searchEnd.getTime(), employeeID);
				if (dateAvailableEmployee)
				{
					finalStartDate = searchStart.getTime();
					finalFinishDate = searchEnd.getTime();
				}
			}
		}
		
		if (finalStartDate == null)
		{
			return null;
		}
		
		return new SuggestedDate(employeeID, finalStartDate, finalFinishDate);
	}

	private boolean isFinishDateBeforeClose(Calendar finishDate, OpenTime openTime)
	{

		int hourTo = openTime.getHourTo();
		int hour = finishDate.get(Calendar.HOUR_OF_DAY);

		if (hour < hourTo)
		{
			return true;
		}
		else if (hour > hourTo)
		{
			return false;
		}
		else
		{
			int minTo = openTime.getMinTo();
			int minute = finishDate.get(Calendar.MINUTE);
			if (minute < minTo)
			{
				return true;
			}
			else
			{
				return false;
			}
		}
	}
	
	private boolean isStartDateAfterOpen(Calendar startDate2, OpenTime openTime)
	{
		int hourFrom = openTime.getHourFrom();
		int hour = startDate2.get(Calendar.HOUR_OF_DAY);
		
		if(hour > hourFrom)
		{
			return true;
		}
		else if(hour < hourFrom)
		{
			return false;
		}
		else
		{
			int minFrom = openTime.getMinFrom();
			int minute = startDate2.get(Calendar.MINUTE);
			if(minute >= minFrom)
			{
				return true;
			}
			else
			{
				return false;
			}
		}
	}

	private Date convertDate(String date, String startTime)
	{
		Date startDate = null;
		try
		{
			startDate = DATE_FORMATER.parse(date + startTime);
		}
		catch (ParseException e)
		{
			logger.error("SimpleDateFormat error for " + date + " " + startTime, e);
		}

		return startDate;
	}

	@Override
	public boolean bookVisit(NewVisit newVisit, Long suggestedDateID)
	{
		boolean booked = false;

		if (newVisit != null)
		{
			SearchingForVisitResult searchingForVisitResult = newVisit.getSearchingForVisitResult();

			if (searchingForVisitResult != null)
			{
				boolean dateAvailable = searchingForVisitResult.isDateAvailable();

				Date startDate = null;
				Date finishDate = null;
				long employeeID = 0;

				if (dateAvailable == true)
				{
					startDate = searchingForVisitResult.getStartDate();
					finishDate = searchingForVisitResult.getFinishDate();
					employeeID = searchingForVisitResult.getEmployeeID();
				}
				else
				{
					List<SuggestedDate> suggestedDates = searchingForVisitResult.getSuggestedDates();
					for (SuggestedDate suggestedDate : suggestedDates)
					{
						if (suggestedDateID.longValue() == suggestedDate.getId())
						{
							startDate = suggestedDate.getStartDate();
							finishDate = suggestedDate.getFinishDate();
							employeeID = suggestedDate.getEmployeeID();
							break;
						}
					}
				}

				booked = bookVisit(newVisit.getClientID(), employeeID, searchingForVisitResult.getPlaceID(), startDate, finishDate);

			}
		}

		return booked;
	}

	private boolean bookVisit(long clientID, long employeeID, long workPlaceID, Date startDate, Date finishDate)
	{

		boolean booked = false;

		if (employeeID != 0 && workPlaceID != 0 && startDate != null && finishDate != null)
		{
			if (clientOkForNewVisitSilent(clientID, startDate, finishDate))
			{
				try
				{
					Object synchoObj = SynchronizationProvider.getSynchoObj(Visit.class, employeeID);
					synchronized (synchoObj)
					{
						boolean dateAvailable = visitDAO.isDateAvailableEmployee(startDate, finishDate, employeeID);

						if (dateAvailable == true && workAtPlace(employeeID, workPlaceID))
						{
							createNewVisit(clientID, employeeID, workPlaceID, startDate, finishDate);
							booked = true;
						}
					}
				}
				finally
				{
					SynchronizationProvider.relaseSynchoObj(Visit.class, employeeID);
				}

			}
		}

		return booked;

	}

	private boolean clientOkForNewVisitSilent(long clientID, Date startDate, Date finishDate)
	{
		try
		{
			clientOkForNewVisit(clientID, startDate, finishDate);
		}
		catch (ClientHasVisitException e)
		{
			return false;
		}

		return true;
	}

	private void createNewVisit(long clientID, long employeeID, long workPlaceID, Date startDate, Date finishDate)
	{
		Visit visit = new Visit();
		visit.setClient(clientID != 0 ? clientDAO.load(clientID) : null);
		visit.setEmployee(employeeDAO.load(employeeID));
		visit.setHairdressers(hairdressersDAO.load(workPlaceID));
		Calendar start = Calendar.getInstance();
		start.setTime(startDate);
		visit.setStartDate(start);
		Calendar finish = Calendar.getInstance();
		finish.setTime(finishDate);
		visit.setFinishDate(finish);
		visitDAO.save(visit);
	}

	private boolean workAtPlace(long employeeID, long workPlaceID)
	{
		Employee employee = employeeDAO.load(employeeID);
		Hairdressers hairdressers = employee.getHairdressers();

		if (hairdressers != null && hairdressers.getId() == workPlaceID)
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	@Override
	public List<EmployeeVisit> getAllPlaceVisit(long placeID)
	{

		List<Visit> placeVisits = visitDAO.getPlaceVisits(placeID);

		return convertPlaceVisitsDB(placeVisits);
	}

	@Override
	public List<EmployeeVisit> getPastPlaceVisit(long placeID)
	{
		List<Visit> placeVisitsBefore = visitDAO.getPlaceVisitsBefore(placeID, new Date());

		return convertPlaceVisitsDB(placeVisitsBefore);
	}

	@Override
	public List<EmployeeVisit> getUpcomingPlaceVisit(long placeID)
	{
		List<Visit> placeVisitsAfter = visitDAO.getPlaceVisitsAfter(placeID, new Date());

		return convertPlaceVisitsDB(placeVisitsAfter);
	}
	
	private List<EmployeeVisit> convertPlaceVisitsDB(List<Visit> placeVisitsBefore)
	{
		List<EmployeeVisit> employeesVisits = Lists.newArrayListWithCapacity(placeVisitsBefore.size());

		for (Visit visit : placeVisitsBefore)
		{
			employeesVisits.add(new EmployeeVisit(visit));
		}
		
		return employeesVisits;
	}

}
