package net.sourceforge.floggy.persistence;

/**
 * Thrown to indicate a general exception occurred in a operation.
 * 
 * @author Thiago Leão Moreira <thiagolm@users.sourceforge.net/>
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
         *                The detail message.
         */
    public FloggyException(String message) {
	super(message);
    }
}
