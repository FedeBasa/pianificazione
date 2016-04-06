package it.soprasteria.pianificazione.v2.dao;

import java.util.List;

import it.soprasteria.pianificazione.v2.bean.PmBean;
public interface PmDao {
	
	public List<PmBean> verifyV2();
	
	public List<PmBean> verifyStatus(Integer month, PmDao dao);
}
