package org.korek.spring.controllers.employee;

import java.security.Principal;
import java.util.List;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.korek.spring.controllers.helpers.ModelAttr;
import org.korek.spring.controllers.helpers.RequestMap;
import org.korek.spring.controllers.helpers.SessionAttr;
import org.korek.spring.controllers.helpers.Views;
import org.korek.spring.controllers.models.EmployeeTO;
import org.korek.spring.controllers.models.EmployeeVisit;
import org.korek.spring.controllers.models.NewVisit;
import org.korek.spring.controllers.models.WorkPlace;
import org.korek.spring.services.common.helpers.SearchingForVisitResult;
import org.korek.spring.services.common.helpers.exceptions.base.NewVisitException;
import org.korek.spring.services.common.interfaces.IHairdressersManager;
import org.korek.spring.services.common.interfaces.IVisitManager;
import org.korek.spring.services.employee.helpers.EditEmployeeException;
import org.korek.spring.services.employee.interfaces.IEmployeeManager;
import org.korek.spring.utils.CheckForVisitHelper;
import org.korek.spring.utils.CommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@PreAuthorize("hasAnyRole('ROLE_EMPLOYEE', 'ROLE_ADMIN')")
public class EmployeeController
{
	
	@Autowired
	IVisitManager visitManager;
	
	@Autowired
	IEmployeeManager employeeManager;
	
	@Autowired
	IHairdressersManager hairdressersManager;
	
	@RequestMapping(value = RequestMap.EMPLOYEE_PROFILE, method = RequestMethod.GET)
	public String employeeEditProfile(Model model, Principal principal, HttpSession session)
	{
		if (!model.containsAttribute(ModelAttr.EMPLOYEE))
		{
			EmployeeTO employee = employeeManager.getEmployeeTO(CommonUtils.getLoggedUserId(principal));

			model.addAttribute(ModelAttr.EMPLOYEE, employee);
			session.setAttribute(SessionAttr.EMPLOYEE, employee);
		}

		return Views.EMPLOYEE_PROFILE;
	}
	
	@RequestMapping(value = RequestMap.EMPLOYEE_EDIT_PROFILE, method = RequestMethod.POST)
	public String editProfile(@Valid @ModelAttribute(value = ModelAttr.EMPLOYEE) EmployeeTO employee, BindingResult bindingResult, HttpSession session, Model model, Principal principal, RedirectAttributes attr)
	{
		
		if(bindingResult.hasErrors())
		{
			CommonUtils.handleBindingErrors(ModelAttr.EMPLOYEE, bindingResult, attr, employee);

			return "redirect:/" + Views.EMPLOYEE_PROFILE;
		}
		
		EmployeeTO employeeOld = (EmployeeTO) session.getAttribute(SessionAttr.EMPLOYEE);
		employeeOld.setPlaceID(0); // not gonna change place

		
		boolean changed = false;
		try
		{
			changed = employeeManager.saveChanges(employeeOld, employee);
		}
		catch (EditEmployeeException e)
		{
			attr.addFlashAttribute(e.getDataToModel(), true);
			return "redirect:/" + RequestMap.EMPLOYEE_PROFILE;
		}
		
		if(changed == true)
		{
			attr.addFlashAttribute(ModelAttr.SUCCES, true);
		}
		
		return "redirect:/" + RequestMap.EMPLOYEE_PROFILE;
		
	}
	
	@RequestMapping(value = RequestMap.EMPLOYEE_VISIT_NEW, method = RequestMethod.GET)
	public String newVisit(Model model, Principal principal, HttpSession session, RedirectAttributes attr)
	{
		WorkPlace workPlace = employeeManager.getEmployeesWorkPlace(CommonUtils.getLoggedUserId(principal));
		
		if(workPlace == null)
		{
			attr.addFlashAttribute(ModelAttr.NO_WORK_PLACE, true);
			
			return "redirect:/" + RequestMap.EMPLOYEE_VISIT_UPCOMING;
		}
		
		NewVisit newVisit = (NewVisit) session.getAttribute(SessionAttr.VISIT);
		if (newVisit == null)
		{
			newVisit = new NewVisit();
		}
		model.addAttribute(ModelAttr.NEW_VISIT, newVisit);

		model.addAttribute(ModelAttr.WORK_PLACE, workPlace);
		session.setAttribute(SessionAttr.NEW_VISIT_PLACE_ID, workPlace.getId());

		model.addAttribute(ModelAttr.MIN_DATE, CommonUtils.calcMinDate(workPlace));
		
		return Views.EMPLOYEE_VISIT_NEW;
	}
	
	@RequestMapping(value = RequestMap.EMPLOYEE_CHECK_VISIT_POST, method = RequestMethod.POST)
	public String checkForVisit(@ModelAttribute(value = "newVisit") NewVisit newVisit, HttpSession session,Principal principal, RedirectAttributes attr)
	{
		
		long placeID = (long) session.getAttribute(SessionAttr.NEW_VISIT_PLACE_ID);
		CheckForVisitHelper.prepareNewVisitEmployee(newVisit, placeID, principal, session);
		
		SearchingForVisitResult searchingForVisitResult = null;
		try
		{
			searchingForVisitResult = visitManager.checkForVisit(newVisit);
		}
		catch (NewVisitException e)
		{
			attr.addFlashAttribute(e.getDataToModel(), true);
			
			return "redirect:/" + RequestMap.EMPLOYEE_VISIT_NEW;
		}
		
		CheckForVisitHelper.handleCheckForVisit(searchingForVisitResult, newVisit, session, attr);
		
		return "redirect:/" + RequestMap.EMPLOYEE_CONFIRM_VISIT;
	}
	
	@RequestMapping(value = RequestMap.EMPLOYEE_CONFIRM_VISIT, method = RequestMethod.GET)
	public String yyyy(Model model)
	{

		if (!model.containsAttribute(ModelAttr.DATE_AVAIABLE))
		{
			return "redirect:/" + RequestMap.EMPLOYEE_VISIT_NEW;
		}

		return  Views.EMPLOYEE_VISIT_NEW_CONFIRM; 
	}
	
	@RequestMapping(value = RequestMap.EMPLOYEE_CONFIRM_VISIT_POST, method = RequestMethod.POST)
	public String confirmVisit(HttpSession session, Model model, Principal principal, RedirectAttributes attr,  @RequestParam(value= "suggestedDateID", required=false) Long suggestedDateID)
	{

		NewVisit newVisit = (NewVisit) session.getAttribute(SessionAttr.VISIT);

		boolean bookVisit = visitManager.bookVisit(newVisit, suggestedDateID);

		if (bookVisit == true)
		{
			attr.addFlashAttribute(ModelAttr.NEW_VISIT_ADDED, true);
			return "redirect:/" + RequestMap.EMPLOYEE_VISIT_UPCOMING;
		}
		else
		{
			attr.addFlashAttribute(ModelAttr.NEW_VISIT_FAILED, true);
			return "redirect:/" + RequestMap.EMPLOYEE_VISIT_NEW;
		}
	}
	
	@RequestMapping(value = RequestMap.EMPLOYEE_VISIT_PAST, method = RequestMethod.GET)
	public String pastVisit(Model model, Principal principal)
	{
		List<EmployeeVisit> pastEmployeeVisits = employeeManager.getPastEmployeeVisits(CommonUtils.getLoggedUserId(principal));
		model.addAttribute(ModelAttr.VISITS, pastEmployeeVisits);
		model.addAttribute(ModelAttr.PAST_VISITS, true);
		
		return Views.EMPLOYEE_VISIT_PAST;
	}
	
	@RequestMapping(value = RequestMap.EMPLOYEE_VISIT_UPCOMING, method = RequestMethod.GET)
	public String upcomingVisit(Model model, Principal principal)
	{
		List<EmployeeVisit> upcomingEmployeeVisits = employeeManager.getUpcomingEmployeeVisits(CommonUtils.getLoggedUserId(principal));
		model.addAttribute(ModelAttr.VISITS, upcomingEmployeeVisits);
		
		return Views.EMPLOYEE_VISIT_UPCOMING;
	}
	
	@RequestMapping(value = RequestMap.EMPLOYEE_VISIT_CONFIRM, method = RequestMethod.POST)
	public String confirmPastVisit(Model model, Principal principal, @PathVariable("id") long visitID)
	{
		employeeManager.confirmPastVisit(visitID, CommonUtils.getLoggedUserId(principal));
		
		return "redirect:/" + RequestMap.EMPLOYEE_VISIT_PAST;
	}
	
	@RequestMapping(value = RequestMap.EMPLOYEE_VISIT_CANCEL, method = RequestMethod.POST)
	@ResponseStatus(value = HttpStatus.OK)
	public void cancelVist(Model model, Principal principal, @PathVariable("id") long visitID)
	{
		employeeManager.cancelVisitEmployee(visitID, CommonUtils.getLoggedUserId(principal));
	}
}
