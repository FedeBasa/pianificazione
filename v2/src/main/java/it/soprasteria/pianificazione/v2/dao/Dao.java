package it.soprasteria.pianificazione.v2.dao;

import java.util.List;

import it.soprasteria.pianificazione.v2.bean.EmployeeBean;
import it.soprasteria.pianificazione.v2.bean.ProjectBean;
import it.soprasteria.pianificazione.v2.bean.RecordV2Bean;
import it.soprasteria.pianificazione.v2.bean.UserBean;
import it.soprasteria.pianificazione.v2.bean.V2Bean;

public interface Dao {
	
	public List<EmployeeBean> getAllEmployees();

	public List<ProjectBean> getAllProject();
	
	public List<ProjectBean> findProjectsByBusinessUnit(int businessUnit);

	public List<RecordV2Bean> getV2(int month, int businessUnit, String user);

	public EmployeeBean getEmployee(String id);

	public ProjectBean getProject(long id);

	public RecordV2Bean getRecord(long id);

	public void update(RecordV2Bean rec);

	public void insert(RecordV2Bean rec);

	public void delete(long id);

	public List<Integer> getMonths(String user);
	
	public void addNextMonth(String user, Integer lastMonth);
	
	public void persist(List<EmployeeBean> list);
	
	public void deleteAllEmployees();
	
	public void updateTable(Long id, String colname, Integer modify, String username);
	
	public void updateTable(Long id, String colname, String value, String username);
	
	public V2Bean findByMonth(int month, int businessUnit, String username);

	public List<V2Bean> findByUser(String username);
	
	public int getTotDays(int month);
	
	public int getConsDays(String badgeNumber,String colname, int mese);
	
	public void setValidateState(String user, int month, int businessUnit);
	
	public void persistProject(List<ProjectBean> list);

	public void deleteAllProjects();
	
	public int checkChangePassword(String userid);
	
	public UserBean findByUsername(final String username);
	
	public void produceAll(String user, int month, int businessUnit);

}