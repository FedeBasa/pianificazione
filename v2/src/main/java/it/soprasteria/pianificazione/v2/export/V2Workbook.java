package it.soprasteria.pianificazione.v2.export;

import java.util.List;

import it.soprasteria.pianificazione.v2.bean.RecordV2Bean;
import it.soprasteria.pianificazione.v2.service.V2Service;

public class V2Workbook {

	private V2Service v2Service;
	
	public V2Workbook build(V2Service v2Service) {
		
		V2Workbook instance = new V2Workbook();
		instance.v2Service = v2Service;
		
		return instance;
	}
	
	public void export(int month, int businessUnit, String username) {

		List<RecordV2Bean> recordList = v2Service.getV2(month, businessUnit, username);
		
		

	}
	
	public void setV2Service(V2Service v2Service) {
		this.v2Service = v2Service;
	}
}
