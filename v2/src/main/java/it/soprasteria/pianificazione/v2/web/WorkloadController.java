package it.soprasteria.pianificazione.v2.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import it.soprasteria.pianificazione.v2.bean.WorkloadBean;
import it.soprasteria.pianificazione.v2.service.WorkloadService;
import it.soprasteria.pianificazione.v2.util.SessionHelper;

@Controller
public class WorkloadController {

	@Autowired
	private WorkloadService service;
	
	@RequestMapping(value = "/workload", method = RequestMethod.GET)
	public ModelAndView home(@RequestParam(required = true, name = "month") int month, @RequestParam(required = true, name = "bu") int businessUnit) {

		String username = SessionHelper.getUser().getUsername();
		
		List<WorkloadBean> list = service.findWorkload(month, username);
		
		ModelAndView model = new ModelAndView();
		model.setViewName("workload_resources");
		
		model.addObject("list", list);
		model.addObject("month", month);
		model.addObject("businessUnit", businessUnit);
		
		return model;
	}
}
