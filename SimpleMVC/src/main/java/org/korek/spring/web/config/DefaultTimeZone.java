package org.korek.spring.web.config;

import java.util.Locale;
import java.util.TimeZone;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class DefaultTimeZone implements ServletContextListener
{

	@Override
	public void contextInitialized(ServletContextEvent sce)
	{
		TimeZone timeZone = TimeZone.getTimeZone("Europe/London");
		TimeZone.setDefault(timeZone);
		
		Locale.setDefault(new Locale("en", "GB"));
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce)
	{
		
	}

}
