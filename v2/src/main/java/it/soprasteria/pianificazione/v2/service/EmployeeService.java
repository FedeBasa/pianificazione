package it.soprasteria.pianificazione.v2.service;

import java.sql.SQLException;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;

import it.soprasteria.pianificazione.v2.bean.EmployeeBean;
import it.soprasteria.pianificazione.v2.dao.Dao;

public class EmployeeService {

	@Autowired
	private Dao dao;
	private static final Logger LOG = Logger.getLogger(EmployeeService.class);

	public EmployeeBean findByBadgeNumber(String badgeNumber) {

		EmployeeBean bean = new EmployeeBean();
		bean.setBadgeNumber("00001");
		bean.setName("Nome");
		bean.setSurname("Cognome");

		return bean;
	}

	@Cacheable(value = "employeeCache")
	public List<EmployeeBean> findAll() throws SQLException {
		LOG.debug("RISORSE AGGIUNTE");
		return dao.getAllEmployees();
	}

	public EmployeeBean details(String id) throws SQLException {
		return dao.getEmployee(id);
	}
	
	public void save(List<EmployeeBean> list) {
		
		
		// TODO
		// richiamare il DAO
	}
}
