package it.soprasteria.pianificazione.v2.service;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import it.soprasteria.pianificazione.v2.dao.Dao;

public class LoginService {
		
	
	@Autowired
	Dao dao ;
	
	private static final Logger LOG = Logger.getLogger(LoginService.class);
	
	public int firstlogin(String userid){
		LOG.debug("USERSTATUS :" + dao.controlChangePassword(userid));
		return	dao.controlChangePassword(userid);
	}
	
	
	
}
