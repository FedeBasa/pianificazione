package it.soprasteria.pianificazione.v2.service;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import it.soprasteria.pianificazione.v2.bean.EmployeeBean;
import it.soprasteria.pianificazione.v2.bean.ProjectBean;
import it.soprasteria.pianificazione.v2.bean.RecordV2Bean;
import it.soprasteria.pianificazione.v2.bean.V2Bean;
import it.soprasteria.pianificazione.v2.dao.DaoImpl;

public class V2Service {
	private static final Logger LOG = Logger.getLogger(V2Service.class);

	@Autowired
	private DaoImpl dao;

	public V2Bean findByMonth(int month, String username) {
		
		return dao.findByMonth(month, username);
	}
	
	
	public List<RecordV2Bean> getV2(int month, String user) {

		List<RecordV2Bean> list = dao.getV2(month, user);

		for (RecordV2Bean item : list) {
			completeRecord(item);
		}
		return list;
	}

	public RecordV2Bean getRecord(Long id) {

		RecordV2Bean record = dao.getRecord(id);
		completeRecord(record);
		return record;
	}

	private void completeRecord(RecordV2Bean item) {
		String bn = item.getBadgeNumber();
		EmployeeBean eb = dao.getEmployee(bn);

		item.setNome(eb.getName());
		item.setCognome(eb.getSurname());
		Long id = item.getIdProject();
		ProjectBean prb = dao.getProject(id);
		item.setProjectDesc(prb.getDescription());
		item.setCustomer(prb.getCustomer());
		item.setBusinessUnit(prb.getBusinessUnit());
	}

	public void updateRecord(RecordV2Bean record) {
		completeRecord(record);
		dao.update(record);
	}

	public void insertRecord(RecordV2Bean record) {
		completeRecord(record);
		dao.insert(record);
	}

	public void deleteRecord(Long id) {
		dao.delete(id);
	}
	
	public void v2Update(Long id, String colname, Integer value, String username){
		dao.updateTable(id, colname, value, username);
	}
	
	public List<Integer> getMonths(String user) {
		List<Integer> monthsList = dao.getMonths(user);
		
		return monthsList;
	}
	
	public List<V2Bean> findByUser(String username) {
		
		return dao.findByUser(username);
	}
	
	public boolean addNextMonth(String username) {
		
		List<Integer> list = dao.getMonths(username);
		Integer lastMonth = list.get(list.size()-1);
		
		List<Integer> listConfig = dao.getMonthsConfig();
		Integer lastMonthConfig = listConfig.get(listConfig.size()-1);
		
		boolean check = lastMonth.intValue() < lastMonthConfig.intValue();
		if (check) {
			dao.addNextMonth(username, lastMonth);
			return true;
		}

		return false;
	}
	
	public void updateEditable(String user, int month) {
		dao.updateEditable(user, month);
	}
	
	public void updateMonthsStatus(int month, boolean enable) {
		dao.updateMonthsStatus(month, enable);
	}

	public void updateV2ConfigStatus(int month, boolean enable) {
		dao.updateV2ConfigStatus(month, enable);
	}
	/*
	public List<V2Bean> getV2ToApprove(String user) {
		
		List<V2Bean> v2List = dao.getV2ToApprove(user);
		
		return v2List;
	}
	*/
	public List<V2Bean> getV2Config() {
		
		List<V2Bean> v2List = dao.getV2Config();
		
		return v2List;
	}


	public void v2Update(long id, String realColname , String value , String username) {
		dao.updateTable(id, realColname, value, username);
	}

}