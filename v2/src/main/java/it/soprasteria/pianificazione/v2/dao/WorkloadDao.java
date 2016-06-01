package it.soprasteria.pianificazione.v2.dao;

import java.util.List;

import it.soprasteria.pianificazione.v2.bean.WorkloadBean;
import it.soprasteria.pianificazione.v2.bean.WorkloadDetailBean;

public interface WorkloadDao {

	public List<WorkloadBean> findWorkload(int month, String username);
	
	public List<WorkloadDetailBean> findWorkloadDetails(int month, String badgeNumber);
	
	public void updateFerieTable(WorkloadBean workloadBean, int month, String colname, Integer value, String username);
}
