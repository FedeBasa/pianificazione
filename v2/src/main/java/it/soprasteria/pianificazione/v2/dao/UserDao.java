package it.soprasteria.pianificazione.v2.dao;

import java.util.List;

import it.soprasteria.pianificazione.v2.bean.UserBean;

public interface UserDao {

	public UserBean findByUsername(final String username);
	
	public List<UserBean> findByProfilo(final String profilo);

	public UserBean login(String username, String password);

	public void changePassword(String userId, String password);
}
