package org.korek.spring.web.config;

import java.util.TimeZone;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.springframework.context.annotation.Configuration;

public class DefaultTimeZone implements ServletContextListener
{

	@Override
	public void contextInitialized(ServletContextEvent sce)
	{
		TimeZone timeZone = TimeZone.getTimeZone("Europe/London");
		TimeZone.setDefault(timeZone);
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce)
	{
		
	}

}
