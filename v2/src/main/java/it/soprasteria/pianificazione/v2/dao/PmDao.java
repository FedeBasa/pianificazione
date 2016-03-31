package it.soprasteria.pianificazione.v2.dao;

import java.util.List;

import it.soprasteria.pianificazione.v2.bean.UserBean;
public interface PmDao {
	
	public List<UserBean> verifyV2(Integer month);
	
	
}
