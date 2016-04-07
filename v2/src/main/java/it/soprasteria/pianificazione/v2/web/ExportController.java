package it.soprasteria.pianificazione.v2.web;



import java.io.IOException;
import java.text.ParseException;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import it.soprasteria.pianificazione.v2.bean.RecordV2Bean;
import it.soprasteria.pianificazione.v2.dao.DaoImpl;
import it.soprasteria.pianificazione.v2.service.ExportV2Service;
import it.soprasteria.pianificazione.v2.service.V2Service;
import it.soprasteria.pianificazione.v2.util.SessionHelper;
@Controller
public class ExportController {

	@Autowired
	private ExportV2Service service;
	@Autowired
	private V2Service v2Service;
	
	private static final Logger LOG = Logger.getLogger(DaoImpl.class);
	
	@RequestMapping(value = "/export/v2", method = RequestMethod.GET)
		public void export(HttpServletResponse response, @RequestParam (name = "month" , required= true)int month,@RequestParam (name = "bu" , required= true) int bu) throws IOException, InvalidFormatException, ParseException{
		
			String user = SessionHelper.getUser().getUsername();
			
			List<RecordV2Bean> record = v2Service.getV2(month, bu, user);
			
			byte[] bytes = service.export(record);
			
			response.setContentType("application/vnd.ms-excel");
			response.setHeader("Content-Disposition", String.format("inline; filename=\"" + user + "_" + month + "_" + bu + ".xlsx\""));
			
			ServletOutputStream outputStream = response.getOutputStream();
			outputStream.write(bytes);
			
			outputStream.close();
		}
}