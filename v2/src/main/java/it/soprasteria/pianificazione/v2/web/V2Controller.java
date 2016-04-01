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
import it.soprasteria.pianificazione.v2.service.EnumService;
import it.soprasteria.pianificazione.v2.service.ProjectService;
import it.soprasteria.pianificazione.v2.service.V2Service;
import it.soprasteria.pianificazione.v2.util.ColnameConverter;
import it.soprasteria.pianificazione.v2.util.SessionHelper;
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
	private FormValidator formValidator;
	@Autowired
	private EnumService enumservice;

	@InitBinder(value="v2Form")
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
		int editable = service.getEditableState(username, month);
	//	int editable = 60;
		model.addObject("list", list);
		RecordV2Bean recordV2Bean = new RecordV2Bean();
		recordV2Bean.setMonth(month);
		recordV2Bean.setBusinessUnit(businessUnit);
		model.addObject("v2Form", recordV2Bean);
		model.addObject("v2Bean", v2Bean);
		model.addObject("editable", editable);
		model.addObject("month", month);
		model.addObject("businessUnit", businessUnit);

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

		LOG.debug("*********************************************************************************SONO QUI");

		return service.getRecord(id);
	}

	@RequestMapping(value = "/record/update", method = RequestMethod.POST)
	public String modifyRecord(@ModelAttribute("v2Form") @Validated RecordV2Bean record, BindingResult result, Model model, RedirectAttributes redirectAttributes) {

		try{
		V2Bean v2 = SessionHelper.getV2(record.getMonth(), record.getBusinessUnit());
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

		if (v2.getStato() == 100) {
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
			list = service.getV2(record.getMonth(), record.getBusinessUnit(), SessionHelper.getUser().getUsername());

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

			return buildRedirectV2Edit(record.getMonth(), record.getBusinessUnit());
		}
		}catch(NumberFormatException e){
			LOG.debug("ECCEZIONE" );
			return "redirect:/home";
		}
	}

	@RequestMapping(value = "/record/insert", method = RequestMethod.POST)
	public String insertRecord(@ModelAttribute("v2Form") @Validated RecordV2Bean record, BindingResult result, Model model, RedirectAttributes redirectAttributes) {
		LOG.debug("SONO NELL'INSERT");

		try{
		V2Bean v2 = SessionHelper.getV2(record.getMonth(), record.getBusinessUnit());
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

		if (v2.getStato() == 100) {
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
			list = service.getV2(record.getMonth(), record.getBusinessUnit(), SessionHelper.getUser().getUsername());
			model.addAttribute("list", list);

			return "index";
		} else {

			record.setUserIns(SessionHelper.getUser().getUsername());
			service.insertRecord(record);

			return buildRedirectV2Edit(record.getMonth(), record.getBusinessUnit());
		}
		}catch(NumberFormatException e){
			return "redirect:/home";
		}
	}

	private String buildRedirectV2Edit(int month, int businessUnit) {
		return "redirect:/edit/v2?month=" + month + "&bu=" + businessUnit;
	}

	@RequestMapping(value = "/record/delete", method = RequestMethod.POST)
	public String deleteRecord(@ModelAttribute("v2Form") RecordV2Bean record, BindingResult result, Model model, RedirectAttributes redirectAttributes) {

		LOG.debug("SONO NEL DELETE");
		try{
			V2Bean v2 = SessionHelper.getV2(record.getMonth(), record.getBusinessUnit());
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

		if (v2.getStato() == 100) {
			// TODO
			// return codice errore
			// l'utente prova a modificare un v2 non editabile
		}
		
		
          	Long id = record.getIdRecord();
        	service.deleteRecord(id);
        

		}catch(NullPointerException ex){
			return "redirect:/home";
	    }


		return buildRedirectV2Edit(record.getMonth(), record.getBusinessUnit());
	}
	

	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public @ResponseBody JsonResponse newUpdate(@RequestParam(required = true, name = "month") int month, @RequestParam(required = true, name = "bu") int businessUnit, 
			@RequestParam(required = true, name = "id") String id, @RequestParam(required = true, name = "colname") String colname, @RequestParam(name = "value") String data) {

		V2Bean v2 = SessionHelper.getV2(month, businessUnit);
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
		if (v2.getStato() == 100) {
			// TODO
			// return codice errore
			// l'utente prova a modificare un v2 non editabile
		}

		if (ColnameConverter.existsColname(colname)) {

			String realColname = ColnameConverter.convertColname(colname);

			LOG.debug("realcolname :" + realColname);
			LOG.debug("data :" + data);

			if (realColname.equals("valuta") || realColname.equals("attività")) {
				if (!enumservice.getSet(realColname).contains(data)) {

					return JsonResponse.build(JsonResponse.CODE_INVALID_COLVALUE, "Valore  non valido ");

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

}