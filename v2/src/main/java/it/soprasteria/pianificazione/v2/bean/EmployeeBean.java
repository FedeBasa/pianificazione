package it.soprasteria.pianificazione.v2.bean;

import java.io.Serializable;
import java.util.Date;

public class EmployeeBean implements Serializable {

	private String badgeNumber;
	private String name;
	private String surname;
	private String utenteIns;
	private Date dataIns;
	private String utenteMod;
	private Date dataMod;

	public static EmployeeBean build(String badgeNumber, String name, String surname, String utenteIns) {

		EmployeeBean instance = new EmployeeBean();
		instance.badgeNumber = badgeNumber;
		instance.name = name;
		instance.surname = surname;
		instance.utenteIns = utenteIns;

		return instance;
	}

	public String getNameSurname() {
		if (name != null && surname != null && badgeNumber != null) {
			return name + " " + surname + " (" + badgeNumber + ")";
		}
		return "";
	}

	public String getBadgeNumber() {
		return badgeNumber;
	}

	public void setBadgeNumber(String budgeNumber) {
		this.badgeNumber = budgeNumber;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getUtenteIns() {
		return utenteIns;
	}

	public void setUtenteIns(String utenteIns) {
		this.utenteIns = utenteIns;
	}

	public Date getDataIns() {
		return dataIns;
	}

	public void setDataIns(Date dataIns) {
		this.dataIns = dataIns;
	}

	public String getUtenteMod() {
		return utenteMod;
	}

	public void setUtenteMod(String utenteMod) {
		this.utenteMod = utenteMod;
	}

	public Date getDataMod() {
		return dataMod;
	}

	public void setDataMod(Date dataMod) {
		this.dataMod = dataMod;
	}

}
