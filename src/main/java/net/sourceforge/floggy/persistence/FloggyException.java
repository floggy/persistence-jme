package net.sourceforge.floggy.persistence;

/**
 * Exception thrown to indicate a persistence problem.
 * 
 * @since 1.0
 */
public class FloggyException extends Exception {

	/**
	 * Construct a new FloggyException with no detail message.
	 */
	public FloggyException() {
		super();
	}

	/**
	 * Construct a new FloggyException with a specified detail message.
	 * 
	 * @param message
	 *            The detail message.
	 */
	public FloggyException(String message) {
		super(message);
	}
}
