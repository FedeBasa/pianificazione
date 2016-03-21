package it.soprasteria.pianificazione.v2.service;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import it.soprasteria.pianificazione.v2.bean.EmployeeBean;
import it.soprasteria.pianificazione.v2.bean.ProjectBean;
import it.soprasteria.pianificazione.v2.bean.RecordV2Bean;
import it.soprasteria.pianificazione.v2.bean.V2Bean;
import it.soprasteria.pianificazione.v2.dao.DaoImpl;
import it.soprasteria.pianificazione.v2.util.DateUtil;

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

		item.setEmployeeDesc(eb.getName() + " " + eb.getSurname());

		Long id = item.getIdProject();
		ProjectBean prb = dao.getProject(id);

		item.setProjectDesc(prb.getDescription());
		item.setCurrency(prb.getCurrency());
		item.setActivityType(prb.getType());
		item.setCustomer(prb.getCustomer());
		item.setBusinessUnit(prb.getBusinessUnit());

	}

	public void updateRecord(RecordV2Bean record) {
		dao.update(record);
	}

	public void insertRecord(RecordV2Bean record) {
		dao.insert(record);
	}

	public void deleteRecord(Long id) {
		dao.delete(id);
	}
	
	public void v2Update(Long id, String colname, Integer value){
		dao.updateTable(id, colname, value);
	}
	
	public List<Integer> getMonths(String user) {
		List<Integer> monthsList = dao.getMonths(user);
		
		return monthsList;
	}
	
	public boolean addNextMonth(String user) {
		
		List<Integer> list = dao.getMonths(user);
		Integer lastMonth = list.get(list.size()-1);
		
		if(DateUtil.checkMonth(lastMonth)) {
			dao.addNextMonth(user, lastMonth);
			return false;
		}
		
		return true;
	}
	
	// TODO
	// rinominare
	public void updateEditable(String user, int month) {
		dao.updateEditable(user, month);
	}

}