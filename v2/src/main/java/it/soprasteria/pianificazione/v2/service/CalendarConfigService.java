package it.soprasteria.pianificazione.v2.service;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;

import it.soprasteria.pianificazione.v2.dao.DaoImpl;

public class CalendarConfigService {

	private static final Logger LOG = Logger.getLogger(CalendarConfigService.class);

	@Autowired
	private DaoImpl dao;

	@Cacheable(value = "calendarConfigCache")
	public int getConfig(int month) {
		
		return dao.getTotDays(month);
	}
	
}
