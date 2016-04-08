package it.soprasteria.pianificazione.v2.service;

import org.springframework.beans.factory.annotation.Autowired;

import it.soprasteria.pianificazione.v2.dao.Dao;

public class LoginService {
		
	
	@Autowired
	Dao dao ;
	
	public int firstlogin(String userid){
		return	dao.controlLogin(userid);
	}
	
}
