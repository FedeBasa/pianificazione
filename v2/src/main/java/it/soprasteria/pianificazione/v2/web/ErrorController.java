package it.soprasteria.pianificazione.v2.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class ErrorController {

	@RequestMapping(value = "/error/400", method = RequestMethod.GET)
	public String error400(Model model) {
		
		return "/error/400";
	}

	@RequestMapping(value = "/error/404", method = RequestMethod.GET)
	public String error404(Model model) {
		
		return "/error/404";
	}

	@RequestMapping(value = "/error/500", method = RequestMethod.GET)
	public String error500(Model model) {
		
		return "/error/500";
	}

}
