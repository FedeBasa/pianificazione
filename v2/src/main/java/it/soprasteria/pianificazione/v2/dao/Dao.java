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

	public List<RecordV2Bean> getV2(int month, String user);

	public EmployeeBean getEmployee(String id);

	public ProjectBean getProject(long id);

	public RecordV2Bean getRecord(long id);

	public void update(RecordV2Bean rec);

	public void insert(RecordV2Bean rec);

	public void delete(long id);

	public List<Integer> getMonths(String user);
	
	public void addNextMonth(String user, Integer lastMonth);
	
	public void updateMonthsStatus(final int month, boolean enable);
	
	public void updateV2ConfigStatus(final int month, boolean enable);
	/*
	public List<V2Bean> getV2ToApprove(final String username);
	*/
	public List<V2Bean> getV2Config();
	
	public void updateEditable(String user, int month);

	public void persist(List<EmployeeBean> list);
	
	public void deleteAllEmployees();
	
	public void updateTable(Long id, String colname, Integer modify, String username);

	public V2Bean findByMonth(int month, String username);

	public List<V2Bean> findByUser(String username);
	
	public UserBean login(String username,String password);

}