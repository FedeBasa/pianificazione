package it.soprasteria.pianificazione.v2.bean;

public class WorkloadDetailBean {

	private String badgeNumber;
	private String name;
	private String surname;
	private String descProgetto;
	private int cons1;
	private int prod1;
	private int cons2;
	private int prod2;
	private int cons3;
	private int prod3;
	private String usernameIns;

	public String getBadgeNumber() {
		return badgeNumber;
	}

	public void setBadgeNumber(String badgeNumber) {
		this.badgeNumber = badgeNumber;
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
	
	public String getEmployeeDesc() {
		
		return name + " " + surname;
	}

	public String getDescProgetto() {
		return descProgetto;
	}

	public void setDescProgetto(String descProgetto) {
		this.descProgetto = descProgetto;
	}

	public int getCons1() {
		return cons1;
	}

	public void setCons1(int cons1) {
		this.cons1 = cons1;
	}

	public int getProd1() {
		return prod1;
	}

	public void setProd1(int prod1) {
		this.prod1 = prod1;
	}

	public int getCons2() {
		return cons2;
	}

	public void setCons2(int cons2) {
		this.cons2 = cons2;
	}

	public int getProd2() {
		return prod2;
	}

	public void setProd2(int prod2) {
		this.prod2 = prod2;
	}

	public int getCons3() {
		return cons3;
	}

	public void setCons3(int cons3) {
		this.cons3 = cons3;
	}

	public int getProd3() {
		return prod3;
	}

	public void setProd3(int prod3) {
		this.prod3 = prod3;
	}

	public String getUsernameIns() {
		return usernameIns;
	}

	public void setUsernameIns(String usernameIns) {
		this.usernameIns = usernameIns;
	}

}
