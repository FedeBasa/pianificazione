package it.soprasteria.pianificazione.v2.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import it.soprasteria.pianificazione.v2.bean.UserBean;
import it.soprasteria.pianificazione.v2.dao.UserDao;

public class UserService {

	@Autowired
	private UserDao dao;

	public UserBean login(String username, String password) {
		return dao.login(username, password);
	}
	
	public void changePw(String username, String password) {
		dao.changePassword(username, password);
	}
	
	public List<UserBean> findByProfilo(final String profilo) {
		
		return dao.findByProfilo(profilo);
	}
	
	public UserBean findByUsername(final String username) {
		
		return dao.findByUsername(username);
	}

}
