package it.soprasteria.pianificazione.v2.bean;

import java.util.Date;

public class ProjectBean {

	private long idProject;
	private String customer;
	private String description;
	private String type;
	private String currency;
	private int businessUnit;
	private String utenteIns;
	private Date dataIns;
	private String utenteMod;
	private Date dataMod;

	public static ProjectBean build(long idProject, String description, String customer, int businessUnit, String utenteIns) {

		ProjectBean instance = new ProjectBean();
		instance.idProject = idProject;
		instance.description = description;
		instance.customer = customer;
		instance.businessUnit = businessUnit;
		instance.utenteIns = utenteIns;

		return instance;
	}

	public void setBusinessUnit(int businessUnit) {
		this.businessUnit = businessUnit;
	}

	public int getBusinessUnit() {
		return businessUnit;
	}

	public long getIdProject() {
		return idProject;
	}

	public void setIdProject(long idProject) {
		this.idProject = idProject;
	}

	public String getCustomer() {
		return customer;
	}

	public void setCustomer(String customer) {
		this.customer = customer;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
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
