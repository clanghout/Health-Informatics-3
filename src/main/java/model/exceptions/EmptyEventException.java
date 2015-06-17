package model.exceptions;

/**
 * The exception to be thrown when the input of LSA contains an empty event.
 * @author Louis Gosschalk
 */
public class EmptyEventException extends RuntimeException {
	/**
	 * Construct a new EmptyEventException.
	 *
	 * @param message The message you want ot add to this Exception.
	 */
	public EmptyEventException(String message) {
		super(message);
	}
}