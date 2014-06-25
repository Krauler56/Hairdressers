package org.korek.spring.controllers.client;

import java.security.Principal;
import java.util.List;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.korek.spring.controllers.helpers.ModelAttr;
import org.korek.spring.controllers.helpers.RequestMap;
import org.korek.spring.controllers.helpers.SessionAttr;
import org.korek.spring.controllers.helpers.Views;
import org.korek.spring.controllers.models.ClientTO;
import org.korek.spring.controllers.models.ClientVisit;
import org.korek.spring.controllers.models.EmployeeTO;
import org.korek.spring.controllers.models.NewVisit;
import org.korek.spring.controllers.models.RateVisit;
import org.korek.spring.controllers.models.WorkPlace;
import org.korek.spring.services.client.interfaces.IClientManager;
import org.korek.spring.services.common.helpers.SearchingForVisitResult;
import org.korek.spring.services.common.helpers.exceptions.base.NewVisitException;
import org.korek.spring.services.common.interfaces.IHairdressersManager;
import org.korek.spring.services.common.interfaces.IVisitManager;
import org.korek.spring.services.employee.interfaces.IEmployeeManager;
import org.korek.spring.utils.CheckForVisitHelper;
import org.korek.spring.utils.CommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@PreAuthorize("hasRole('ROLE_USER')")
public class ClientController
{
	@Autowired
	IHairdressersManager hairdressersManager;

	@Autowired
	IVisitManager visitManager;

	@Autowired
	IClientManager clientManager;

	@Autowired
	IEmployeeManager employeeManager;

	@RequestMapping(value = RequestMap.CLIENT_VISIT_NEW, method = RequestMethod.GET)
	public String newVisit(Model model, RedirectAttributes attr)
	{
		List<WorkPlace> allPlaces = hairdressersManager.getAllWorkPlaceWithAtLeastOneEmployee();
		if(allPlaces.isEmpty())
		{
			attr.addFlashAttribute(ModelAttr.NO_PLACES_TO_SELECT, true);
			
			return "redirect:/" + RequestMap.CLIENT_VISIT_UPCOMING;
		}
		
		model.addAttribute(ModelAttr.ALL_PLACES, allPlaces);

		return Views.CLIENT_VISIT_NEW;
	}

	@RequestMapping(value = RequestMap.CLIENT_CHECK_VISIT, method = RequestMethod.GET)
	public String selectPlace(Model model, @PathVariable("id") long placeID, HttpSession session)
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

		return Views.CLIENT_BOOK_VISIT;
	}

	@RequestMapping(value = RequestMap.CLIENT_SELECT_PLACE, method = RequestMethod.POST)
	public String selectPlacePost(@RequestParam("placeID") long placeID)
	{
		return "redirect:/" + RequestMap.clientCheckVisit(placeID);
	}

	@RequestMapping(value = RequestMap.CLIENT_CHECK_VISIT_POST, method = RequestMethod.POST)
	public String checkForVisit(@ModelAttribute(value = "newVisit") NewVisit newVisit, HttpSession session, Principal principal, RedirectAttributes attr)
	{

		long placeID = (long) session.getAttribute(SessionAttr.NEW_VISIT_PLACE_ID);
		CheckForVisitHelper.prepareNewVisitClient(newVisit, placeID, principal, session);

		SearchingForVisitResult searchingForVisitResult = null;
		try
		{
			searchingForVisitResult = visitManager.checkForVisit(newVisit);
		}
		catch (NewVisitException e)
		{
			attr.addFlashAttribute(e.getDataToModel(), true);

			return "redirect:/" + RequestMap.clientCheckVisit(placeID);
		}

		CheckForVisitHelper.handleCheckForVisit(searchingForVisitResult, newVisit, session, attr);

		return "redirect:/" + RequestMap.clientConfirmVisit(placeID);

	}

	@RequestMapping(value = RequestMap.CLIENT_CONFIRM_VISIT, method = RequestMethod.GET)
	public String yyyy(Model model, @PathVariable("id") long placeID)
	{
		if (!model.containsAttribute(ModelAttr.DATE_AVAIABLE))
		{
			return "redirect:/" + RequestMap.clientCheckVisit(placeID);
		}

		return Views.CLIENT_CONFIRM_VISIT;
	}

	@RequestMapping(value = RequestMap.CLIENT_CONFIRM_VISIT_POST, method = RequestMethod.POST)
	public String confirmVisit(HttpSession session, RedirectAttributes attr, @RequestParam(value = "suggestedDateID", required = false) Long suggestedDateID)
	{
		NewVisit newVisit = (NewVisit) session.getAttribute(SessionAttr.VISIT);

		boolean bookVisit = visitManager.bookVisit(newVisit, suggestedDateID);

		if (bookVisit == true)
		{
			attr.addFlashAttribute(ModelAttr.NEW_VISIT_ADDED, true);
			return "redirect:/" + Views.CLIENT_VISIT_UPCOMING;
		}
		else
		{
			attr.addFlashAttribute(ModelAttr.NEW_VISIT_FAILED, true);
			return "redirect:/" + Views.CLIENT_VISIT_NEW;
		}
	}

	@RequestMapping(value = RequestMap.CLIENT_PROFILE, method = RequestMethod.GET)
	public String clientEditProfile(Model model, Principal principal, HttpSession session)
	{
		if (!model.containsAttribute(ModelAttr.CLIENT))
		{
			ClientTO client = clientManager.getClient(CommonUtils.getLoggedUserId(principal));
			model.addAttribute(ModelAttr.CLIENT, client);
			session.setAttribute(SessionAttr.CLIENT, client);
		}

		return Views.CLIENT_PROFILE;
	}

	@RequestMapping(value = RequestMap.CLIENT_EDIT_PROFILE, method = RequestMethod.POST)
	public String editProfile(@Valid @ModelAttribute(value = ModelAttr.CLIENT) ClientTO client, BindingResult bindingResult, HttpSession session, RedirectAttributes attr)
	{
		if (bindingResult.hasErrors())
		{
			CommonUtils.handleBindingErrors(ModelAttr.CLIENT, bindingResult, attr, client);

			return "redirect:/" + Views.CLIENT_PROFILE;
		}

		ClientTO clientOld = (ClientTO) session.getAttribute(SessionAttr.CLIENT);

		boolean changed = clientManager.saveChanges(clientOld, client);
		if (changed)
		{
			attr.addFlashAttribute(ModelAttr.SUCCES, true);
		}

		return "redirect:/" + RequestMap.CLIENT_PROFILE;
	}

	@RequestMapping(value = RequestMap.CLIENT_VISIT_UPCOMING, method = RequestMethod.GET)
	public String showUpcomingVisits(Model model, Principal principal)
	{
		List<ClientVisit> clientVisits = clientManager.getUpcomingClientVisits(CommonUtils.getLoggedUserId(principal));
		model.addAttribute(ModelAttr.CLIENT_VISITS, clientVisits);

		return Views.CLIENT_VISIT_UPCOMING;
	}

	@RequestMapping(value = RequestMap.CLIENT_VISIT_PAST, method = RequestMethod.GET)
	public String showPastVisits(Model model, Principal principal)
	{
		List<ClientVisit> clientPastVisits = clientManager.getPastClientVisits(CommonUtils.getLoggedUserId(principal));
		model.addAttribute(ModelAttr.CLIENT_PAST_VISITS, clientPastVisits);

		return Views.CLIENT_VISIT_PAST;
	}

	@RequestMapping(value = RequestMap.CLIENT_VISIT_RATE, method = RequestMethod.GET)
	public String ratePastVisits(Model model, @PathVariable("id") long id)
	{
		model.addAttribute(ModelAttr.ID, id);
		model.addAttribute(ModelAttr.RATE_VISIT, new RateVisit());

		return Views.CLIENT_RATE_VISIT;
	}

	@RequestMapping(value = RequestMap.CLIENT_VISIT_RATE, method = RequestMethod.POST)
	public String ratePastVisitsPOST(Model model, Principal principal, @ModelAttribute(value = ModelAttr.RATE_VISIT) RateVisit rateVisit, @PathVariable("id") long visitID)
	{
		clientManager.rateVisit(rateVisit, CommonUtils.getLoggedUserId(principal), visitID);

		return "redirect:/" + RequestMap.CLIENT_VISIT_PAST;
	}

	@RequestMapping(value = RequestMap.CLIENT_VISIT_CANCEL, method = RequestMethod.POST)
	public String cancelVist(Model model, Principal principal, @PathVariable("id") long visitID)
	{
		clientManager.cancelVisitClient(visitID, CommonUtils.getLoggedUserId(principal));

		return "redirect:/" + RequestMap.CLIENT_VISIT_UPCOMING;
	}
}
