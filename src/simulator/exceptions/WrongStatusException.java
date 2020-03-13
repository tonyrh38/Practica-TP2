package simulator.exceptions;

public class WrongStatusException extends Exception{
	/**
	 * 
	 */
	private static final long serialVersionUID = -6984156332695121659L;
	
	public WrongStatusException() { super(); }
	public WrongStatusException(String msg) { super(msg); }
	public WrongStatusException(String msg, Throwable cause) { super(msg, cause); }
	public WrongStatusException(String msg, Throwable cause, boolean suppression, boolean stackTrace) {
		super(msg, cause, suppression, stackTrace);
	}

}
