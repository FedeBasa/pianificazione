package it.soprasteria.pianificazione.v2.service;

import it.soprasteria.pianificazione.v2.bean.EmployeeBean;

public class EmployeeService {

	public EmployeeBean findByBadgeNumber(String budgeNumber) {
		
		EmployeeBean bean = new EmployeeBean();
		bean.setBudgeNumber("00001");
		bean.setName("Nome");
		bean.setSurname("Cognome");
		
		return bean;
	}

}
