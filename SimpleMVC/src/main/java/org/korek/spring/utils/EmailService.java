package org.korek.spring.utils;

import java.util.List;

import javax.mail.MessagingException;

import org.korek.spring.services.client.interfaces.IClientManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@EnableScheduling
public class EmailService
{
	
	private static final Logger logger = LoggerFactory.getLogger(EmailService.class);
	
	@Autowired
	IClientManager clientManager;

	private EmailSender emailSender = new EmailSender();
	
	@Scheduled(fixedRate = 10 * 60 * 1000)
	public void run()
	{
		List<Email> emailsToSend = clientManager.getEmailsToSend();
		for (Email email : emailsToSend)
		{
			try
			{
				emailSender.sendEmail(email);
			}
			catch (MessagingException e)
			{
				logger.error("E-mail exception for: " + email.toString());
			}
		}
		
	}
}
