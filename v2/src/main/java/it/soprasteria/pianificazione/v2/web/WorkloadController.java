package it.soprasteria.pianificazione.v2.web;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import it.soprasteria.pianificazione.v2.bean.WorkloadBean;
import it.soprasteria.pianificazione.v2.bean.WorkloadDetailBean;
import it.soprasteria.pianificazione.v2.service.CalendarConfigService;
import it.soprasteria.pianificazione.v2.service.WorkloadService;
import it.soprasteria.pianificazione.v2.util.ColnameConverter;
import it.soprasteria.pianificazione.v2.util.DateUtil;
import it.soprasteria.pianificazione.v2.util.SessionHelper;
import it.soprasteria.pianificazione.v2.web.ajax.JsonResponse;
import it.soprasteria.pianificazione.v2.web.ajax.UpdateFerieJsonResponse;

@Controller
public class WorkloadController {

	private static final Logger LOG = Logger.getLogger(WorkloadController.class);
	
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

	@RequestMapping(value = "/updateWorkload", method = RequestMethod.POST)
	public @ResponseBody JsonResponse newUpdate(@RequestParam(required = true, name = "month") int month, @RequestParam(required = true, name = "bu") int businessUnit,
			@RequestParam(required = true, name = "colname") String colname, @RequestParam(name = "value") String data,  @RequestParam(name = "badgeNumber") String badgeNumber) {
		
		if (ColnameConverter.existsColname(colname)) {

			String realColname = ColnameConverter.convertColname(colname);

			if(!colname.startsWith("ferie")) {
				return JsonResponse.build(JsonResponse.CODE_INVALID_COLVALUE, "Valore dell colonna [" + realColname + "] non valido");
			}
			
			String username = SessionHelper.getUser().getUsername();
					
			Integer value = 0;
	
			try {
				value = Integer.parseInt(data);
			} catch (NumberFormatException e) {
				return JsonResponse.build(JsonResponse.CODE_INVALID_COLVALUE, "Valore della colonna [" + realColname + "] non valida");
			}
			
			WorkloadBean bean = service.workloadUpdate(month, realColname, value, username, badgeNumber);
			
			if(bean != null) {
				return UpdateFerieJsonResponse.build(JsonResponse.CODE_SUCCESS, "Aggiornamento effettuato correttamente", bean);
			}
		}
		
		return JsonResponse.build(JsonResponse.CODE_INVALID_COLNAME, "Colonna [" + colname + "] non valida");	
	}
}
