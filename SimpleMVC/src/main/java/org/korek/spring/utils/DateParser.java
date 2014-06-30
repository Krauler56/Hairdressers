package org.korek.spring.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DateParser
{

	private final SimpleDateFormat dateFormater;
	
	public DateParser(String pattern, Locale locale, TimeZone timezone)
	{
		super();
		dateFormater = new SimpleDateFormat(pattern, locale);
		dateFormater.setTimeZone(timezone);
	}

	public DateParser(String pattern)
	{
		this(pattern, new Locale("en", "GB"), TimeZone.getTimeZone("Europe/London"));
	}

	public Date parse(String s) throws ParseException
	{
		return dateFormater.parse(s);
	}
	
	public String format(Date date)
	{
		return dateFormater.format(date);
	}
	
}
