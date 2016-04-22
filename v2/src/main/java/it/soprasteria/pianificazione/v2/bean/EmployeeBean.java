package it.soprasteria.pianificazione.v2.bean;

import java.io.Serializable;

public class EmployeeBean extends BaseBean implements Serializable {

	private String badgeNumber;
	private String name;
	private String surname;

	public static EmployeeBean build(String badgeNumber, String name, String surname, String utenteIns) {

		EmployeeBean instance = new EmployeeBean();
		instance.badgeNumber = badgeNumber;
		instance.name = name;
		instance.surname = surname;
		instance.setUtenteIns(utenteIns);

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

}