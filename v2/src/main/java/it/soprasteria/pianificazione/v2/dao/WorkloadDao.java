package it.soprasteria.pianificazione.v2.dao;

import java.util.List;

import it.soprasteria.pianificazione.v2.bean.WorkloadBean;

public interface WorkloadDao {

	public List<WorkloadBean> findWorkload(int month, String username);
}
