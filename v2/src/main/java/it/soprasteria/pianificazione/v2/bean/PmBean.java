package it.soprasteria.pianificazione.v2.bean;

public class PmBean {

	private String pm;
	private int mese;
	private String stato;
	private String username;

	public String getUsername() {
		return username;
	}

	public void setUsername(String profilo) {
		this.username = profilo;
	}

	public String getPm() {
		return pm;
	}

	public void setPm(String pm) {
		this.pm = pm;
	}

	public int getMese() {
		return mese;
	}

	public void setMese(int mese) {
		this.mese = mese;
	}

	public String getStato() {
		return stato;
	}

	public void setStato(String stato) {
		this.stato = stato;
	}

}