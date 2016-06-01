package it.soprasteria.pianificazione.v2.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import it.soprasteria.pianificazione.v2.bean.WorkloadBean;
import it.soprasteria.pianificazione.v2.bean.WorkloadDetailBean;
import it.soprasteria.pianificazione.v2.dao.WorkloadDao;
import it.soprasteria.pianificazione.v2.util.DateUtil;

public class WorkloadService {

	@Autowired
	private WorkloadDao dao;
	
	@Autowired
	private CalendarConfigService calendarConfigService;
	
	public List<WorkloadBean> findWorkload(int month, String username) {
		
		List<WorkloadBean> list = dao.findWorkload(month, username);
		
		int config1 = calendarConfigService.getConfig(month);
		int config2 = calendarConfigService.getConfig(DateUtil.addMonth(month, 1));
		int config3 = calendarConfigService.getConfig(DateUtil.addMonth(month, 2));
		
		for (WorkloadBean bean : list) {
			
			bean.setNit1(bean.getWork1() - bean.getRecognized1());
			bean.setNit2(bean.getWork2() - bean.getRecognized2());
			bean.setNit3(bean.getWork3() - bean.getRecognized3());
			
			bean.setNonProject1(config1 - bean.getWork1() - bean.getFerie1());
			bean.setNonProject2(config2 - bean.getWork2() - bean.getFerie2());
			bean.setNonProject3(config3 - bean.getWork3() - bean.getFerie3());
		}
		
		return list;
	}
	
	public List<WorkloadDetailBean> findWorkloadDetail(int month, String badgeNumber) {
	
		return this.dao.findWorkloadDetails(month, badgeNumber);
	}
	
	//ritono il noninproj
	public WorkloadBean workloadUpdate(int month, String colname, Integer value, String username, String badgeNumber) {
		
		List<WorkloadBean> list = findWorkload(month, username);
		
		WorkloadBean workloadBean = null;
		for (WorkloadBean item : list) {
			
			if (item.getBadgeNumber().equals(badgeNumber)) {
				
				workloadBean = item;
				break;
			}
		}
		
		if (workloadBean == null) {
			return null;
		}
		
		int config1 = calendarConfigService.getConfig(month);
		int config2 = calendarConfigService.getConfig(DateUtil.addMonth(month, 1));
		int config3 = calendarConfigService.getConfig(DateUtil.addMonth(month, 2));
		
		if ("ferie_1".equals(colname)) {
			
			if (value.intValue() > config1 - workloadBean.getWork1()) {
				return null;
			}
			workloadBean.setNonProject1(config1 - workloadBean.getWork1() - value);
			
		} else if ("ferie_2".equals(colname)) {
			
			if (value.intValue() > config2 - workloadBean.getWork2()) {
				return null;
			}
			workloadBean.setNonProject2(config2 - workloadBean.getWork2() - value);
			
		} else if ("ferie_3".equals(colname)) {
			
			if (value.intValue() > config3 - workloadBean.getWork3()) {
				return null;
			}
			workloadBean.setNonProject3(config3 - workloadBean.getWork3() - value);
		}
		dao.updateFerieTable(workloadBean, month, colname, value, username);
		
		return workloadBean;
	}
}
