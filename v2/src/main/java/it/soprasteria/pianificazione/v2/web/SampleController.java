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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import it.soprasteria.pianificazione.v2.bean.EmployeeBean;
import it.soprasteria.pianificazione.v2.bean.ProjectBean;
import it.soprasteria.pianificazione.v2.bean.RecordV2Bean;
import it.soprasteria.pianificazione.v2.service.EmployeeService;
import it.soprasteria.pianificazione.v2.service.ProjectService;
import it.soprasteria.pianificazione.v2.service.V2Service;
import it.soprasteria.pianificazione.v2.util.SessionHelper;
import it.soprasteria.pianificazione.v2.validator.FormValidator;

@Controller
public class SampleController {

	private static final Logger LOG = Logger.getLogger(SampleController.class);

	@Autowired
	private V2Service service;
	@Autowired
	private EmployeeService employeeService;
	@Autowired
	private ProjectService projectService;
	@Autowired
	private FormValidator formValidator;

	@InitBinder
	protected void initBinder(WebDataBinder binder) {
		binder.setValidator(formValidator);
	}

	@RequestMapping(value = "/home", method = RequestMethod.GET)
	public String home(Model model) {
		
		// TODO 
		// manca parametro in input riguardo l'utente
		
        List<Integer> monthsList = new ArrayList<Integer>();
        String user = SessionHelper.getUser().getUsername();
        monthsList = service.getMonths(user);
            
        model.addAttribute("lista",monthsList);
        
		return "home";
	}

	@RequestMapping(value = "/addMonth", method = RequestMethod.POST)
	public String addMonth(Model model, RedirectAttributes redirectAttributes) {
		
		boolean addMonthRejected = false;
		
			String user = SessionHelper.getUser().getUsername();
			LOG.info("user: " + user);
			
			addMonthRejected = service.addNextMonth(user);
			
			redirectAttributes.addFlashAttribute("rejected", addMonthRejected);
			return "redirect:/home";
		
	}
	
	@RequestMapping(value = "/edit/v2", method = RequestMethod.GET)
	public ModelAndView method1(@RequestParam(required = false, name = "month") int month) throws SQLException {

		ModelAndView model = new ModelAndView();
		model.setViewName("index");
		
		String user = SessionHelper.getUser().getUsername();
		List<RecordV2Bean> list = new ArrayList<RecordV2Bean>();
		list = service.getV2(month, SessionHelper.getUser().getUsername());
		boolean editable = service.isEditable(user, month);
		
		model.addObject("list", list);
		model.addObject("v2Form", new RecordV2Bean());
		model.addObject("editable", editable);
		
		return model;
	}

	@RequestMapping(value = "/autocomplete/risorse", method = RequestMethod.GET)
	public @ResponseBody List<EmployeeBean> autocomplete() throws SQLException {

		// TODO
		// inserire parametro per filtrare sul servizio
		List<EmployeeBean> result = employeeService.findAll();

		return result;
	}

	@RequestMapping(value = "/autocomplete/progetto", method = RequestMethod.GET)
	public @ResponseBody List<ProjectBean> autocompleta(@RequestParam (name = "bu", required = false) int businessUnit) {
		
		// TODO
		// inserire parametro per filtrare sul servizio
		List<ProjectBean> result = projectService.findAll(businessUnit);
		return result;
	}

	@RequestMapping(value = "/table/edit", method = RequestMethod.GET)
	public @ResponseBody RecordV2Bean detail(int id) throws SQLException {
		
		LOG.debug("*********************************************************************************SONO QUI");
		
		return service.getRecord(id);
	}

	// era presente un errore nella firma del metodo
	@RequestMapping(value = "/send/data", method = RequestMethod.POST)
	public String modifyRecord(@ModelAttribute("v2Form") @Validated RecordV2Bean record, BindingResult result, Model model, RedirectAttributes redirectAttributes) {
		
		// il bean deve essere dichiarato @Validated
		// non bisogna invocare il metodo di validazione sul form fa Spring in automatico
		
		if (result.hasErrors()) {
			LOG.warn("EERRRRROOOOOOOREEEEEEE");
			
			// ricarico la lista così nella jsp continuo a vedere la lista
			List<RecordV2Bean> list = new ArrayList<RecordV2Bean>();
			list = service.getV2(record.getMonth(), SessionHelper.getUser().getUsername());
			
			model.addAttribute("list", list);
			
			return "index";
		} else {
			
			// TODO
			// provare a verificare se ritornando sulla pagina si riescono a gestire dopo la redirect
			redirectAttributes.addFlashAttribute("css", "success");
			redirectAttributes.addFlashAttribute("msg", "Record aggiornato!");
			
			service.updateRecord(record);

			return "redirect:/edit/v2?month=" + record.getMonth();
		}
	}

	@RequestMapping(value = "/send/insert", method = RequestMethod.POST)
	public String insertRecord(@ModelAttribute("v2Form") @Validated RecordV2Bean record, BindingResult result, Model model, RedirectAttributes redirectAttributes) {
		LOG.debug("SONO NELL'INSERT");

		if (result.hasErrors()) {
			LOG.debug("EERRRRROOOOOOOREEEEEEE " + result.getFieldError());
			
			// TODO
			// come per il metodo di modifica anche qui bisogna ricaricare la lista
			
			return "index";
		} else {

			service.insertRecord(record);

			return "redirect:/edit/v2?month=" + record.getMonth();
		}
	}

	@RequestMapping(value = "/send/delete", method = RequestMethod.POST)
	public String deleteRecord(@ModelAttribute("v2Form") RecordV2Bean record, BindingResult result, Model model, RedirectAttributes redirectAttributes) {
		LOG.debug("SONO NEL DELETE");
       long id = record.getIdRecord();
	   service.deleteRecord(id);
		
		return "redirect:/edit/v2?month=" + record.getMonth();
	}
	
}
