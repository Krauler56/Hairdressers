package org.korek.spring.utils;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class EmailSender
{
	private static final String USERNAME = "michal.gawraluk@gmail.com";
	private static final String PASSWORD = "ziemniaczki";
	private static final Properties PROPS = prepareProperties();
	private static final Session SESSION = prepareSession(PROPS, USERNAME, PASSWORD);

	public EmailSender()
	{
		super();
	}

	public void sendEmail(String subject, String text, String recipent) throws MessagingException
	{
			Message message = new MimeMessage(SESSION);
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipent));
			message.setSubject(subject);
			message.setText(text);

			Transport.send(message);

	}

	public void sendEmail(Email email) throws MessagingException
	{
		sendEmail(email.getSubject(), email.getText(), email.getRecipent());
	}

	private static Properties prepareProperties()
	{
		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");

		return props;
	}

	private static Session prepareSession(Properties props, final String username, final String password)
	{
		Session session = Session.getInstance(props, new javax.mail.Authenticator()
		{
			protected PasswordAuthentication getPasswordAuthentication()
			{
				return new PasswordAuthentication(username, password);
			}
		});

		return session;

	}

}
