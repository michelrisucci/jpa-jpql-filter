package javax.persistence.filter.exception;

/**
 * @author Michel Risucci
 */
public class FirstResultOutOfRangeException extends RuntimeException {

	private static final long serialVersionUID = 7980821713987937823L;

	/**
	 * 
	 */
	public FirstResultOutOfRangeException() {
		super();
	}

	/**
	 * @param message
	 */
	public FirstResultOutOfRangeException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public FirstResultOutOfRangeException(Throwable cause) {
		super(cause);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public FirstResultOutOfRangeException(String message, Throwable cause) {
		super(message, cause);
	}

}