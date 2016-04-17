package it.soprasteria.pianificazione.v2.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import it.soprasteria.pianificazione.v2.bean.EmployeeBean;
import it.soprasteria.pianificazione.v2.bean.WorkloadBean;
import it.soprasteria.pianificazione.v2.bean.WorkloadDetailBean;
import it.soprasteria.pianificazione.v2.dao.WorkloadDao;
import it.soprasteria.pianificazione.v2.util.DateUtil;

public class WorkloadService {

	@Autowired
	private WorkloadDao dao;
	@Autowired
	private EmployeeService employeeService;
	@Autowired
	private CalendarConfigService calendarConfigService;
	
	public List<WorkloadBean> findWorkload(int month, String username) {
		
		List<WorkloadBean> list = dao.findWorkload(month, username);
		
		int config1 = calendarConfigService.getConfig(month);
		int config2 = calendarConfigService.getConfig(DateUtil.addMonth(month, 1));
		int config3 = calendarConfigService.getConfig(DateUtil.addMonth(month, 2));
		
		for (WorkloadBean bean : list) {
			
			EmployeeBean employee = employeeService.findById(bean.getBadgeNumber());
			bean.setName(employee.getName());
			bean.setSurname(employee.getSurname());
			
			bean.setNit1(bean.getWork1() - bean.getRecognized1());
			bean.setNit2(bean.getWork2() - bean.getRecognized2());
			bean.setNit3(bean.getWork3() - bean.getRecognized3());
			
			bean.setNonProject1(config1 - bean.getWork1());
			bean.setNonProject2(config2 - bean.getWork2());
			bean.setNonProject3(config3 - bean.getWork3());
		}
		
		return list;
	}
	
	public List<WorkloadDetailBean> findWorkloadDetail(int month, String badgeNumber) {
	
		return this.dao.findWorkloadDetails(month, badgeNumber);
	}
	
}
