package it.soprasteria.pianificazione.v2.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import it.soprasteria.pianificazione.v2.service.PmService;
import it.soprasteria.pianificazione.v2.util.SessionHelper;

@Controller
public class PmViewController {

	@Autowired
	PmService pmservice;
	
	@RequestMapping(value = "/pmView", method = RequestMethod.GET)
	public String view(@RequestParam (name = "month" , required= true)int month, Model model) {
		
		model.addAttribute("list", pmservice.getStatus(month));
		
		return "pm_view";
	}
	
}
