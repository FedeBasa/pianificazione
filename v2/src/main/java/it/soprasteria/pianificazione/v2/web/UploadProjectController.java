package it.soprasteria.pianificazione.v2.web;

import java.io.IOException;

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
	
	
	@Autowired
	private ProjectService projectService;
	

	@RequestMapping(value = "/excel/upload/project", method = RequestMethod.GET)
	public String projectView(Model model) {
		
		model.addAttribute("uploadProjectBean", new UploadProjectBean());
		
		return "upload_progetti";
	}
	
	@RequestMapping(value = "/excel/upload/project", method = RequestMethod.POST)
	public String uploadProject(@ModelAttribute("uploadProjectBean") UploadProjectBean uploadProjectBean, BindingResult result, Model model, RedirectAttributes redirectAttributes) {

		
		MultipartFile multipartFile = uploadProjectBean.getFile();
		
		ExcelProjectDigester digester = new ExcelProjectDigester();
		
		try {
		
			digester.load(multipartFile.getInputStream());
			
			digester.validate();
			
			model.addAttribute("digester", digester);
			
			SessionHelper.storeProjectDigester(digester);
			
		} catch(DigestException | IOException e) {

			// TODO
			// gestire correttamente, aggiungere messaggio da visualizzare a video
		
		}
		
		return "upload_progetti";
	}
	@RequestMapping(value = "/excel/save/project", method = RequestMethod.POST)
	public String saveProject(Model model) {
	
		ExcelProjectDigester digester = SessionHelper.getProjectDigester();
		
		projectService.save(digester.getList());
		
		SessionHelper.clearProjectDigester();
		
		// TODO
		// aggiungere messaggio di conferma operazione
		
		return "redirect:/excel/upload/project";
	}	

	@RequestMapping(value = "/excel/replace/project", method = RequestMethod.POST)
	public String replaceProject(Model model) {
	
		ExcelProjectDigester digester = SessionHelper.getProjectDigester();
		
		projectService.replace(digester.getList());
		
		SessionHelper.clearProjectDigester();
		
		// TODO
		// aggiungere messaggio di conferma operazione

		return "redirect:/excel/upload/project";
	}	
	
}
