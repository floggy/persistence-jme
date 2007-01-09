/**
 *
 */
package net.sourceforge.floggy.persistence;

/**
 * @author Thiago Rossato
 * @since 1.0
 */
public class NoMoreObjectsException extends RuntimeException {

    public NoMoreObjectsException() {
	super();
    }

    public NoMoreObjectsException(String message) {
	super(message);
    }
}
