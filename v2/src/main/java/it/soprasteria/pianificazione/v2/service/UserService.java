package it.soprasteria.pianificazione.v2.service;

import it.soprasteria.pianificazione.v2.bean.UserBean;

public class UserService {

	public UserBean login(String username, String password) {
		
		// TODO
		// implementare invocando il DAO
		// al momento è finto
		
		if ("admin".equalsIgnoreCase(username) && "admin".equalsIgnoreCase(password)) {
			
			return UserBean.build("admin", "utente", "prova", "administrator");
		}
		
		return null;
		
	}
	
}
