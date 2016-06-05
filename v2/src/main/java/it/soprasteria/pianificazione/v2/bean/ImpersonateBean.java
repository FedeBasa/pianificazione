package it.soprasteria.pianificazione.v2.bean;

import java.util.Date;

public class ImpersonateBean {

	private UserBean user;
	private Date startDate;

	public static ImpersonateBean build(UserBean user) {

		ImpersonateBean instance = new ImpersonateBean();

		instance.user = user;
		instance.startDate = new Date();

		return instance;
	}

	public UserBean getUser() {
		return user;
	}

	public void setUser(UserBean user) {
		this.user = user;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

}
