package org.korek.spring.controllers;

import javax.validation.Valid;

import org.korek.spring.controllers.helpers.ConstraintHandlerDB;
import org.korek.spring.controllers.helpers.ModelAttr;
import org.korek.spring.controllers.helpers.RequestMap;
import org.korek.spring.controllers.helpers.Views;
import org.korek.spring.controllers.models.NewClient;
import org.korek.spring.services.client.interfaces.IClientManager;
import org.korek.spring.utils.CommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class LoginRegisterController
{

	@Autowired
	IClientManager clientManager;

	@Autowired
	AuthenticationProvider customAuthenticationProvider;

	@RequestMapping(value = RequestMap.LOGIN, method = RequestMethod.GET)
	public String login(Model model)
	{
		processLogin(model);

		return Views.LOGIN;
	}

	@RequestMapping(value = RequestMap.LOGIN_ERROR, method = RequestMethod.GET)
	public String loginErr(Model model)
	{
		processLogin(model);
		model.addAttribute(ModelAttr.LOGIN_ERROR, true);

		return Views.LOGIN;
	}

	@RequestMapping(value = RequestMap.LOGIN_EXPIRED, method = RequestMethod.GET)
	public String loginExpired(Model model)
	{
		processLogin(model);
		model.addAttribute(ModelAttr.LOGIN_EXPIRED, true);

		return Views.LOGIN;
	}

	@RequestMapping(value = RequestMap.REGISTER, method = RequestMethod.POST)
	public String processForm(@Valid @ModelAttribute(value = ModelAttr.NEW_CLIENT) NewClient newClient, BindingResult bindingResult, RedirectAttributes attr)
	{

		if (bindingResult.hasErrors())
		{
			
			CommonUtils.handleBindingErrors(ModelAttr.NEW_CLIENT, bindingResult, attr, newClient);

			return "redirect:/" + Views.LOGIN;
		}

		try
		{
			clientManager.registerNewClient(newClient);
		}
		catch (Exception e)
		{
			ConstraintHandlerDB.handleHibernateException(e, attr);
			attr.addFlashAttribute(ModelAttr.NEW_CLIENT, newClient);

			return "redirect:/" + Views.LOGIN;
		}

		return "redirect:/";
	}

	private void processLogin(Model model)
	{
		if (!model.containsAttribute(ModelAttr.NEW_CLIENT))
		{
			model.addAttribute(ModelAttr.NEW_CLIENT, new NewClient());
		}
	}
}
