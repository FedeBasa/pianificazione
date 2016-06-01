package it.soprasteria.pianificazione.v2.web.ajax;

import it.soprasteria.pianificazione.v2.bean.WorkloadBean;

public class UpdateFerieJsonResponse extends JsonResponse {

	private int nonProj1;
	private int nonProj2;
	private int nonProj3;

	public static UpdateFerieJsonResponse build(int code, String message, WorkloadBean bean) {

		UpdateFerieJsonResponse instance = new UpdateFerieJsonResponse();

		instance.setCode(code);
		instance.setMessage(message);

		instance.nonProj1 = bean.getNonProject1();
		instance.nonProj2 = bean.getNonProject2();
		instance.nonProj3 = bean.getNonProject3();

		return instance;
	}

	public int getNonProj1() {
		return nonProj1;
	}

	public void setNonProj1(int nonProj1) {
		this.nonProj1 = nonProj1;
	}

	public int getNonProj2() {
		return nonProj2;
	}

	public void setNonProj2(int nonProj2) {
		this.nonProj2 = nonProj2;
	}

	public int getNonProj3() {
		return nonProj3;
	}

	public void setNonProj3(int nonProj3) {
		this.nonProj3 = nonProj3;
	}

}
