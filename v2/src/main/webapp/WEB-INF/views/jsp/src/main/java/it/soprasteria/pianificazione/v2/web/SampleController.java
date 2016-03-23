package it.soprasteria.pianificazione.v2.web;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import it.soprasteria.pianificazione.v2.bean.EmployeeBean;
import it.soprasteria.pianificazione.v2.service.EmployeeService;

@Controller
public class SampleController {

	private static final Logger LOG = Logger.getLogger(SampleController.class);
	
	@Autowired
	private EmployeeService employeeService;
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String index(Map<String, Object> model) {

		LOG.debug("index() is executed!");

		return "index";
	}

	@RequestMapping(value = "/sample/method1", method = RequestMethod.GET)
	public ModelAndView method1(@RequestParam(required=false, name="name") String name) {

		LOG.debug("parametro name: " + name);
		
		ModelAndView model = new ModelAndView();
		model.setViewName("index");
		
		return model;
	}
	
	@RequestMapping(value = "/autocomplete/employee", method = RequestMethod.GET)
	public @ResponseBody List<EmployeeBean> autocompleEmployee() {
	
		List<EmployeeBean> result = new ArrayList<EmployeeBean>();
		
		result.add(employeeService.findByBadgeNumber("1"));
		result.add(employeeService.findByBadgeNumber("1"));
		result.add(employeeService.findByBadgeNumber("1"));
		result.add(employeeService.findByBadgeNumber("1"));
		
		return result;
	}
	
}
