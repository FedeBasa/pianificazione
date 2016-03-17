package it.soprasteria.pianificazione.v2.service;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import it.soprasteria.pianificazione.v2.bean.EmployeeBean;
import it.soprasteria.pianificazione.v2.bean.ProjectBean;
import it.soprasteria.pianificazione.v2.bean.RecordV2Bean;
import it.soprasteria.pianificazione.v2.dao.DaoImpl;

public class V2Service {
	private static final Logger LOG = Logger.getLogger(V2Service.class);

	@Autowired
	private DaoImpl dao;

	public List<RecordV2Bean> getV2(int month, String user) {

		List<RecordV2Bean> list = dao.getV2(month, user);

		for (RecordV2Bean item : list) {
			completeRecord(item);
		}
		return list;
	}

	public RecordV2Bean getRecord(long id) {

		RecordV2Bean record = dao.getRecord(id);
		completeRecord(record);
		LOG.debug("*******************************************************" + "  " + record.getProjectDesc());
		return record;
	}

	private void completeRecord(RecordV2Bean item) {
		String bn = item.getBadgeNumber();
		EmployeeBean eb = dao.getEmployee(bn);

		item.setEmployeeDesc(eb.getName() + " " + eb.getSurname());

		long id = item.getIdProject();
		LOG.debug("IDENTIFICATIVO " + id);
		ProjectBean prb = dao.getProject(id);

		item.setProjectDesc(prb.getDescription());
		item.setCurrency(prb.getCurrency());
		item.setActivityType(prb.getType());
		item.setCustomer(prb.getCustomer());

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
	
	// TODO
	// manca parametro in input per filtrare l'utente
	public List<RecordV2Bean> trovaV2(){
		List<RecordV2Bean> v2s = dao.findAllV2();
		return v2s;
	}
	
	public List<Integer> getMonths(String user) {
		List<Integer> monthsList = dao.getMonths(user);
		
		return monthsList;
	}
	
	public boolean addNextMonth(String user) {
		
		if(dao.checkMonth(dao.getLastMonth(dao.getMonths(user)))) {
			dao.addNextMonth(user);
			return false;
		}
		
		return true;
	}
	
	public boolean isEditable(String user, int month) {
		
		if(dao.checkEditable(user, month) == 0) {			
			return true;
		}
		
		return false;
	}

}
