package it.soprasteria.pianificazione.v2.dao;

import java.util.List;

import it.soprasteria.pianificazione.v2.bean.EnumBean;

public interface EnumDao {

	public  EnumBean findById(Integer id);
	
	public  List<EnumBean> findByType(String tipologia);
	
}
