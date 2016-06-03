package it.soprasteria.pianificazione.v2.bean;

import java.util.Date;

public class FerieBean {

	private int idFerie;
	private int mese;
	private String badgeNumber;
	private String nome;
	private String cognome;
	private int ferie1;
	private int ferie2;
	private int ferie3;
	private String utenteIns;
	private Date dataIns;
	private String utenteMod;
	private Date dataMod;

	public int getIdFerie() {
		return idFerie;
	}

	public void setIdFerie(int idFerie) {
		this.idFerie = idFerie;
	}

	public int getMese() {
		return mese;
	}

	public void setMese(int mese) {
		this.mese = mese;
	}

	public String getBadgeNumber() {
		return badgeNumber;
	}

	public void setBadgeNumber(String badgeNumber) {
		this.badgeNumber = badgeNumber;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getCognome() {
		return cognome;
	}

	public void setCognome(String cognome) {
		this.cognome = cognome;
	}

	public int getFerie1() {
		return ferie1;
	}

	public void setFerie1(int ferie1) {
		this.ferie1 = ferie1;
	}

	public int getFerie2() {
		return ferie2;
	}

	public void setFerie2(int ferie2) {
		this.ferie2 = ferie2;
	}

	public int getFerie3() {
		return ferie3;
	}

	public void setFerie3(int ferie3) {
		this.ferie3 = ferie3;
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
