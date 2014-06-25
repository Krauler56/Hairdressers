package org.korek.spring.controllers.admin;

import java.security.Principal;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.korek.spring.controllers.helpers.ConstraintHandlerDB;
import org.korek.spring.controllers.helpers.ModelAttr;
import org.korek.spring.controllers.helpers.RequestMap;
import org.korek.spring.controllers.helpers.SessionAttr;
import org.korek.spring.controllers.helpers.Views;
import org.korek.spring.controllers.models.EmployeeTO;
import org.korek.spring.controllers.models.EmployeeVisit;
import org.korek.spring.controllers.models.NewEmployee;
import org.korek.spring.controllers.models.NewPlace;
import org.korek.spring.controllers.models.NewVisit;
import org.korek.spring.controllers.models.WorkPlace;
import org.korek.spring.services.common.helpers.SearchingForVisitResult;
import org.korek.spring.services.common.helpers.exceptions.base.PlaceException;
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
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class AdminController
{
	
	private static final Logger log = Logger.getLogger(AdminController.class);

	@Autowired
	IHairdressersManager hairdressersManager;

	@Autowired
	IVisitManager visitManager;

	@Autowired
	IEmployeeManager employeeManager;

	@RequestMapping(value = RequestMap.ADMIN_PLACE_ALL, method = RequestMethod.GET)
	public String adminShowWorkPlaces(Model model)
	{
		List<WorkPlace> allPlaces = hairdressersManager.getAllWorkPlace();
		model.addAttribute(ModelAttr.ALL_PLACES, allPlaces);

		return Views.ADMIN_PLACE_ALL;
	}

	@RequestMapping(value = RequestMap.ADMIN_EMPLOYEE_NEW, method = RequestMethod.GET)
	public String newEmployee(Model model)
	{
		List<WorkPlace> workPlaceWithAvailableVacancy = hairdressersManager.getAllWorkPlaceWithAvailableVacancy();
		model.addAttribute(ModelAttr.ALL_PLACES_WITH_VACANCY, workPlaceWithAvailableVacancy);

		if (!model.containsAttribute(ModelAttr.NEW_EMPLOYEE))
		{
			model.addAttribute(ModelAttr.NEW_EMPLOYEE, new NewEmployee());
		}

		return Views.ADMIN_EMPLOYEE_NEW;
	}

	@RequestMapping(value = RequestMap.ADMIN_ADD_EMPLOYEE, method = RequestMethod.POST)
	public String addNewEmployee(@Valid @ModelAttribute(value = ModelAttr.NEW_EMPLOYEE) NewEmployee newEmployee, BindingResult bindingResult, RedirectAttributes attr)
	{

		boolean added = false;

		if (bindingResult.hasErrors())
		{
			CommonUtils.handleBindingErrors(ModelAttr.NEW_EMPLOYEE, bindingResult, attr, newEmployee);
			
			return "redirect:/" + RequestMap.ADMIN_EMPLOYEE_NEW;
		}

		try
		{
			added = employeeManager.addEmployee(newEmployee);
		}
		catch (Exception e)
		{
			ConstraintHandlerDB.handleHibernateException(e, attr);
			added = false;
		}

		if (added)
		{
			attr.addFlashAttribute(ModelAttr.SUCCES, added);
			return "redirect:/" + RequestMap.ADMIN_EMPLOYEES;
		}
		else
		{
			attr.addFlashAttribute(ModelAttr.NEW_EMPLOYEE, newEmployee);
			return "redirect:/" + RequestMap.ADMIN_EMPLOYEE_NEW;
		}

	}

	@RequestMapping(value = RequestMap.ADMIN_PLACE_NEW, method = RequestMethod.GET)
	public String newPlace(Model model)
	{
		if (!model.containsAttribute(ModelAttr.NEW_PLACE))
		{
			model.addAttribute(ModelAttr.NEW_PLACE, new NewPlace());
		}

		return Views.ADMIN_PLACE_NEW;
	}

	@RequestMapping(value = RequestMap.ADMIN_ADD_PLACE, method = RequestMethod.POST)
	public String addNewPlace(@Valid @ModelAttribute(value = ModelAttr.NEW_PLACE) NewPlace newPlace, BindingResult bindingResult, RedirectAttributes attr)
	{

		if (bindingResult.hasErrors())
		{
			CommonUtils.handleBindingErrors(ModelAttr.NEW_PLACE, bindingResult, attr, newPlace);
			
			return "redirect:/" + RequestMap.ADMIN_PLACE_NEW;
		}

		try
		{
			hairdressersManager.addPlace(newPlace);
		}
		catch (PlaceException e)
		{
			attr.addFlashAttribute(e.getDataToModel(), true);
			attr.addFlashAttribute(ModelAttr.NEW_PLACE, newPlace);
			
			return "redirect:/" + RequestMap.ADMIN_PLACE_NEW;
		}

		return "redirect:/" + RequestMap.ADMIN_PLACE_ALL;

	}

	@RequestMapping(value = RequestMap.ADMIN_PLACE_EDIT, method = RequestMethod.GET)
	public String editPlace(Model model, @PathVariable("id") long id, HttpSession session)
	{

		if (!model.containsAttribute(ModelAttr.WORK_PLACE))
		{
			WorkPlace workPlace = hairdressersManager.getWorkPlace(id);

			model.addAttribute(ModelAttr.WORK_PLACE, workPlace);
			session.setAttribute(SessionAttr.WORK_PLACE, workPlace);
		}

		return Views.ADMIN_PLACE_EDIT; 
	}

	@RequestMapping(value = RequestMap.ADMIN_PLACE_EDIT_POST, method = RequestMethod.POST)
	public String editPlace(Model model, @Valid @ModelAttribute(value = ModelAttr.WORK_PLACE) WorkPlace workPlace, BindingResult bindingResult, HttpSession session, RedirectAttributes attr)
	{
		WorkPlace workPlaceOld = (WorkPlace) session.getAttribute(SessionAttr.WORK_PLACE);

		if (bindingResult.hasErrors())
		{
			CommonUtils.handleBindingErrors(ModelAttr.WORK_PLACE, bindingResult, attr, workPlace);

			return "redirect:/" + "admin/place/" + workPlaceOld.getId() + "/edit";
		}

		try
		{
			hairdressersManager.saveChanges(workPlaceOld, workPlace);
		}
		catch (PlaceException e)
		{
			System.out.println(e.getDataToModel());
			attr.addFlashAttribute(e.getDataToModel(), true);
			attr.addFlashAttribute(ModelAttr.WORK_PLACE, workPlace);
			
			return "redirect:/" + "admin/place/" + workPlaceOld.getId() + "/edit";
		}

		return "redirect:/" + Views.ADMIN_PLACE_ALL;
	}

	@RequestMapping(value = RequestMap.ADMIN_PLACE_VISITS, method = RequestMethod.GET)
	public String visitsPlace(Model model, @PathVariable("id") long id)
	{

		List<EmployeeVisit> upcomingPlaceVisit = visitManager.getUpcomingPlaceVisit(id);
		model.addAttribute(ModelAttr.VISITS, upcomingPlaceVisit);
		model.addAttribute(ModelAttr.PLACE_ID, id);

		return Views.ADMIN_PLACE_VISITS;
	}

	@RequestMapping(value = RequestMap.ADMIN_PLACE_VISITS_PAST, method = RequestMethod.GET)
	public String pastVisitsPlace(Model model, @PathVariable("id") long id)
	{

		List<EmployeeVisit> pastPlaceVisit = visitManager.getPastPlaceVisit(id);
		model.addAttribute(ModelAttr.VISITS, pastPlaceVisit);
		model.addAttribute(ModelAttr.PAST_VISITS, true);
		model.addAttribute(ModelAttr.PREV_PAGE, "admin/place/" + id + "/visits");

		return Views.ADMIN_PLACE_VISITS_PAST;
	}

	@RequestMapping(value = RequestMap.ADMIN_PLACE_VISIT_NEW, method = RequestMethod.GET)
	public String newVisitsPlace(Model model, @PathVariable("id") long placeID, HttpSession session)
	{
		NewVisit newVisit = (NewVisit) session.getAttribute(SessionAttr.VISIT);
		if (newVisit == null)
		{
			newVisit = new NewVisit();
		}
		model.addAttribute(ModelAttr.NEW_VISIT, newVisit);

		session.setAttribute(SessionAttr.NEW_VISIT_PLACE_ID, placeID);

		List<EmployeeTO> employees = hairdressersManager.getAllEmployees(placeID);
		WorkPlace workPlace = hairdressersManager.getWorkPlace(placeID);

		model.addAttribute(ModelAttr.WORK_PLACE, workPlace);
		model.addAttribute(ModelAttr.ALL_EMPLOYEES, employees);

		model.addAttribute(ModelAttr.MIN_DATE, CommonUtils.calcMinDate(workPlace));
		model.addAttribute(ModelAttr.PREV_PAGE, "admin/place/" + placeID + "/visits");

		return Views.ADMIN_PLACE_VISIT_NEW; 

	}

	@RequestMapping(value = RequestMap.ADMIN_CHECK_VISIT, method = RequestMethod.POST)
	public String checkForVisit(@ModelAttribute(value = "newVisit") NewVisit newVisit, HttpSession session, Principal principal, RedirectAttributes attr)
	{
		long placeID = (long) session.getAttribute(SessionAttr.NEW_VISIT_PLACE_ID);

		CheckForVisitHelper.prepareNewVisitAdmin(newVisit, placeID, session);

		SearchingForVisitResult searchingForVisitResult = null;
		try
		{
			searchingForVisitResult = visitManager.checkForVisit(newVisit);
		}
		catch (NewVisitException e)
		{
			attr.addFlashAttribute(e.getDataToModel(), true);

			return "redirect:/" + "admin/place/" + placeID + "/visit/new";
		}
		CheckForVisitHelper.handleCheckForVisit(searchingForVisitResult, newVisit, session, attr);

		return "redirect:/" + "admin/place/" + placeID + "/confirm_visit";
	}

	@RequestMapping(value = RequestMap.ADMIN_CONFIRM_PLACE_VISIT, method = RequestMethod.GET)
	public String yyyy(Model model, @PathVariable("id") long placeID)
	{

		if (!model.containsAttribute(ModelAttr.DATE_AVAIABLE))
		{
			return "redirect:/" + "admin/place/" + placeID + "/visit/new";
		}

		return Views.ADMIN_PLACE_CONFIRM_NEW_VISIT; 
	}

	@RequestMapping(value = RequestMap.ADMIN_CONFIRM_VISIT, method = RequestMethod.POST)
	public String confirmVisit(HttpSession session, RedirectAttributes attr, @RequestParam(value = "suggestedDateID", required = false) Long suggestedDateID)
	{
		NewVisit newVisit = (NewVisit) session.getAttribute(SessionAttr.VISIT);
		long placeID = newVisit.getWorkPlaceID();
		boolean bookVisit = visitManager.bookVisit(newVisit, suggestedDateID);

		if (bookVisit == true)
		{
			attr.addFlashAttribute(ModelAttr.NEW_VISIT_ADDED, true);
			return "redirect:/" + "admin/place/" + placeID + "/visits";
		}
		else
		{
			attr.addFlashAttribute(ModelAttr.NEW_VISIT_FAILED, true);
			return "redirect:/" + "admin/place/" + placeID + "/visit/new";
		}
	}

	@RequestMapping(value = RequestMap.ADMIN_EMPLOYEES, method = RequestMethod.GET)
	public String showAllEmployees(Model model)
	{
		List<EmployeeTO> allEmployees = employeeManager.getAllEmployees();
		model.addAttribute(ModelAttr.ALL_EMPLOYEES, allEmployees);
		model.addAttribute(ModelAttr.PREFIX, "");

		return Views.ADMIN_EMPLOYEES;
	}

	@RequestMapping(value = RequestMap.ADMIN_EMPLOYEE_EDIT, method = RequestMethod.GET)
	public String editEmployeeX(Model model, @PathVariable("id") long employeeID, HttpSession session)
	{
		editEmployee(model, session, employeeID);

		model.addAttribute(ModelAttr.PREV_PAGE, RequestMap.ADMIN_EMPLOYEES);
		model.addAttribute(ModelAttr.ACTION, "admin/employee/edit");

		return Views.ADMIN_EMPLOYEE_EDIT; 
	}

	@RequestMapping(value = RequestMap.ADMIN_EMPLOYEE_EDIT_POST, method = RequestMethod.POST)
	public String editEmployeePost(@Valid @ModelAttribute(value = ModelAttr.EMPLOYEE) EmployeeTO employee, BindingResult bindingResult, HttpSession session, RedirectAttributes attr)
	{

		EmployeeTO employeeOld = (EmployeeTO) session.getAttribute(SessionAttr.EMPLOYEE);
		String returnLink = "admin/employee/" + employeeOld.getId() + "/edit";
		if (bindingResult.hasErrors())
		{
			CommonUtils.handleBindingErrors(ModelAttr.EMPLOYEE, bindingResult, attr, employee);

			return "redirect:/" + returnLink;
		}

		boolean changed = false;
		try
		{
			changed = employeeManager.saveChanges(employeeOld, employee);
		}
		catch (EditEmployeeException e)
		{
			attr.addFlashAttribute(e.getDataToModel(), true);
		}

		attr.addFlashAttribute(ModelAttr.CHANGED, changed);

		return "redirect:/" + returnLink;

	}

	@RequestMapping(value = RequestMap.ADMIN_PLACE_EMPLOYEES, method = RequestMethod.GET)
	public String showPlaceEmployees(Model model, @PathVariable("id") long id)
	{
		List<EmployeeTO> allEmployees = hairdressersManager.getAllEmployees(id);
		model.addAttribute(ModelAttr.ALL_EMPLOYEES, allEmployees);
		model.addAttribute(ModelAttr.PREFIX, "/place/" + id);

		return Views.ADMIN_PLACE_EMPLOYEES; 
	}

	@RequestMapping(value = RequestMap.ADMIN_PLACE_EMPLOYEE_EDIT, method = RequestMethod.GET)
	public String editEmployeeY(Model model, @PathVariable("id") long employeeID, @PathVariable("placeID") long placeID, HttpSession session)
	{
		editEmployee(model, session, employeeID);

		model.addAttribute(ModelAttr.PREV_PAGE, "admin/place/" + placeID + "/employees");
		model.addAttribute(ModelAttr.ACTION, "admin/place/" + placeID + "/employee/edit");

		return Views.ADMIN_EMPLOYEE_EDIT; 
	}
	
	@RequestMapping(value = RequestMap.ADMIN_PLACE_EMPLOYEE_EDIT_POST, method = RequestMethod.POST)
	public String editEmployeePostY(@PathVariable("id") long placeID, @Valid @ModelAttribute(value = ModelAttr.EMPLOYEE) EmployeeTO employee, BindingResult bindingResult, HttpSession session, RedirectAttributes attr)
	{

		EmployeeTO employeeOld = (EmployeeTO) session.getAttribute(SessionAttr.EMPLOYEE);

		String returnLink = "admin/place/" + placeID + "/employee/" + employeeOld.getId() + "/edit";
		
		if (bindingResult.hasErrors())
		{
			CommonUtils.handleBindingErrors(ModelAttr.EMPLOYEE, bindingResult, attr, employee);

			return "redirect:/" + returnLink;
		}

		boolean changed = false;
		try
		{
			changed = employeeManager.saveChanges(employeeOld, employee);
		}
		catch (EditEmployeeException e)
		{
			attr.addFlashAttribute(e.getDataToModel(), true);
		}

		attr.addFlashAttribute(ModelAttr.CHANGED, changed);

		return "redirect:/" + returnLink;

	}
	
	private void editEmployee(Model model, HttpSession session, long employeeID)
	{
		EmployeeTO employee = null;

		if (!model.containsAttribute(ModelAttr.EMPLOYEE))
		{
			employee = employeeManager.getEmployeeTO(employeeID);
			model.addAttribute(ModelAttr.EMPLOYEE, employee);
			session.setAttribute(SessionAttr.EMPLOYEE, employee);
		}

		List<WorkPlace> allPlacesWithVacancy = hairdressersManager.getAllWorkPlaceWithAvailableVacancy();
		WorkPlace workPlace = employeeManager.getEmployeesWorkPlace(employeeID);
		addEmployeesWorkPlaceIfRequired(workPlace, allPlacesWithVacancy);

		model.addAttribute(ModelAttr.ALL_PLACES_WITH_VACANCY, allPlacesWithVacancy);
	}

	private void addEmployeesWorkPlaceIfRequired(WorkPlace workPlace, List<WorkPlace> allPlacesWithVacancy)
	{
		if (workPlace != null)
		{
			long placeID = workPlace.getId();
			boolean contain = false;
			for (WorkPlace place : allPlacesWithVacancy)
			{
				if (place.getId() == placeID)
				{
					contain = true;
					break;
				}
			}
			if (contain == false)
			{
				allPlacesWithVacancy.add(workPlace);
			}
		}

	}

	@RequestMapping(value = RequestMap.ADMIN_EMPLOYEE_VISITS, method = RequestMethod.GET)
	public String visitsEmployee(Model model, @PathVariable("id") long id)
	{

		List<EmployeeVisit> upcomingEmployeeVisit = employeeManager.getUpcomingEmployeeVisits(id);
		model.addAttribute(ModelAttr.VISITS, upcomingEmployeeVisit);
		model.addAttribute(ModelAttr.EMPLOYEE_ID, id);

		return Views.ADMIN_EMPLOYEE_VISITS; 
	}

	@RequestMapping(value = RequestMap.ADMIN_EMPLOYEE_VISITS_PAST, method = RequestMethod.GET)
	public String pastVisitsEmployee(Model model, @PathVariable("id") long id)
	{

		List<EmployeeVisit> pastEmployeeVisit = employeeManager.getPastEmployeeVisits(id);
		model.addAttribute(ModelAttr.VISITS, pastEmployeeVisit);
		model.addAttribute(ModelAttr.PAST_VISITS, true);
		model.addAttribute(ModelAttr.PREV_PAGE, "admin/employee/" + id + "/visits");

		return Views.ADMIN_EMPLOYEE_VISITS_PAST; 
	}

	@RequestMapping(value = RequestMap.ADMIN_VISIT_CANCEL, method = RequestMethod.POST)
	@ResponseStatus(value = HttpStatus.OK)
	public void cancelVist(@PathVariable("id") long visitID)
	{
		employeeManager.cancelVisitAdmin(visitID);
	}

	@RequestMapping(value = RequestMap.ADMIN_VISIT_CONFIRM, method = RequestMethod.POST)
	@ResponseStatus(value = HttpStatus.OK)
	public void confirmPastVisit(Model model, Principal principal, @PathVariable("id") long visitID)
	{
		employeeManager.confirmPastVisitAdmin(visitID);
	}
	
	@PostConstruct
	public void addAdminToDBOnStart()
	{
		NewEmployee newEmployee = new NewEmployee();
		newEmployee.setAdmin(true);
		newEmployee.setFirstName("John");
		newEmployee.setLastName("Doe");
		newEmployee.setLogin("admin");
		newEmployee.setPassword("a");
		
		try
		{
			employeeManager.addEmployee(newEmployee);
		}
		catch (Exception e)
		{
			log.info(e.getLocalizedMessage()); // already exist in DB
		}
	}
}
