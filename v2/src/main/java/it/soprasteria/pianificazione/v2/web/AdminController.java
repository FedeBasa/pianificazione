package it.soprasteria.pianificazione.v2.web;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import it.soprasteria.pianificazione.v2.bean.V2Bean;
import it.soprasteria.pianificazione.v2.service.V2Service;
import it.soprasteria.pianificazione.v2.util.SessionHelper;

@Controller
public class AdminController {

	private static final Logger LOG = Logger.getLogger(AdminController.class);
	
	@Autowired
	private V2Service service;

	@RequestMapping(value = "/admin/approva", method = RequestMethod.POST)
	public String approva(@RequestParam int month, Model model, RedirectAttributes redirectAttributes) {

		String user = SessionHelper.getUser().getUsername();
		service.approveMonth(user, month);
		return "redirect:/admin/approva";
	}

	@RequestMapping(value = "/admin/approva", method = RequestMethod.GET)
	public String approvaPage(Model model, RedirectAttributes redirectAttributes) {
	    
		String user = SessionHelper.getUser().getUsername();
		List<V2Bean> v2List = service.getV2ToApprove(user);
		model.addAttribute("v2List",v2List);
		
		return "approva_mese";
	}
}
