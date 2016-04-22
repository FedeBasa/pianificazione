package it.soprasteria.pianificazione.v2.bean;

public class RecordV2Bean extends BaseBean {

	private int month;
	private Long idRecord;
	private String badgeNumber;
	private String activityType;
	private Long idProject;
	private String projectDesc;
	private Integer price;
	private String currency;
	private Integer cons0;
	private Integer prod0;
	private Integer cons1;
	private Integer prod1;
	private Integer cons2;
	private Integer prod2;
	private String customer;
	private Integer businessUnit;
	private String nome;
	private String cognome;
	private String idUser;
	
	private String employeeDesc;

	public String getIdUser() {
		return idUser;
	}

	public void setIdUser(String idUser) {
		this.idUser = idUser;
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

	public Integer getBusinessUnit() {
		return businessUnit;
	}

	public void setBusinessUnit(Integer businessUnit) {
		this.businessUnit = businessUnit;
	}

	public int getMonth() {
		return month;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public Long getIdRecord() {
		return idRecord;
	}

	public void setIdRecord(Long idRecord) {
		this.idRecord = idRecord;
	}

	public String getBadgeNumber() {
		return badgeNumber;
	}

	public void setBadgeNumber(String badgeNumber) {
		this.badgeNumber = badgeNumber;
	}

	public void setEmployeeDesc(String employeeDesc) {
		this.employeeDesc = employeeDesc;
	}
	
	public String getEmployeeDesc() {
		
		if (employeeDesc != null) {
			return employeeDesc;
		}
		
		if (nome != null && cognome != null && badgeNumber != null) {
			return   nome + " " + cognome + " (" +  badgeNumber + ")";
		}
		return "";
	}

	public String getActivityType() {
		return activityType;
	}

	public void setActivityType(String activityType) {
		this.activityType = activityType;
	}

	public Long getIdProject() {
		return idProject;
	}

	public void setIdProject(Long idProject) {
		this.idProject = idProject;
	}

	public String getProjectDesc() {
		return projectDesc;
	}

	public void setProjectDesc(String projectDesc) {
		this.projectDesc = projectDesc;
	}

	public Integer getPrice() {
		return price;
	}

	public void setPrice(Integer price) {
		this.price = price;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public Integer getCons0() {
		return cons0;
	}

	public void setCons0(Integer cons0) {
		this.cons0 = cons0;
	}

	public Integer getProd0() {
		return prod0;
	}

	public void setProd0(Integer prod0) {
		this.prod0 = prod0;
	}

	public Integer getCons1() {
		return cons1;
	}

	public void setCons1(Integer cons1) {
		this.cons1 = cons1;
	}

	public Integer getProd1() {
		return prod1;
	}

	public void setProd1(Integer prod1) {
		this.prod1 = prod1;
	}

	public Integer getCons2() {
		return cons2;
	}

	public void setCons2(Integer cons2) {
		this.cons2 = cons2;
	}

	public Integer getProd2() {
		return prod2;
	}

	public void setProd2(Integer prod2) {
		this.prod2 = prod2;
	}

	public String getCustomer() {
		return customer;
	}

	public void setCustomer(String customer) {
		this.customer = customer;
	}

}
