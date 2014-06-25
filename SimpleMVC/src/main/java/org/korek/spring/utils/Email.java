package org.korek.spring.utils;

public class Email
{
	private final String recipent;
	private final String text;
	private final String subject;

	public Email(String recipient, String text, String subject)
	{
		super();
		this.recipent = recipient;
		this.text = text;
		this.subject = subject;
	}

	public String getRecipent()
	{
		return recipent;
	}

	public String getText()
	{
		return text;
	}

	public String getSubject()
	{
		return subject;
	}

	@Override
	public String toString()
	{
		return "Email [recipent=" + recipent + ", text=" + text + ", subject=" + subject + "]";
	}
	
	
	
	
}
