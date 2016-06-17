package it.soprasteria.pianificazione.v2.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import it.soprasteria.pianificazione.v2.bean.V2Bean;
import it.soprasteria.pianificazione.v2.dao.AdminDao;

public class AdminService {

	@Autowired
	private AdminDao dao;

	public void addNextConfigMonth() {

		List<Integer> listConfig = dao.getMonthsConfig();
		Integer lastMonthConfig = listConfig.get(listConfig.size() - 1);

		dao.addNextConfigMonth(lastMonthConfig);
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

	public List<V2Bean> getV2Config() {

		return dao.getV2Config();
	}
	
}
