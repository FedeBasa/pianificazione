package it.soprasteria.pianificazione.v2.dao;

import java.util.List;

import it.soprasteria.pianificazione.v2.bean.EmployeeBean;
import it.soprasteria.pianificazione.v2.bean.ProjectBean;
import it.soprasteria.pianificazione.v2.bean.RecordV2Bean;

public interface Dao {
	public List<EmployeeBean> getAllEmployees();

	public List<ProjectBean> getAllProject(Integer businessUnit);

	public List<RecordV2Bean> getV2(String month, String user);

	public EmployeeBean getEmployee(String id);

	public ProjectBean getProject(Long id);

	public RecordV2Bean getRecord(Long id);

	public void update(RecordV2Bean rec);

	public void insert(RecordV2Bean rec);

	public void delete(Long id);

	public List<RecordV2Bean> findAllV2();

	public void persist(List<EmployeeBean> list);
	
	public void deleteAllEmployees();

}
