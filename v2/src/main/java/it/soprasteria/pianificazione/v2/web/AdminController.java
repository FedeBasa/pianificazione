package it.soprasteria.pianificazione.v2.web;

import java.io.IOException;
import java.text.ParseException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import it.soprasteria.pianificazione.v2.bean.ImpersonateBean;
import it.soprasteria.pianificazione.v2.bean.RecordV2Bean;
import it.soprasteria.pianificazione.v2.bean.UserBean;
import it.soprasteria.pianificazione.v2.bean.V2Bean;
import it.soprasteria.pianificazione.v2.service.AdminService;
import it.soprasteria.pianificazione.v2.service.ExportV2Service;
import it.soprasteria.pianificazione.v2.service.UserService;
import it.soprasteria.pianificazione.v2.service.V2Service;
import it.soprasteria.pianificazione.v2.util.SessionHelper;
import it.soprasteria.pianificazione.v2.util.V2StatusKeys;

@Controller
public class AdminController {

	private static final Logger LOG = Logger.getLogger(AdminController.class);
	
	@Autowired
	private AdminService service;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private V2Service v2Service;

	@Autowired
	private ExportV2Service exportV2Service;

	@RequestMapping(value = "/admin/apri", method = RequestMethod.POST)
	public String apri(@RequestParam int month, Model model, RedirectAttributes redirectAttributes) {

		service.updateV2ConfigStatus(month, V2StatusKeys.OPEN);
		service.updateMonthsStatus(month, V2StatusKeys.OPEN);
		return "redirect:/admin/gestione_mese";
	}
	
	@RequestMapping(value = "/admin/chiudi", method = RequestMethod.POST)
	public String chiudi(@RequestParam int month, Model model, RedirectAttributes redirectAttributes) {

		service.updateV2ConfigStatus(month, V2StatusKeys.CLOSE);
		service.updateMonthsStatus(month, V2StatusKeys.CLOSE);
		return "redirect:/admin/gestione_mese";
	}

	@RequestMapping(value = "/admin/gestione_mese", method = RequestMethod.GET)
	public String approvaPage(Model model, RedirectAttributes redirectAttributes) {

		List<V2Bean> v2List = service.getV2Config();
		model.addAttribute("v2List",v2List);
		
		return "admin_gestione_mese";
	}
	
	@RequestMapping(value = "/admin/addConfigMonth", method = RequestMethod.POST)
	public String addMonth(Model model, RedirectAttributes redirectAttributes) {

		service.addNextConfigMonth();

		return "redirect:/admin/gestione_mese";
	}
	
	@RequestMapping(value = "/admin/detail", method = RequestMethod.GET)
	public String view(@RequestParam (name = "month" , required= true)int month, Model model) {
		
		List<UserBean> list = userService.findByProfilo("pm");
		Set<String> checkList = new HashSet<String>();
		for(UserBean bean : list) {
			List<V2Bean> v2List = v2Service.findByUser(bean.getUsername());
			for(V2Bean v2 : v2List) {
				if (v2.getMonth() == month) {
					checkList.add(bean.getUsername());
					break;
				}
			}
		}
		
		model.addAttribute("list", list);
		model.addAttribute("checklist", checkList);
		
		return "admin_dettaglio_mese";
	}
	
	@RequestMapping(value = "/admin/impersonate", method = RequestMethod.GET)
	public String impersonate(@RequestParam (name = "username" , required= true)String username) {

		UserBean sessionUser = SessionHelper.getUser();
		
		UserBean impersonateUser = userService.findByUsername(username);
		if (impersonateUser == null) {
			return "/error/403";
		}
		
		sessionUser.setUsername(username);
		SessionHelper.storeImpersonateUser(ImpersonateBean.build(impersonateUser));
		
		return "redirect:/home";
	}

	@RequestMapping(value = "/admin/impersonate/stop", method = RequestMethod.GET)
	public String stopImpersonate() {

		UserBean user = SessionHelper.getUser();
		user.setUsername(user.getRealUsername());
		
		SessionHelper.clearImpersonateUser();
		return "redirect:/admin/gestione_mese";
	}

	@RequestMapping(value = "/admin/export/v2", method = RequestMethod.GET)
	public void export(HttpServletResponse response, @RequestParam(name = "month", required = true) int month, @RequestParam(name = "bu", required = true) int bu) throws IOException, InvalidFormatException, ParseException {

		List<RecordV2Bean> record = v2Service.getV2(month, bu, null);

		byte[] bytes = exportV2Service.export(record);

		response.setContentType("application/vnd.ms-excel");
		response.setHeader("Content-Disposition", "inline; filename=\"" + month + "_" + bu + ".xlsx\"");

		ServletOutputStream outputStream = response.getOutputStream();
		outputStream.write(bytes);

		outputStream.close();
	}

}