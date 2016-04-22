package it.soprasteria.pianificazione.v2.web;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;

import it.soprasteria.pianificazione.v2.bean.upload.UploadEmployeeBean;
import it.soprasteria.pianificazione.v2.digester.ExcelEmployeeDigester;
import it.soprasteria.pianificazione.v2.exception.DigestException;
import it.soprasteria.pianificazione.v2.service.EmployeeService;
import it.soprasteria.pianificazione.v2.util.SessionHelper;

@Controller
public class UploadController {

	private static final Logger LOG = Logger.getLogger(UploadController.class);
	
	@Autowired
	private EmployeeService service;
	
	@RequestMapping(value = "/excel/upload/employee", method = RequestMethod.GET)
	public String view(Model model) {
		
		model.addAttribute("uploadBean", new UploadEmployeeBean());
		
		return "upload_risorse";
	}
	
	@RequestMapping(value = "/excel/upload/employee", method = RequestMethod.POST)
	public String uploadEmployee(@ModelAttribute("uploadBean") UploadEmployeeBean uploadBean, Model model) throws DigestException, IOException {

		
		MultipartFile multipartFile = uploadBean.getFile();
		
		ExcelEmployeeDigester digester = new ExcelEmployeeDigester();
		
		digester.load(multipartFile.getInputStream());
		
		digester.validate(SessionHelper.getUser().getUsername());
		
		model.addAttribute("digester", digester);
		
		SessionHelper.storeEmployeeDigester(digester);
			
		return "upload_risorse";
	}
	
	@RequestMapping(value = "/excel/save/employee", method = RequestMethod.POST)
	public String saveEmployee() {
	
		ExcelEmployeeDigester digester = SessionHelper.getEmployeeDigester();
		
		service.save(digester.getList());

		SessionHelper.clearEmployeeDigester();
		
		return "redirect:/excel/upload/employee";
	}	

	@RequestMapping(value = "/excel/replace/employee", method = RequestMethod.POST)
	public String replaceEmployee() {
	
		ExcelEmployeeDigester digester = SessionHelper.getEmployeeDigester();
		
		service.replace(digester.getList());
		
		SessionHelper.clearEmployeeDigester();

		return "redirect:/excel/upload/employee";
	}	

}