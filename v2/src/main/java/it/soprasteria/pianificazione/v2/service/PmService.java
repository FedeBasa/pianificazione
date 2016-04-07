package it.soprasteria.pianificazione.v2.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import it.soprasteria.pianificazione.v2.bean.PmBean;
import it.soprasteria.pianificazione.v2.dao.PmDao;

public class PmService {

	@Autowired
	private PmDao dao;

	public List<PmBean> getStatus(Integer month) {
		return dao.verifyStatus(month, dao);
	}

}