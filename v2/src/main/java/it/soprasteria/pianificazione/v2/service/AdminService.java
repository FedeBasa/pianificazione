package it.soprasteria.pianificazione.v2.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import it.soprasteria.pianificazione.v2.bean.V2Bean;
import it.soprasteria.pianificazione.v2.dao.AdminDao;

public class AdminService {

	@Autowired
	private AdminDao dao;

	public void addNextConfigMonth(List<String> buList) {

		for(String businessUnit : buList) {
			List<Integer> listConfig = dao.getMonthsConfig(Integer.parseInt(businessUnit));
			Integer lastMonthConfig = listConfig.get(listConfig.size() - 1);
	
			dao.addNextConfigMonth(lastMonthConfig, Integer.parseInt(businessUnit));
		}
	}
	
	public void updateEditable(String user, int month) {
		dao.updateEditable(user, month);
	}

	public void updateMonthsStatus(int month, int bu, int enable) {
		dao.updateMonthsStatus(month, bu, enable);
	}

	public void updateV2ConfigStatus(int month, int bu, int enable) {
		dao.updateV2ConfigStatus(month, bu, enable);
	}

	public List<V2Bean> getV2Config(List<String> buList) {

		List<V2Bean> result = new ArrayList<>();
		for(String businessUnit : buList) {
			result.addAll(dao.getV2Config(Integer.parseInt(businessUnit)));
		}
		
	    Collections.sort(result, new Comparator<V2Bean>() {
	    	@Override
	    	public int compare(V2Bean o1, V2Bean o2) {
	    		
	    		int result = (o1.getMonth() == o2.getMonth()) ? 0 : (o1.getMonth() < o2.getMonth() ? 1 : -1);
	    		if (result == 0) {
	    			result = (o1.getBusinessUnit() == o2.getBusinessUnit()) ? 0 : (o1.getBusinessUnit() > o2.getBusinessUnit() ? 1 : -1);
	    		}
	    		return result;
	    	}
	    });
	    
	    return result;
	}
	
	public V2Bean getV2Config(int month, int businessUnit) {
		
		return dao.getV2Config(month, businessUnit);
	}
	
}
