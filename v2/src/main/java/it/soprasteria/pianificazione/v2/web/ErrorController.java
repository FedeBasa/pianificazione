package it.soprasteria.pianificazione.v2.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class ErrorController {

	private static final String ERROR_500 = "/error/500";
	private static final String ERROR_404 = "/error/404";
	private static final String ERROR_400 = "/error/400";

	@RequestMapping(value = ERROR_400, method = RequestMethod.GET)
	public String error400() {
		
		return ERROR_400;
	}

	@RequestMapping(value = ERROR_404, method = RequestMethod.GET)
	public String error404() {
		
		return ERROR_404;
	}

	@RequestMapping(value = ERROR_500, method = RequestMethod.GET)
	public String error500() {
		
		return ERROR_500;
	}

}
