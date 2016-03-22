package it.soprasteria.pianificazione.v2.service;

import org.springframework.beans.factory.annotation.Autowired;

import it.soprasteria.pianificazione.v2.bean.UserBean;
import it.soprasteria.pianificazione.v2.dao.Dao;

public class UserService {

	@Autowired
	private Dao dao;
	
	public UserBean login(String username, String password) {
		if(dao.login(username, password).getProfilo().equals("")){
		return null;
		}else{
			return dao.login(username, password);
		}
	}
	
}
