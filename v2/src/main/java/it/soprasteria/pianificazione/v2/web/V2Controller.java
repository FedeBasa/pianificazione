package it.soprasteria.pianificazione.v2.web;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import it.soprasteria.pianificazione.v2.bean.EmployeeBean;
import it.soprasteria.pianificazione.v2.bean.ProjectBean;
import it.soprasteria.pianificazione.v2.bean.RecordV2Bean;
import it.soprasteria.pianificazione.v2.bean.V2Bean;
import it.soprasteria.pianificazione.v2.service.CalendarConfigService;
import it.soprasteria.pianificazione.v2.service.EmployeeService;
import it.soprasteria.pianificazione.v2.service.EnumService;
import it.soprasteria.pianificazione.v2.service.ProjectService;
import it.soprasteria.pianificazione.v2.service.V2Service;
import it.soprasteria.pianificazione.v2.util.ColnameConverter;
import it.soprasteria.pianificazione.v2.util.DateUtil;
import it.soprasteria.pianificazione.v2.util.SessionHelper;
import it.soprasteria.pianificazione.v2.util.V2StatusKeys;
import it.soprasteria.pianificazione.v2.validator.FormValidator;
import it.soprasteria.pianificazione.v2.web.ajax.JsonResponse;

@Controller
public class V2Controller {

	private static final Logger LOG = Logger.getLogger(V2Controller.class);

	@Autowired
	private V2Service service;
	@Autowired
	private EmployeeService employeeService;
	@Autowired
	private ProjectService projectService;
	@Autowired
	private CalendarConfigService calendarConfigService;
	@Autowired
	private FormValidator formValidator;
	@Autowired
	private EnumService enumservice;

	@InitBinder(value = "v2Form")
	protected void initBinder(WebDataBinder binder) {
		binder.setValidator(formValidator);
	}

	@RequestMapping(value = "/home", method = RequestMethod.GET)
	public String home(Model model) {

		String username = SessionHelper.getUser().getUsername();

		model.addAttribute("lista", service.findByUser(username));

		return "home";
	}

	@RequestMapping(value = "/addMonth", method = RequestMethod.POST)
	public String addMonth(Model model, RedirectAttributes redirectAttributes) {

		boolean done = false;

		String user = SessionHelper.getUser().getUsername();
		LOG.info("user: " + user);

		done = service.addNextMonth(user);

		redirectAttributes.addFlashAttribute("rejected", !done);
		return "redirect:/home";

	}

	@RequestMapping(value = "/edit/v2", method = RequestMethod.GET)
	public ModelAndView method1(@RequestParam(required = true, name = "month") int month, @RequestParam(required = true, name = "bu") int businessUnit) throws SQLException {

		ModelAndView model = new ModelAndView();
		model.setViewName("index");

		String username = SessionHelper.getUser().getUsername();
		List<RecordV2Bean> list = new ArrayList<RecordV2Bean>();

		V2Bean v2Bean = service.findByMonth(month, businessUnit, username);
		SessionHelper.storeV2(v2Bean);

		list = service.getV2(month, businessUnit, SessionHelper.getUser().getUsername());
		model.addObject("list", list);
		RecordV2Bean recordV2Bean = new RecordV2Bean();
		recordV2Bean.setMonth(month);
		recordV2Bean.setBusinessUnit(businessUnit);
		model.addObject("v2Form", recordV2Bean);
		model.addObject("v2Bean", v2Bean);
		model.addObject("month", month);
		model.addObject("businessUnit", businessUnit);
		model.addObject("cons1", calendarConfigService.getConfig(DateUtil.addMonth(month, 0)));
		model.addObject("cons2", calendarConfigService.getConfig(DateUtil.addMonth(month, 1)));
		model.addObject("cons3", calendarConfigService.getConfig(DateUtil.addMonth(month, 2)));

		return model;
	}

	@RequestMapping(value = "/autocomplete/risorse", method = RequestMethod.GET)
	public @ResponseBody List<EmployeeBean> autocomplete() throws SQLException {

		List<EmployeeBean> result = employeeService.findAll();

		return result;
	}

	@RequestMapping(value = "/autocomplete/progetto", method = RequestMethod.GET)
	public @ResponseBody List<ProjectBean> autocompleta(@RequestParam(required = true, name = "bu") int businessUnit) {

		List<ProjectBean> result = projectService.findByBusinessUnit(businessUnit);
		return result;

	}

	@RequestMapping(value = "/record/detail/{id}", method = RequestMethod.GET)
	public @ResponseBody RecordV2Bean detail(@PathVariable(value = "id") Long id) throws SQLException {

		return service.getRecord(id);
	}

	@RequestMapping(value = "/record/update", method = RequestMethod.POST)
	public String modifyRecord(@ModelAttribute("v2Form") @Validated RecordV2Bean record, BindingResult result, Model model, RedirectAttributes redirectAttributes) {

		String errorPage = checkGeneralConditions(record.getMonth(), record.getBusinessUnit());
		if (errorPage != null) {
			return errorPage;
		}

		if (result.hasErrors()) {

			// ricarico la lista cos� nella jsp continuo a vedere la lista
			List<RecordV2Bean> list = new ArrayList<RecordV2Bean>();
			list = service.getV2(record.getMonth(), record.getBusinessUnit(), SessionHelper.getUser().getUsername());

			model.addAttribute("list", list);

			return "index";
		} else {

			record.setUserMod(SessionHelper.getUser().getUsername());

			service.updateRecord(record);

			redirectAttributes.addFlashAttribute("css", "success");
			redirectAttributes.addFlashAttribute("msg", "Record aggiornato!");

			return buildRedirectV2Edit(record.getMonth(), record.getBusinessUnit());
		}
	}

	@RequestMapping(value = "/record/insert", method = RequestMethod.POST)
	public String insertRecord(@ModelAttribute("v2Form") @Validated RecordV2Bean record, BindingResult result, Model model, RedirectAttributes redirectAttributes) {

		String errorPage = checkGeneralConditions(record.getMonth(), record.getBusinessUnit());
		if (errorPage != null) {
			return errorPage;
		}

		if (result.hasErrors()) {

			List<RecordV2Bean> list = new ArrayList<RecordV2Bean>();
			list = service.getV2(record.getMonth(), record.getBusinessUnit(), SessionHelper.getUser().getUsername());
			model.addAttribute("list", list);

			return "index";
		} else {

			record.setUserIns(SessionHelper.getUser().getUsername());
			service.insertRecord(record);

			return buildRedirectV2Edit(record.getMonth(), record.getBusinessUnit());
		}
	}

	@RequestMapping(value = "/record/delete", method = RequestMethod.POST)
	public String deleteRecord(@ModelAttribute("v2Form") RecordV2Bean record, BindingResult result, Model model, RedirectAttributes redirectAttributes) {

		String errorPage = checkGeneralConditions(record.getMonth(), record.getBusinessUnit());
		if (errorPage != null) {
			return errorPage;
		}
		
		Long id = record.getIdRecord();
		service.deleteRecord(id);

		return buildRedirectV2Edit(record.getMonth(), record.getBusinessUnit());
	}

	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public @ResponseBody JsonResponse newUpdate(@RequestParam(required = true, name = "month") int month, @RequestParam(required = true, name = "bu") int businessUnit, @RequestParam(required = true, name = "id") String id,
			@RequestParam(required = true, name = "colname") String colname, @RequestParam(name = "value") String data) {

		if (checkGeneralConditions(month, businessUnit) != null) {
			return JsonResponse.build(JsonResponse.CODE_INVALID_OPERATION, "Operazione non valida");
		}

		if (ColnameConverter.existsColname(colname)) {

			String realColname = ColnameConverter.convertColname(colname);

			if (realColname.equals("valuta") || realColname.equals("attivit�")) {
				if (!enumservice.getSet(realColname).contains(data)) {

					return JsonResponse.build(JsonResponse.CODE_INVALID_COLVALUE, "Valore non valido");

				} else {
					service.v2Update(Long.parseLong(id), realColname, data, SessionHelper.getUser().getUsername());

					return JsonResponse.build(JsonResponse.CODE_SUCCESS, "Aggiornamento effettuato correttamente");
				}

			} else {

				Integer value = 0;

				try {
					value = Integer.parseInt(data);
				} catch (NumberFormatException e) {
					return JsonResponse.build(JsonResponse.CODE_INVALID_COLVALUE, "Valore della colonna [" + colname + "] non valida");
				}

				boolean updated = service.v2Update(Long.parseLong(id), month, realColname, value, SessionHelper.getUser().getUsername());

				if (updated) {
					return JsonResponse.build(JsonResponse.CODE_SUCCESS, "Aggiornamento effettuato correttamente");
				}

				return JsonResponse.build(JsonResponse.CODE_INVALID_COLVALUE, "Valore  non valido ");

			}

		}
		return JsonResponse.build(JsonResponse.CODE_INVALID_COLNAME, "Colonna [" + colname + "] non valida");
	}

	@RequestMapping(value = "/valida", method = RequestMethod.POST)
	public String valida(@RequestParam(required = true, name = "month") int month, @RequestParam(required = true, name = "bu") int businessUnit, Model model, RedirectAttributes redirectAttributes) {

		String user = SessionHelper.getUser().getUsername();
		
		service.setValidateState(user, month, businessUnit);

		return buildRedirectV2Edit(month, businessUnit);
	}
	
	private String checkGeneralConditions(int month, int businessUnit) {
		
		V2Bean v2 = SessionHelper.getV2(month, businessUnit);
		if (v2 == null) {
			return "/error/403";
		}

		if (v2.getMonth() != month) {
			return "/error/403";
		}

		if (v2.getStato() != V2StatusKeys.OPEN) {
			return "/error/403";
		}

		return null;
	}
	
	private String buildRedirectV2Edit(int month, int businessUnit) {
		return "redirect:/edit/v2?month=" + month + "&bu=" + businessUnit;
	}
	
}