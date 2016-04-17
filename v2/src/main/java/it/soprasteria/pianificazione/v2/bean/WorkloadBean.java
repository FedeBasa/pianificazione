package it.soprasteria.pianificazione.v2.bean;

public class WorkloadBean {

	private String badgeNumber;
	private String name;
	private String surname;
	private int work1;
	private int recognized1;
	private int nit1;
	private int nonProject1;
	private int work2;
	private int recognized2;
	private int nit2;
	private int nonProject2;
	private int work3;
	private int recognized3;
	private int nit3;
	private int nonProject3;

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

	public int getWork1() {
		return work1;
	}

	public void setWork1(int work1) {
		this.work1 = work1;
	}

	public int getRecognized1() {
		return recognized1;
	}

	public void setRecognized1(int recognized1) {
		this.recognized1 = recognized1;
	}

	public int getNit1() {
		return nit1;
	}

	public void setNit1(int nit1) {
		this.nit1 = nit1;
	}

	public int getNonProject1() {
		return nonProject1;
	}

	public void setNonProject1(int nonProject1) {
		this.nonProject1 = nonProject1;
	}

	public int getWork2() {
		return work2;
	}

	public void setWork2(int work2) {
		this.work2 = work2;
	}

	public int getRecognized2() {
		return recognized2;
	}

	public void setRecognized2(int recognized2) {
		this.recognized2 = recognized2;
	}

	public int getNit2() {
		return nit2;
	}

	public void setNit2(int nit2) {
		this.nit2 = nit2;
	}

	public int getNonProject2() {
		return nonProject2;
	}

	public void setNonProject2(int nonProject2) {
		this.nonProject2 = nonProject2;
	}

	public int getWork3() {
		return work3;
	}

	public void setWork3(int work3) {
		this.work3 = work3;
	}

	public int getRecognized3() {
		return recognized3;
	}

	public void setRecognized3(int recognized3) {
		this.recognized3 = recognized3;
	}

	public int getNit3() {
		return nit3;
	}

	public void setNit3(int nit3) {
		this.nit3 = nit3;
	}

	public int getNonProject3() {
		return nonProject3;
	}

	public void setNonProject3(int nonProject3) {
		this.nonProject3 = nonProject3;
	}

}
