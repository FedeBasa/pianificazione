package it.soprasteria.pianificazione.v2.bean;

public class V2Bean {

		private int month;
		private String user;
	    private boolean editable;
	    private int business_unit;
	    
	    
		public int getMonth() {
			return month;
		}

		public void setMonth(int month) {
			this.month = month;
		}

		public String getUser() {
			return user;
		}

		public void setUser(String user) {
			this.user = user;
		}

		public boolean getEditable() {
			return editable;
		}

		public void setEditable(boolean editable) {
			this.editable = editable;
		}

		public int getBusiness_unit() {
			return business_unit;
		}

		public void setBusiness_unit(int business_unit) {
			this.business_unit = business_unit;
		}

}