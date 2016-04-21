package it.soprasteria.pianificazione.v2.web;

import java.security.Principal;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import it.soprasteria.pianificazione.v2.bean.UserBean;
import it.soprasteria.pianificazione.v2.service.LoginService;
import it.soprasteria.pianificazione.v2.util.SessionHelper;

@Controller
public class UserPrincipalController {

	@Autowired
	private LoginService service;

	@RequestMapping(value = "/userprincipal", method = RequestMethod.GET)
	public String userPrincipal(Model model, RedirectAttributes redirectAttributes, HttpServletRequest request, HttpServletResponse response) {

		if (SessionHelper.getUser() != null) {

			// utente già loggato lo riporto alla home
			return "redirect:/home";
		}

		Principal userPrincipal = request.getUserPrincipal();

		UserBean userBean = service.getUserFromUserPrincipal(userPrincipal.getName());

		if (userBean == null) {

			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return "/error/401";
		}

		SessionHelper.storeUser(userBean);

		return "redirect:/home";
	}

}
