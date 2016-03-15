package it.soprasteria.pianificazione.v2.bean;

import java.io.Serializable;

public class EmployeeBean implements Serializable {

	private String badgeNumber;
	private String name;
	private String surname;
	private String nameSurname;

	public static EmployeeBean build(String badgeNumber, String name, String surname) {
		
		EmployeeBean instance = new EmployeeBean();
		instance.badgeNumber = badgeNumber;
		instance.name = name;
		instance.surname = surname;
		
		return instance;
	}
	
	public String getNameSurname() {
		return nameSurname;
	}

	public void setNameSurname(String nameSurname) {
		this.nameSurname = nameSurname;
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
