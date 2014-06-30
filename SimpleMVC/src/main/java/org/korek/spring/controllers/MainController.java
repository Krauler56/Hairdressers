package org.korek.spring.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.korek.spring.controllers.helpers.ModelAttr;
import org.korek.spring.controllers.helpers.RequestMap;
import org.korek.spring.controllers.helpers.Views;
import org.korek.spring.controllers.models.ChangePassword;
import org.korek.spring.controllers.models.ClientTO;
import org.korek.spring.controllers.models.EmployeeTO;
import org.korek.spring.controllers.models.WorkPlace;
import org.korek.spring.services.client.interfaces.IClientManager;
import org.korek.spring.services.common.interfaces.IAuthManager;
import org.korek.spring.services.common.interfaces.IHairdressersManager;
import org.korek.spring.services.employee.interfaces.IEmployeeManager;
import org.korek.spring.utils.CommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.RequestContextUtils;

/**
 * Handles requests for the application home page.
 */
@Controller
public class MainController
{

	@Autowired
	IAuthManager authManager;

	@Autowired
	IClientManager clientManager;

	@Autowired
	IHairdressersManager hairdressersManager;

	@Autowired
	IEmployeeManager employeeManager;

	@RequestMapping(value = {RequestMap.HOME, "/"}, method = RequestMethod.GET)
	public String home(Model model)
	{
		return Views.HOME;
	}

	@PreAuthorize("isAuthenticated()")
	@RequestMapping(value = RequestMap.PASSWORD, method = RequestMethod.GET)
	public String changePass(Model model)
	{
		if (!model.containsAttribute(ModelAttr.CHANGE_PASSWORD))
		{
			model.addAttribute(ModelAttr.CHANGE_PASSWORD, new ChangePassword());
		}

		return Views.CHANGE_PASS;
	}
	
	@PreAuthorize("isAuthenticated()")
	@RequestMapping(value = RequestMap.PASSWORD, method = RequestMethod.POST)
	public String changePassForm(@Valid @ModelAttribute(value = ModelAttr.CHANGE_PASSWORD) ChangePassword changePassword, BindingResult bindingResult, Model model, Authentication authentication, RedirectAttributes attr)
	{
		if (bindingResult.hasErrors())
		{
			CommonUtils.handleBindingErrors(ModelAttr.CHANGE_PASSWORD, bindingResult, attr, changePassword);

			return "redirect:/" + RequestMap.PASSWORD;
		}

		boolean changed = authManager.changePass(changePassword, authentication);

		if (changed)
		{
			attr.addFlashAttribute(ModelAttr.SUCCES, true);
		}
		else
		{
			attr.addFlashAttribute(ModelAttr.SUCCES, false);
		}

		return "redirect:/" + RequestMap.PASSWORD;
	}

	@RequestMapping(value = RequestMap.LANGUAGE, method = RequestMethod.POST)
	@ResponseStatus(value = HttpStatus.OK)
	public void language(@PathVariable("locale") String locale, HttpServletRequest request, HttpServletResponse response)
	{
		LocaleResolver localeResolver = RequestContextUtils.getLocaleResolver(request);
		localeResolver.setLocale(request, response, StringUtils.parseLocaleString(locale));
	}

	@RequestMapping(value = RequestMap.DETAILS_CLIENT, method = RequestMethod.GET)
	public String clientDetails(Model model, @PathVariable("id") long id)
	{
		ClientTO client = clientManager.getClient(id);
		model.addAttribute(ModelAttr.CLIENT, client);

		return Views.DETAILS_CLIENT;
	}

	@RequestMapping(value = RequestMap.DETAILS_PLACE, method = RequestMethod.GET)
	public String placeDetails(Model model, @PathVariable("id") long id)
	{

		WorkPlace workPlace = hairdressersManager.getWorkPlaceDetails(id);
		model.addAttribute(ModelAttr.WORK_PLACE, workPlace);

		return Views.DETAILS_PLACE;
	}

	@RequestMapping(value = RequestMap.DETAILS_EMPLOYEE, method = RequestMethod.GET)
	public String employeeDetails(Model model, @PathVariable("id") long id)
	{

		EmployeeTO employee = employeeManager.getEmployeeTO(id);
		model.addAttribute(ModelAttr.EMPLOYEE, employee);

		return Views.DETAILS_EMPLOYEE;
	}

}
