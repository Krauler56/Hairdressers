package org.korek.spring.utils;

import java.security.Principal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.korek.spring.controllers.models.WorkPlace;
import org.korek.spring.repositories.entities.Employee;
import org.korek.spring.services.common.helpers.LoginDetails;
import org.korek.spring.services.common.helpers.OpenTime;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.common.collect.Lists;

public class CommonUtils
{

	public static final DateParser DATE_PARSER = new DateParser("yyyy-MM-dd");
	
	private static final String BINDING_BASE = "org.springframework.validation.BindingResult.";

	public static boolean isEmpty(String text)
	{
		boolean result = false;

		if (text == null || text.isEmpty())
		{
			result = true;
		}

		return result;
	}

	public static boolean isNotEmpty(String text)
	{

		return !isEmpty(text);
	}

	public static String getNLastCharacters(String string, int n)
	{
		String result = null;

		if (string != null)
		{
			if (n < string.length())
			{
				result = string.substring(string.length() - n);
			}
			else
			{
				result = string;
			}
		}

		return result;
	}
	
	public static long getLoggedUserId(Principal principal)
	{
		LoginDetails loginDetails = (LoginDetails) principal;
		
		return loginDetails.getId();
	}
	
	
	/**
	 * Diff in minutes
	 * @param d1
	 * @param d2
	 * @return
	 */
	public static int diffInDates(Date d1, Date d2)
	{
		long diff = d1.getTime() - d2.getTime(); 
		
		return (int) diff / 60000;
	}

	public static List<Long> getIDs(List<Employee> employees)
	{
		List<Long> ids = Lists.newArrayListWithCapacity(employees.size());
		for (Employee e : employees)
		{
			ids.add(new Long(e.getId()));
		}
		
		return ids;
	}

//	public static String getDateAsString(Date date, String pattern)
//	{
//		SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
//		
//		return dateFormat.format(date);
//	}
	
	public static String getDateAsString(Date date)
	{
		return DATE_PARSER.format(date);
	}

	public static Date getTommorow()
	{
		Calendar c = Calendar.getInstance();
		c.add(Calendar.DATE, 1);
		
		return c.getTime();
	}
	
	public static boolean needSaveChangesToDB(Object obj1, Object obj2)
	{
		if ((obj1 != null) && !(obj1.equals(obj2)))
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	public static float calcPlaceAvgRate(List<Employee> employees)
	{
		float avgRate = 0.0f;
		
		int ratesSum = 0;
		int ratesNumber = 0;
		
		for (Employee employee : employees)
		{
			int rateNumber = employee.getRateNumber();
			if(rateNumber != 0)
			{
				ratesNumber += rateNumber;
				ratesSum += employee.getRateSum();
			}
		}
		
		if(ratesNumber > 0)
		{
			avgRate = ratesSum / (float) ratesNumber;
		}
		
		return avgRate;
	}

	public static String calcMinDate(WorkPlace workPlace)
	{
		Calendar now = Calendar.getInstance();
		OpenTime openTime = new OpenTime(workPlace);
		
		int hourOpenTo = openTime.getHourTo();
		int hourNow = now.get(Calendar.HOUR_OF_DAY);		
		
		boolean returnTomorow = false;
		if(hourNow > hourOpenTo)
		{
			returnTomorow = true;
		}
		else if(hourNow == hourOpenTo)
		{
			int minOpenTo = openTime.getMinTo();
			int minNow = now.get(Calendar.MINUTE);
			
			if(minNow >= minOpenTo)
			{
				returnTomorow = true;
			}
		}
		
		if(returnTomorow == true)
		{
			now.add(Calendar.DAY_OF_MONTH, 1);
			
			return getDateAsString(now.getTime());
		}
		else
		{
			return getDateAsString(now.getTime());
		}
	
	}

	public static void handleBindingErrors(String attrName, BindingResult bindingResult, RedirectAttributes attr, Object toModel)
	{
		String bindName = BINDING_BASE + attrName;
		
		attr.addFlashAttribute(bindName, bindingResult);
		attr.addFlashAttribute(attrName, toModel);
	}

}
