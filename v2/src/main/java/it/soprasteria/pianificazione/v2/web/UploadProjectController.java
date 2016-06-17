package it.soprasteria.pianificazione.v2.web;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import it.soprasteria.pianificazione.v2.bean.upload.UploadProjectBean;
import it.soprasteria.pianificazione.v2.digester.ExcelProjectDigester;
import it.soprasteria.pianificazione.v2.exception.DigestException;
import it.soprasteria.pianificazione.v2.service.ProjectService;
import it.soprasteria.pianificazione.v2.util.SessionHelper;

@Controller
public class UploadProjectController {
	
	private static final Logger LOG = Logger.getLogger(UploadProjectController.class);
	
	@Autowired
	private ProjectService projectService;
	

	@RequestMapping(value = "/admin/excel/upload/project", method = RequestMethod.GET)
	public String projectView(Model model) {
		
		model.addAttribute("uploadProjectBean", new UploadProjectBean());
		
		model.addAttribute("projectNumber", projectService.findAll().size());
		
		return "admin_upload_progetti";
	}
	
	@RequestMapping(value = "/admin/excel/upload/project", method = RequestMethod.POST)
	public String uploadProject(@ModelAttribute("uploadProjectBean") UploadProjectBean uploadProjectBean, Model model) throws DigestException, IOException {

		
		MultipartFile multipartFile = uploadProjectBean.getFile();
		
		ExcelProjectDigester digester = new ExcelProjectDigester();
		
		digester.load(multipartFile.getInputStream());
		
		digester.validate(SessionHelper.getUser().getUsername());
		
		model.addAttribute("digester", digester);
		
		SessionHelper.storeProjectDigester(digester);
		
		model.addAttribute("projectNumber", projectService.findAll().size());
			
		return "admin_upload_progetti";
	}
	
	@RequestMapping(value = "/admin/excel/save/project", method = RequestMethod.POST)
	public String saveProject() {
	
		ExcelProjectDigester digester = SessionHelper.getProjectDigester();
		
		projectService.clearCache();
		projectService.save(digester.getList());
		
		SessionHelper.clearProjectDigester();
		   
		return "redirect:/admin/excel/upload/project";
	}	

	@RequestMapping(value = "/admin/excel/replace/project", method = RequestMethod.POST)
	public String replaceProject() {

		ExcelProjectDigester digester = SessionHelper.getProjectDigester();
		
		projectService.clearCache();
		projectService.replace(digester.getList());
		
		SessionHelper.clearProjectDigester();
		
		// TODO
		// aggiungere messaggio di conferma operazione

		return "redirect:/admin/excel/upload/project";
	}	
	
}