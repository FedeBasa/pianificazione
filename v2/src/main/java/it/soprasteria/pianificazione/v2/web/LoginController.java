package it.soprasteria.pianificazione.v2.web;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import it.soprasteria.pianificazione.v2.bean.UserBean;
import it.soprasteria.pianificazione.v2.service.UserService;
import it.soprasteria.pianificazione.v2.util.SessionHelper;

@Controller
public class LoginController {

	private static final Logger LOG = Logger.getLogger(LoginController.class);
	
	@Autowired
	private UserService service;
	
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String view(Model model) {
		
		if (SessionHelper.getUser() != null) {
			
			// utente già loggato lo riporto alla home
			return "redirect:/home";
		}
		
		model.addAttribute("userbean", new UserBean());
		
		return "login";
	}
	
	
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public String login(@ModelAttribute("userbean") @Validated UserBean userBean, BindingResult result, Model model, RedirectAttributes redirectAttributes) {
		
		// TODO
		// aggiungere validator per check username e password non vuoti
		if (result.hasErrors()) {

			LOG.warn("Dati inseriti non validi");
			return "login";
			
		} else {
			
			UserBean user = service.login(userBean.getUsername(), userBean.getPassword());
			if (user == null) {
				
				LOG.warn("Credenziali non valide");
				return "login";
			}
			
			SessionHelper.storeUser(user);
			return "redirect:/home";
		}
	}
	
	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	public String logout(Model model) {
		
		SessionHelper.logout();
		
		return "redirect:/login";
	}	
	
	@RequestMapping(value = "/changepw", method = RequestMethod.GET)
	public String changepw(Model model){
		
		return("change_password");
		
	}
	
	@RequestMapping(value = "/saveNewPassword", method = RequestMethod.POST)
	public String saveNewPassword(@RequestParam(name = "userid")String userid , @RequestParam(name = "npw") String npw , @RequestParam(name = "opw") String opw){
		
		service.changePw(userid, npw, opw);
		
		return "redirect:/login";
	}
	
	

}
