package simulator.exceptions;

public class WrongArgumentException extends Exception{
	/**
	 * 
	 */
	private static final long serialVersionUID = -6984156332695121659L;
	
	public WrongArgumentException() { super(); }
	public WrongArgumentException(String msg) { super(msg); }
	public WrongArgumentException(String msg, Throwable cause) { super(msg, cause); }
	public WrongArgumentException(String msg, Throwable cause, boolean suppression, boolean stackTrace) {
		super(msg, cause, suppression, stackTrace);
	}

}
