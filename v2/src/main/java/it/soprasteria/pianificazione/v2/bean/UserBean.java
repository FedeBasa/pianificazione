package it.soprasteria.pianificazione.v2.bean;

public class UserBean {

	private String username;
	private String password;
	private String name;
	private String surname;
	private String profilo;
	private String active;
	private int bu;
	


	public static UserBean build(String username, String name, String surname, String profilo) {

		UserBean result = new UserBean();

		result.username = username;
		result.name = name;
		result.surname = surname;
		result.profilo = profilo;

		return result;
	}
	
	public int getBu() {
		return bu;
	}

	public void setBu(int bu) {
		this.bu = bu;
	}

	
	public String getActive() {
		return active;
	}

	public void setActive(String active) {
		this.active = active;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
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

	public String getProfilo() {
		return profilo;
	}

	public void setProfilo(String profilo) {
		this.profilo = profilo;
	}

}
