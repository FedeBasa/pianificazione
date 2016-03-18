package it.soprasteria.pianificazione.v2.dao;

import java.util.List;

import it.soprasteria.pianificazione.v2.bean.EmployeeBean;
import it.soprasteria.pianificazione.v2.bean.ProjectBean;
import it.soprasteria.pianificazione.v2.bean.RecordV2Bean;

public interface Dao {
	public List<EmployeeBean> getAllEmployees();

	public List<ProjectBean> getAllProject(int businessUnit);

	public List<RecordV2Bean> getV2(int month, String user);

	public EmployeeBean getEmployee(String id);

	public ProjectBean getProject(long id);

	public RecordV2Bean getRecord(long id);

	public void update(RecordV2Bean rec);

	public void insert(RecordV2Bean rec);

	public void delete(long id);

	public List<RecordV2Bean> findAllV2();
	
	public List<Integer> getMonths(String user);
	
	public void addProjectsResources(String user, int currentMonth, int nextMonth);
	
	public void addNextMonth(String user);
	
	public void setEditable(String user, int month);

	public void persist(List<EmployeeBean> list);
	
	public void deleteAllEmployees();
	
	public void updateTable(Long id, String colname, Integer modify);
}
