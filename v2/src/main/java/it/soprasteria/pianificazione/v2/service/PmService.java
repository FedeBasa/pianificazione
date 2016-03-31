package it.soprasteria.pianificazione.v2.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import it.soprasteria.pianificazione.v2.bean.UserBean;
import it.soprasteria.pianificazione.v2.dao.PmDao;
public class PmService {
	
	@Autowired
	PmDao dao;
	
		public List<UserBean> getStatus(Integer month){
			
			return dao.verifyV2(month);
			
		}
	
}
