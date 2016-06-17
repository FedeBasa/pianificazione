package it.soprasteria.pianificazione.v2.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import it.soprasteria.pianificazione.v2.bean.UserBean;
import it.soprasteria.pianificazione.v2.dao.Dao;

public class LoginService {

	private static final Logger LOG = Logger.getLogger(LoginService.class);

	@Autowired
	private Dao dao;

	public int firstlogin(String userid) {

		return dao.checkChangePassword(userid);
	}

	public UserBean getUserFromUserPrincipal(String userPrincipal) {

		UserBean userBean = dao.findByUsername(userPrincipal);
		List<String> buList = new ArrayList<>();
		
		String divisione = userBean.getDivisione();
		String[] tokens = divisione.split(",");
		
		for(String token : tokens) {
			buList.addAll(dao.findBuByDivisione(token));
		}

		userBean.getBuList().addAll(buList);
		
		return userBean;
	}
	
}
