package org.korek.spring;

import java.util.Calendar;
import java.util.GregorianCalendar;

import junit.framework.Assert;

import org.junit.Test;
import org.korek.spring.repositories.entities.Hairdressers;
import org.korek.spring.services.common.helpers.OpenTime;
import org.korek.spring.utils.CommonUtils;


public class CommonUtilsTest
{

	@Test
	public void testGetNLastCharacters()
	{
		Assert.assertEquals("", CommonUtils.getNLastCharacters("aaa", 0));
		
		Assert.assertEquals("a", CommonUtils.getNLastCharacters("aaa", 1));
		
		Assert.assertEquals("aa", CommonUtils.getNLastCharacters("aaa", 2));
		
		Assert.assertEquals("aaa", CommonUtils.getNLastCharacters("aaa", 3));
		
		Assert.assertEquals("aaa", CommonUtils.getNLastCharacters("aaa", 4));
		
		Assert.assertEquals("", CommonUtils.getNLastCharacters("", 4));
		
		Assert.assertEquals(null, CommonUtils.getNLastCharacters(null, 4));
		
	}
	
	@Test
	public void diffInDateTest()
	{
		Calendar c1 = new GregorianCalendar(1500, 2, 3, 9, 40, 0);
		Calendar c2 = new GregorianCalendar(1500, 2, 3, 9, 20, 0);
		
		int minutes = CommonUtils.diffInDates(c1.getTime(), c2.getTime());
		
		Assert.assertEquals(20, minutes);
	}
	
	@Test
	public void openTimeTest()
	{
		Hairdressers hairdressers = new Hairdressers();
		hairdressers.setOpenFrom("07:00");
		hairdressers.setOpenTo("15:30");
		
		OpenTime openTime = new OpenTime(hairdressers );
		
		Assert.assertEquals(7, openTime.getHourFrom());
		Assert.assertEquals(0, openTime.getMinFrom());
		
		Assert.assertEquals(15, openTime.getHourTo());
		Assert.assertEquals(30, openTime.getMinTo());
	}
}
