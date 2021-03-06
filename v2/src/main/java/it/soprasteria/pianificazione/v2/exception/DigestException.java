package it.soprasteria.pianificazione.v2.exception;

public class DigestException extends Exception {

	private static final long serialVersionUID = 1L;

	public DigestException() {
		super();
	}
	
	public DigestException(String message) {
		super(message);
	}
	
	public DigestException(String message, Throwable th) {
		super(message, th);
	}
	
	public DigestException(Throwable th) {
		super(th);
	}

}