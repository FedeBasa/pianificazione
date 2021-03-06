package it.soprasteria.pianificazione.v2.bean;

import java.text.ParseException;

import it.soprasteria.pianificazione.v2.util.DateUtil;

public class V2Bean {

	private int month;
	private String user;
	private int stato;
	private int businessUnit;

	public int getMonth() {
		return month;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public String getFormattedMonth() {
		try {
			return DateUtil.convertExportFormat(month);
		} catch (ParseException e) {
			return String.valueOf(month);
		}
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public int getStato() {
		return stato;
	}

	public void setStato(int stato) {
		this.stato = stato;
	}

	public int getBusinessUnit() {
		return businessUnit;
	}

	public void setBusinessUnit(int businessUnit) {
		this.businessUnit = businessUnit;
	}

}