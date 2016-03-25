package it.soprasteria.pianificazione.v2.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import it.soprasteria.pianificazione.v2.bean.EnumBean;
import it.soprasteria.pianificazione.v2.dao.DaoImpl;
import it.soprasteria.pianificazione.v2.dao.EnumDao;


public class EnumService  {

	private static final Logger LOG = Logger.getLogger(EnumService.class);
	
	@Autowired
	private EnumDao enumdao;
	
	
	public Set<String> getSet(String realcolname){
		
		List<EnumBean> enumList = enumdao.findByType(realcolname);
		
		Set<String> set = new HashSet<String>();
		
		
			for(EnumBean enumbean : enumList){
				
				LOG.debug("value "+enumbean.getValue());
				
				set.add(enumbean.getValue());
			}
			
		return set;
	}
	
}
