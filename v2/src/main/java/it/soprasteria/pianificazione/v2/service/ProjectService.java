package it.soprasteria.pianificazione.v2.service;

import java.sql.SQLException;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;

import it.soprasteria.pianificazione.v2.bean.ProjectBean;
import it.soprasteria.pianificazione.v2.dao.Dao;

public class ProjectService {

	@Autowired
	private Dao dao;
	private static final Logger LOG = Logger.getLogger(EmployeeService.class);

	public ProjectBean findById(long projectId) {

		ProjectBean bean = new ProjectBean();

		bean.setIdProject(1);
		bean.setDescription("Desc progetto");
		bean.setCustomer("Customer");
		bean.setType("Forfait");
		bean.setCurrency("EUR");

		return bean;
	}

	@Cacheable(value = "projectCache")
	public List<ProjectBean> findAll() {
		LOG.debug("RISORSE AGGIUNTE");
		return dao.getAllProject();

	}

	public ProjectBean details(long id) throws SQLException {
		return dao.getProject(id);
	}
}
