package it.soprasteria.pianificazione.v2.dao;

import java.util.List;

import it.soprasteria.pianificazione.v2.bean.V2Bean;
public interface AdminDao {

	/*
	public List<PmBean> verifyV2();
	
	public List<PmBean> verifyStatus(Integer month, AdminDao dao);
	*/
	
	public void addNextConfigMonth(Integer lastMonth);

	public void updateEditable(String user, int month);

	public void updateMonthsStatus(int month, int enable);

	public void updateV2ConfigStatus(int month, int enable);

	public List<V2Bean> getV2Config();

	public List<Integer> getMonthsConfig();
}
