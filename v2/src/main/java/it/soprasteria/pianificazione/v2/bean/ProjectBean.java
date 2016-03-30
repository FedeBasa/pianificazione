package it.soprasteria.pianificazione.v2.bean;

public class ProjectBean {

	private long idProject;
	private String customer;
	private String description;
	private String type;
	private String currency;
	private int businessUnit;
	
	
public static ProjectBean build(long idProject, String description, String customer, int businessUnit) {
		
		ProjectBean instance = new ProjectBean();
		instance.idProject = idProject;
		instance.description = description;
		instance.customer = customer;
		instance.businessUnit = businessUnit;
		
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

}
