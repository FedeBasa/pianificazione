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
	public String login(@ModelAttribute("userbean") @Validated UserBean userBean, BindingResult result, RedirectAttributes redirectAttributes) {
		
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
	public String logout() {
		
		SessionHelper.logout();
		
		return "redirect:/login";
	}	
	
	@RequestMapping(value = "/changepw", method = RequestMethod.GET)
	public String changepw(){
		
		return "change_pwd";
		
	}
	
	@RequestMapping(value = "/changepw", method = RequestMethod.POST)
	public String saveNewPassword(@RequestParam(name = "npw") String npw, Model model){
		
		if (npw == null || npw.length() == 0) {
			model.addAttribute("errorMessage", "Valorizzare i campi");
			return "change_pwd";
		}
		
		UserBean user = SessionHelper.getUser();
		
		service.changePw(user.getUsername(), npw);
		user.setFirstlogin(1);
		
		return "redirect:/home";
	}
	
	

}
