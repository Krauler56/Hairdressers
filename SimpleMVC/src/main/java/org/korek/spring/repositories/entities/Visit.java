package org.korek.spring.repositories.entities;

import java.util.Calendar;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;

@NamedQueries({
	@NamedQuery(
			name = Visit.IS_DATE_AVAILABLE_EMPLOYEE,
			query = "select 1 from Visit where exists(from Visit where employee.id = :employeeID "
					+ "and ((startDate >= :startDate and startDate < :finishDate) or (startDate <= :startDate and finishDate > :startDate) ) )"
	),
	@NamedQuery(
			name = Visit.GET_VISITS_WHICH_COINCIDES_WITHDATE,
			query = "from Visit where employee.id = :employeeID "
					+ "and ((startDate >= :startDate and startDate < :finishDate) or (startDate <= :startDate and finishDate > :startDate) ) order by startDate DESC"
	),
	@NamedQuery(
			name = Visit.GET_ALL_EMPLOYEE_VISITS_AFTER_DATE,
			query = "from Visit where employee.id = :employeeID and finishDate >= :after order by finishDate"
	),
	@NamedQuery(
			name = Visit.GET_CLIENT_VISITS,
			query = "from Visit where client.id = :clientID order by startDate"
	),
	@NamedQuery(
			name = Visit.GET_CLIENT_VISITS_BEFORE,
			query = "from Visit where client.id = :clientID and startDate <= :before order by startDate"
	),
	@NamedQuery(
			name = Visit.GET_CLIENT_VISITS_AFTER,
			query = "from Visit where client.id = :clientID and startDate >= :after  order by startDate"
	),
	@NamedQuery(
			name = Visit.GET_EMPLOYEE_VISITS_BEFORE,
			query = "from Visit where employee.id = :employeeID and startDate <= :before  order by startDate"
	),
	@NamedQuery(
			name = Visit.GET_EMPLOYEE_VISITS_AFTER,
			query = "from Visit where employee.id = :employeeID and startDate >= :after  order by startDate"
	),
	@NamedQuery(
			name = Visit.GET_EMPLOYEE_VISITS_BETWEEN,
			query = "from Visit where employee.id = :employeeID and startDate >= :after and startDate <= :before  order by startDate"
	),
	@NamedQuery(
			name = Visit.GET_EMPLOYEES_VISITS,
			query = "from Visit where employee.id IN (:ids) order by startDate"
	),
	@NamedQuery(
			name = Visit.GET_EMPLOYEE_VISITS,
			query = "from Visit where employee.id = :employeeID order by startDate"
	),
	@NamedQuery(
			name = Visit.GET_EMPLOYEES_VISITS_BEFORE,
			query = "from Visit where employee.id IN (:ids) and startDate <= :before order by startDate"
	),
	@NamedQuery(
			name = Visit.GET_EMPLOYEES_VISITS_AFTER,
			query = "from Visit where employee.id IN (:ids) and startDate >= :after  order by startDate"
	),
	@NamedQuery(
			name = Visit.GET_CLIENT_VISIT_TO_SENT_EMAIL,
			query = "from Visit where client.email <> '' and startDate <= :date and notificationSent = false" 
	),
	@NamedQuery(
			name = Visit.HAS_ANY_UPCOMING_VISITS,
			query = "select 1 from Visit where exists(from Visit where employee.id = :employeeID and startDate >= :date)" 
	),
	@NamedQuery(
			name = Visit.GET_PLACE_VISITS,
			query = "from Visit where hairdressers.id = :placeID order by startDate"
	),
	@NamedQuery(
			name = Visit.GET_PLACE_VISITS_BEFORE,
			query = "from Visit where hairdressers.id = :placeID and startDate <= :before  order by startDate"
	),
	@NamedQuery(
			name = Visit.GET_PLACE_VISITS_AFTER,
			query = "from Visit where hairdressers.id = :placeID and startDate >= :after  order by startDate"
	)
	,
	@NamedQuery(
			name = Visit.IS_DATE_AVAILABLE_CLIENT,
			query = "select 1 from Visit where exists(from Visit where client.id = :clientID "
					+ "and ((startDate >= :startDate and startDate < :finishDate) or (startDate <= :startDate and finishDate > :startDate) ) )"
	)
	
})

@Entity
public class Visit
{

	public static final String IS_DATE_AVAILABLE_EMPLOYEE =  "IS_DATE_AVAILABLE_EMPLOYEE";

	public static final String GET_ALL_EMPLOYEE_VISITS_AFTER_DATE = "GET_ALL_EMPLOYEE_VISITS_AFTER_DATE";

	public static final String GET_CLIENT_VISITS = "GET_CLIENT_VISITS";
	
	public static final String GET_CLIENT_VISITS_BEFORE = "GET_CLIENT_VISITS_BEFORE";
	
	public static final String GET_CLIENT_VISITS_AFTER = "GET_CLIENT_VISITS_AFTER";
	
	public static final String GET_EMPLOYEE_VISITS_BEFORE = "GET_EMPLOYEE_VISITS_BEFORE";
	
	public static final String GET_EMPLOYEE_VISITS_AFTER = "GET_EMPLOYEE_VISITS_AFTER";

	public static final String GET_EMPLOYEES_VISITS = "GET_EMPLOYEES_VISITS";

	public static final String GET_EMPLOYEE_VISITS = "GET_EMPLOYEE_VISITS";

	public static final String GET_EMPLOYEES_VISITS_BEFORE = "GET_EMPLOYEES_VISITS_BEFORE";
	
	public static final String GET_EMPLOYEES_VISITS_AFTER = "GET_EMPLOYEES_VISITS_AFTER";

	public static final String GET_CLIENT_VISIT_TO_SENT_EMAIL = "GET_CLIENT_VISIT_TO_SENT_EMAIL";
	
	public static final String HAS_ANY_UPCOMING_VISITS = "HAS_ANY_UPCOMING_VISITS";

	public static final String GET_PLACE_VISITS = "GET_PLACE_VISITS";
	
	public static final String GET_PLACE_VISITS_BEFORE = "GET_PLACE_VISITS_BEFORE";
	
	public static final String GET_PLACE_VISITS_AFTER = "GET_PLACE_VISITS_AFTER";

	public static final String IS_DATE_AVAILABLE_CLIENT = "IS_DATE_AVAILABLE_CLIENT";

	public static final String GET_EMPLOYEE_VISITS_BETWEEN = "GET_EMPLOYEE_VISITS_BETWEEN";

	public static final String GET_VISITS_WHICH_COINCIDES_WITHDATE = "GET_VISITS_WHICH_COINCIDES_WITHDATE";
	
	@Id
	@GeneratedValue
	private long id;

	@OneToOne
	private Employee employee;

	@OneToOne
	private Client client;
	
	@OneToOne
	private Hairdressers hairdressers;

	private Calendar startDate;

	private Calendar finishDate;

	private int serviceRate;
	
	private boolean tookPlace;
	
	private boolean notificationSent;

	public Visit()
	{
		super();
		this.tookPlace = false;
		this.notificationSent = false;
	}

	public long getId()
	{
		return id;
	}

	public void setId(long id)
	{
		this.id = id;
	}


	public Employee getEmployee()
	{
		return employee;
	}

	public void setEmployee(Employee employee)
	{
		this.employee = employee;
	}

	public Client getClient()
	{
		return client;
	}

	public void setClient(Client client)
	{
		this.client = client;
	}

	public Calendar getStartDate()
	{
		return startDate;
	}

	public void setStartDate(Calendar startDate)
	{
		this.startDate = startDate;
	}

	public Calendar getFinishDate()
	{
		return finishDate;
	}

	public void setFinishDate(Calendar finishDate)
	{
		this.finishDate = finishDate;
	}

	public int getServiceRate()
	{
		return serviceRate;
	}

	public void setServiceRate(int serviceRate)
	{
		this.serviceRate = serviceRate;
	}

	public boolean isTookPlace()
	{
		return tookPlace;
	}

	public void setTookPlace(boolean tookPlace)
	{
		this.tookPlace = tookPlace;
	}

	public boolean isNotificationSent()
	{
		return notificationSent;
	}

	public void setNotificationSent(boolean notificationSent)
	{
		this.notificationSent = notificationSent;
	}

	public Hairdressers getHairdressers()
	{
		return hairdressers;
	}

	public void setHairdressers(Hairdressers hairdressers)
	{
		this.hairdressers = hairdressers;
	}
	
}
