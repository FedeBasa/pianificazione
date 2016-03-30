package it.soprasteria.pianificazione.v2.web.ajax;

public class JsonResponse {

	public static final int CODE_SUCCESS = 0;
	public static final int CODE_INVALID_COLNAME = -1;
	public static final int CODE_INVALID_COLVALUE = -2;
	
	private int code;
	private String message;

	public static JsonResponse build(int code, String message) {

		JsonResponse instance = new JsonResponse();
		instance.code = code;
		instance.message = message;

		return instance;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
