package it.soprasteria.pianificazione.v2.service;

import it.soprasteria.pianificazione.v2.bean.ProjectBean;

public class ProjectService {

	public ProjectBean findById(long projectId) {
		
		ProjectBean bean = new ProjectBean();
		
		bean.setIdProject(1);
		bean.setDescription("Desc progetto");
		bean.setCustomer("Customer");
		bean.setType("Forfait");
		bean.setCurrency("EUR");
		
		return bean;
	}
	
}
