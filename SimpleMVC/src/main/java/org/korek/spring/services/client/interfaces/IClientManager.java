package org.korek.spring.services.client.interfaces;

import java.util.List;

import org.korek.spring.controllers.models.ClientTO;
import org.korek.spring.controllers.models.ClientVisit;
import org.korek.spring.controllers.models.NewClient;
import org.korek.spring.controllers.models.RateVisit;
import org.korek.spring.utils.Email;
import org.springframework.transaction.annotation.Transactional;

public interface IClientManager
{
	
	@Transactional
	List<ClientVisit> getClientVisits(long clientID); 
	
	@Transactional
	List<ClientVisit> getPastClientVisits(long clientID); 
	
	@Transactional
	List<ClientVisit> getUpcomingClientVisits(long clientID); 
	
	@Transactional
	void registerNewClient(NewClient newClient);

	@Transactional
	ClientTO getClient(long clientID);

	@Transactional
	boolean saveChanges(ClientTO clientOld, ClientTO client);
	
	@Transactional
	void cancelVisitClient(long visitID, long clientID);
	
	@Transactional
	void rateVisit(RateVisit rateVisit, long clientID, long visitID);
	
	@Transactional
	List<Email> getEmailsToSend();
}
