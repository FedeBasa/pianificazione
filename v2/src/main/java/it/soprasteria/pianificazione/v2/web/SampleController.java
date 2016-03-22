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
import it.soprasteria.pianificazione.v2.service.EmployeeService;
import it.soprasteria.pianificazione.v2.service.ProjectService;
import it.soprasteria.pianificazione.v2.service.V2Service;
import it.soprasteria.pianificazione.v2.util.ColnameConverter;
import it.soprasteria.pianificazione.v2.util.SessionHelper;
import it.soprasteria.pianificazione.v2.validator.FormValidator;
import it.soprasteria.pianificazione.v2.web.ajax.JsonResponse;

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
	public ModelAndView method1(@RequestParam(required = false, name = "month") int month) throws SQLException {

		ModelAndView model = new ModelAndView();
		model.setViewName("index");

		String username = SessionHelper.getUser().getUsername();
		List<RecordV2Bean> list = new ArrayList<RecordV2Bean>();

		V2Bean v2Bean = service.findByMonth(month, username);
		SessionHelper.storeV2(v2Bean);

		list = service.getV2(month, SessionHelper.getUser().getUsername());

		model.addObject("list", list);
		model.addObject("v2Form", new RecordV2Bean());
		model.addObject("editable", (v2Bean.getEditable()));
		model.addObject("month", month);

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
	public @ResponseBody List<ProjectBean> autocompleta(@RequestParam(name = "bu", required = false) Integer businessUnit) {

		// TODO
		// inserire parametro per filtrare sul servizio
		List<ProjectBean> result = projectService.findAll(businessUnit);
		return result;
	}

	 @RequestMapping(value = "/record/detail/{id}", method = RequestMethod.GET)
	 public @ResponseBody RecordV2Bean detail(@PathVariable(value="id") Long id) throws SQLException {
	
		LOG.debug("*********************************************************************************SONO QUI");
		
		return service.getRecord(id);
	}

	@RequestMapping(value = "/record/update", method = RequestMethod.POST)
	public String modifyRecord(@ModelAttribute("v2Form") @Validated RecordV2Bean record, BindingResult result, Model model, RedirectAttributes redirectAttributes) {

		V2Bean v2 = SessionHelper.getV2();
		if (v2 == null) {
			// TODO
			// return codice errore http permission denied
		}

		if (v2.getMonth() != record.getMonth()) {
			// TODO
			// return codice errore
			// l'utente sta cercando di modificare un mese diverso da quello che
			// è in sessione
		}
		
		if (v2.getEditable()) {
			// TODO
			// return codice errore
			// l'utente prova a modificare un v2 non editabile
		}

		// il bean deve essere dichiarato @Validated
		// non bisogna invocare il metodo di validazione sul form fa Spring in
		// automatico

		if (result.hasErrors()) {
			LOG.warn("EERRRRROOOOOOOREEEEEEE");

			// ricarico la lista così nella jsp continuo a vedere la lista
			List<RecordV2Bean> list = new ArrayList<RecordV2Bean>();
			list = service.getV2(record.getMonth(), SessionHelper.getUser().getUsername());

			model.addAttribute("list", list);

			return "index";
		} else {

			record.setUserMod(SessionHelper.getUser().getUsername());
			
			service.updateRecord(record);

			// TODO
			// provare a verificare se ritornando sulla pagina si riescono a
			// gestire dopo la redirect
			redirectAttributes.addFlashAttribute("css", "success");
			redirectAttributes.addFlashAttribute("msg", "Record aggiornato!");


			return "redirect:/edit/v2?month=" + record.getMonth();
		}
	}

	@RequestMapping(value = "/record/insert", method = RequestMethod.POST)
	public String insertRecord(@ModelAttribute("v2Form") @Validated RecordV2Bean record, BindingResult result, Model model, RedirectAttributes redirectAttributes) {
		LOG.debug("SONO NELL'INSERT");

		V2Bean v2 = SessionHelper.getV2();
		if (v2 == null) {
			// TODO
			// return codice errore http permission denied
		}

		if (v2.getMonth() != record.getMonth()) {
			// TODO
			// return codice errore
			// l'utente sta cercando di modificare un mese diverso da quello che
			// è in sessione
		}
		
		if (v2.getEditable()) {
			// TODO
			// return codice errore
			// l'utente prova a modificare un v2 non editabile
		}

		if (result.hasErrors()) {
			LOG.debug("EERRRRROOOOOOOREEEEEEE " + result.getFieldError());
			// TODO
			// come per il metodo di modifica anche qui bisogna ricaricare la
			// lista
			List<RecordV2Bean> list = new ArrayList<RecordV2Bean>();
			list = service.getV2(record.getMonth(), SessionHelper.getUser().getUsername());
			model.addAttribute("list", list);

			return "index";
		} else {

			record.setUserIns(SessionHelper.getUser().getUsername());
			service.insertRecord(record);

			return "redirect:/edit/v2?month=" + record.getMonth();
		}
	}

	@RequestMapping(value = "/record/delete", method = RequestMethod.POST)
	public String deleteRecord(@ModelAttribute("v2Form") RecordV2Bean record, BindingResult result, Model model, RedirectAttributes redirectAttributes) {

		LOG.debug("SONO NEL DELETE");

		V2Bean v2 = SessionHelper.getV2();
		if (v2 == null) {
			// TODO
			// return codice errore http permission denied
		}

		if (v2.getMonth() != record.getMonth()) {
			// TODO
			// return codice errore
			// l'utente sta cercando di modificare un mese diverso da quello che
			// è in sessione
		}
		
		if (v2.getEditable()) {
			// TODO
			// return codice errore
			// l'utente prova a modificare un v2 non editabile
		}

		Long id = record.getIdRecord();
		service.deleteRecord(id);

		return "redirect:/edit/v2?month=" + record.getMonth();
	}

	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public @ResponseBody JsonResponse newUpdate(@RequestParam(name = "id") String id, @RequestParam(name = "colname") String colname, @RequestParam(name = "value") String data) {

		V2Bean v2 = SessionHelper.getV2();
		if (v2 == null) {
			// TODO
			// return codice errore http permission denied
		}

		// TODO
		// passare anche id del mese per controllo coerenza
		// if(v2.getMonth() != record.getMonth()) {
		// TODO
		// return codice errore
		// l'utente sta cercando di modificare un mese diverso da quello che è
		// in sessione
		// }
		if (v2.getEditable()) {
			// TODO
			// return codice errore
			// l'utente prova a modificare un v2 non editabile
		}

		if (ColnameConverter.existsColname(colname)) {

			String realColname = ColnameConverter.convertColname(colname);
			Integer value = 0;
			try {
				value = Integer.parseInt(data);
			} catch (NumberFormatException e) {
				return JsonResponse.build(JsonResponse.CODE_INVALID_COLVALUE, "Valore della colonna [" + colname + "] non valida");
			}

			service.v2Update(Long.parseLong(id), realColname, value, SessionHelper.getUser().getUsername());

			return JsonResponse.build(JsonResponse.CODE_SUCCESS, "Aggiornamento effettuato correttamente");
		}

		return JsonResponse.build(JsonResponse.CODE_INVALID_COLNAME, "Colonna [" + colname + "] non valida");
	}

	@RequestMapping(value = "/approva", method = RequestMethod.POST)
	public String approva(@RequestParam int month, Model model, RedirectAttributes redirectAttributes) {

		String user = SessionHelper.getUser().getUsername();
		service.updateEditable(user, month);
		return "redirect:/approvaPage";
	}

	@RequestMapping(value = "/approvaPage", method = RequestMethod.GET)
	public String approvaPage(Model model, RedirectAttributes redirectAttributes) {
	    
		String user = SessionHelper.getUser().getUsername();
		List<V2Bean> v2List = service.getV2ToApprove(user);
		model.addAttribute("v2List",v2List);
		
		return "approva_mese";
	}
}