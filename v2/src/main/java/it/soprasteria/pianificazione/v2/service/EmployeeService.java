package it.soprasteria.pianificazione.v2.service;

import java.sql.SQLException;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.transaction.annotation.Transactional;

import it.soprasteria.pianificazione.v2.bean.EmployeeBean;
import it.soprasteria.pianificazione.v2.dao.Dao;

public class EmployeeService {

	private static final Logger LOG = Logger.getLogger(EmployeeService.class);

	@Autowired
	private Dao dao;

	@Cacheable(value = "employeeCache")
	public List<EmployeeBean> findAll() throws SQLException {

		return dao.getAllEmployees();
	}

	public EmployeeBean findById(String id) throws SQLException {
		return dao.getEmployee(id);
	}
	
	@Transactional
	public void save(List<EmployeeBean> list) {
		dao.persist(list);
	}

	@Transactional
	public void replace(List<EmployeeBean> list) {
		
		dao.deleteAllEmployees();
		
		dao.persist(list);
	}
}
