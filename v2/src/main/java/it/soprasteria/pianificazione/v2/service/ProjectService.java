package it.soprasteria.pianificazione.v2.service;

import java.sql.SQLException;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.transaction.annotation.Transactional;

import it.soprasteria.pianificazione.v2.bean.ProjectBean;
import it.soprasteria.pianificazione.v2.dao.Dao;

public class ProjectService {

	@Autowired
	private Dao dao;
	private static final Logger LOG = Logger.getLogger(EmployeeService.class);

	@Cacheable(value = "projectCache")
	public List<ProjectBean> findAll() {
		return dao.getAllProject();

	}
	
	@Cacheable(value = "projectCache")
	public List<ProjectBean> findByBusinessUnit(int businessUnit) {
		return dao.findProjectsByBusinessUnit(businessUnit);

	}
	
	@CacheEvict(value = "projectCache")
	public void clearCache() {
		// clear ehcache, no implementation is required (see the annotation)
	}

	public ProjectBean details(long id) throws SQLException {
		return dao.getProject(id);
	}
	
	@Transactional
	public void  save(List<ProjectBean> list){
        
		dao.persistProject(list);
		
	}
	
	
	@Transactional
	public void replace(List<ProjectBean> list) {
		
		dao.deleteAllProjects();
		
		dao.persistProject(list);
	}
}
