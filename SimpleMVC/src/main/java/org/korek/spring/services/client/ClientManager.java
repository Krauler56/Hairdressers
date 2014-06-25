package org.korek.spring.services.client;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.korek.spring.controllers.models.ClientTO;
import org.korek.spring.controllers.models.ClientVisit;
import org.korek.spring.controllers.models.NewClient;
import org.korek.spring.controllers.models.RateVisit;
import org.korek.spring.repositories.dao.interfaces.IAuthDetailsDAO;
import org.korek.spring.repositories.dao.interfaces.IClientDAO;
import org.korek.spring.repositories.dao.interfaces.IVisitDAO;
import org.korek.spring.repositories.entities.Client;
import org.korek.spring.repositories.entities.Employee;
import org.korek.spring.repositories.entities.Visit;
import org.korek.spring.services.client.interfaces.IClientManager;
import org.korek.spring.services.common.interfaces.IPassEncoder;
import org.korek.spring.utils.CommonUtils;
import org.korek.spring.utils.Email;
import org.korek.spring.utils.SynchronizationProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;

@Service
public class ClientManager implements IClientManager
{

	@Autowired
	IClientDAO clientDAO;

	@Autowired
	IAuthDetailsDAO authDetailsDAO;
	
	@Autowired
	IVisitDAO visitDAO;
	
	@Autowired
	IPassEncoder passEncoder;
	
	@Override
	public List<ClientVisit> getClientVisits(long clientID)
	{
		List<Visit> visits = visitDAO.getClientVisits(clientID);

		return convertClientVisitsDB(visits);
	}

	@Override
	public List<ClientVisit> getPastClientVisits(long clientID)
	{
		List<Visit> clientVisitsBefore = visitDAO.getClientVisitsBefore(clientID, new Date());

		return convertClientVisitsDB(clientVisitsBefore);
	}

	@Override
	public List<ClientVisit> getUpcomingClientVisits(long clientID)
	{
		List<Visit> clientVisitsAfter = visitDAO.getClientVisitsAfter(clientID, new Date());

		return convertClientVisitsDB(clientVisitsAfter);
	}
	
	private List<ClientVisit> convertClientVisitsDB(List<Visit> visits)
	{
		List<ClientVisit> clientVisits = Lists.newArrayListWithCapacity(visits.size());
		
		for (Visit visit : visits)
		{
			clientVisits.add(new ClientVisit(visit));
		}
		
		return clientVisits;
	}

	@Override
	public void registerNewClient(NewClient newClient)
	{
		String password = newClient.getPassword();
		newClient.setPassword(passEncoder.encode(password));
		Client client = new Client(newClient);
		clientDAO.save(client);

	}

	@Override
	public ClientTO getClient(long clientID)
	{
		Client client = clientDAO.findOne(clientID);

		return new ClientTO(client);
	}

	@Override
	public boolean saveChanges(ClientTO clientOld, ClientTO client)
	{
		boolean changed = false;
		
		if (!clientOld.equals(client))
		{
			Client clientDB = clientDAO.load(clientOld.getId());
			
			clientDB.setEmail(client.getEmail());
			clientDB.setFirstName(client.getFirstName());
			clientDB.setLastName(client.getLastName());
			
			clientDAO.update(clientDB);
			changed = true;
		}
		
		return changed;
	}
	
	@Override
	public void cancelVisitClient(long visitID, long clientID)
	{
		Visit visit = visitDAO.load(visitID);
		if (visit.getClient().getId() == clientID)
		{
			if (visit.getStartDate().after(Calendar.getInstance()))
			{
				visitDAO.delete(visit);
			}
		}

	}
	
	@Override
	public void rateVisit(RateVisit rateVisit, long clientID, long visitID)
	{
		Visit visit = visitDAO.load(visitID);
		if (visit.getClient().getId() == clientID)
		{
			int rate = rateVisit.getRate();
			if ((rate > 0) && (rate < 6))
			{
				Employee employee = visit.getEmployee();

				try
				{
					Object synchoObj = SynchronizationProvider.getSynchoObj(Employee.class, employee.getId());
					synchronized (synchoObj)
					{
						employee.setRateNumber(employee.getRateNumber() + 1);
						employee.setRateSum(employee.getRateSum() + rate);
						visit.setServiceRate(rate);
						visitDAO.update(visit);
					}
				}
				finally
				{
					SynchronizationProvider.relaseSynchoObj(Employee.class, employee.getId());
				}
			}
		}

	}

	@Override
	public List<Email> getEmailsToSend()
	{

		List<Visit> clientVisitToSentEmail = visitDAO.getClientVisitToSentEmail();
		
		List<Email> emails = Lists.newArrayListWithCapacity(clientVisitToSentEmail.size());
		
		for (Visit visit : clientVisitToSentEmail)
		{
			visit.setNotificationSent(true);

			String recipent = visit.getClient().getEmail();
			String subject = prepareSubject(visit);
			String text = prepareText(visit);
			
			emails.add(new Email(recipent, text, subject));
		}
		
		return emails;
	}
	
	private String prepareText(Visit visit)
	{
		String dateAsString = CommonUtils.getDateAsString(visit.getStartDate().getTime(), "HH:mm  dd-MM-YYYY");
		
		return "Visit date: " + dateAsString + "\nEmployee: " + visit.getEmployee().getFirstName() + " " + visit.getEmployee().getLastName();
	}

	private String prepareSubject(Visit visit)
	{
		String name = visit.getEmployee().getHairdressers().getName();
		
		return "Visit at " + name;
	}
}
