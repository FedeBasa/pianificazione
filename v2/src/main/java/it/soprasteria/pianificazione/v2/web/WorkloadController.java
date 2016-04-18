package it.soprasteria.pianificazione.v2.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import it.soprasteria.pianificazione.v2.bean.WorkloadBean;
import it.soprasteria.pianificazione.v2.bean.WorkloadDetailBean;
import it.soprasteria.pianificazione.v2.service.CalendarConfigService;
import it.soprasteria.pianificazione.v2.service.WorkloadService;
import it.soprasteria.pianificazione.v2.util.DateUtil;
import it.soprasteria.pianificazione.v2.util.SessionHelper;

@Controller
public class WorkloadController {

	@Autowired
	private WorkloadService service;
	@Autowired
	private CalendarConfigService calendarConfigService;
	
	@RequestMapping(value = "/workload", method = RequestMethod.GET)
	public ModelAndView home(@RequestParam(required = true, name = "month") int month, @RequestParam(required = true, name = "bu") int businessUnit) {

		String username = SessionHelper.getUser().getUsername();
		
		List<WorkloadBean> list = service.findWorkload(month, username);
		
		ModelAndView model = new ModelAndView();
		model.setViewName("workload_resources");
		
		model.addObject("list", list);
		model.addObject("month", month);
		model.addObject("businessUnit", businessUnit);
		
		model.addObject("cons1", calendarConfigService.getConfig(DateUtil.addMonth(month, 0)));
		model.addObject("cons2", calendarConfigService.getConfig(DateUtil.addMonth(month, 1)));
		model.addObject("cons3", calendarConfigService.getConfig(DateUtil.addMonth(month, 2)));

		model.addObject("currentMonth", DateUtil.getMonthName(month));
		model.addObject("nextMonth", DateUtil.getMonthName(month, 1));
		model.addObject("lastMonth", DateUtil.getMonthName(month, 2));
		
		return model;
	}
	
	@RequestMapping(value = "/workload/detail/{badgeNumber}", method = RequestMethod.GET)
	public ModelAndView home(@PathVariable(value="badgeNumber") String badgeNumber, @RequestParam(required = true, name = "month") int month) {

		String username = SessionHelper.getUser().getUsername();
		
		List<WorkloadDetailBean> list = service.findWorkloadDetail(month, badgeNumber);
		
		ModelAndView model = new ModelAndView();
		model.setViewName("workload_details");
		
		model.addObject("list", list);
		model.addObject("month", month);
		
		model.addObject("cons1", calendarConfigService.getConfig(DateUtil.addMonth(month, 0)));
		model.addObject("cons2", calendarConfigService.getConfig(DateUtil.addMonth(month, 1)));
		model.addObject("cons3", calendarConfigService.getConfig(DateUtil.addMonth(month, 2)));
		
		model.addObject("currentMonth", DateUtil.getMonthName(month));
		model.addObject("nextMonth", DateUtil.getMonthName(month, 1));
		model.addObject("lastMonth", DateUtil.getMonthName(month, 2));		
		
		return model;
	}

}
