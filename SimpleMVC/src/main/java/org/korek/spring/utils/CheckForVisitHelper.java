package org.korek.spring.utils;

import java.security.Principal;

import javax.servlet.http.HttpSession;

import org.korek.spring.controllers.helpers.ModelAttr;
import org.korek.spring.controllers.helpers.SessionAttr;
import org.korek.spring.controllers.models.NewVisit;
import org.korek.spring.services.common.helpers.SearchingForVisitResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

public class CheckForVisitHelper
{
	public static void handleCheckForVisit(SearchingForVisitResult searchingForVisitResult, NewVisit newVisit, HttpSession session, RedirectAttributes attr)
	{
		newVisit.setSearchingForVisitResult(searchingForVisitResult);
		attr.addFlashAttribute(ModelAttr.START_DATE, searchingForVisitResult.getStartDate());
		
		if (searchingForVisitResult.isDateAvailable() == true) 
		{
			attr.addFlashAttribute(ModelAttr.DATE_AVAIABLE, true);
		}
		else 
		{
			attr.addFlashAttribute(ModelAttr.DATE_AVAIABLE, false);
			attr.addFlashAttribute(ModelAttr.SUGGESTED_DATES, searchingForVisitResult.getSuggestedDates());
		}
	}

	public static void prepareNewVisitClient(NewVisit newVisit, long placeID, Principal principal, HttpSession session)
	{
		newVisit.setClientID(CommonUtils.getLoggedUserId(principal));
		prepareNewVisit(newVisit, placeID, session);
	}
	
	public static void prepareNewVisitEmployee(NewVisit newVisit, long placeID, Principal principal, HttpSession session)
	{
		newVisit.setEmployeeID(CommonUtils.getLoggedUserId(principal));
		prepareNewVisit(newVisit, placeID, session);
	}
	
	public static void prepareNewVisitAdmin(NewVisit newVisit, long placeID, HttpSession session)
	{
		prepareNewVisit(newVisit, placeID, session);
	}
	
	private static void prepareNewVisit(NewVisit newVisit, long placeID, HttpSession session)
	{
		newVisit.setWorkPlaceID(placeID);
		session.setAttribute(SessionAttr.VISIT, newVisit);
	}
}
