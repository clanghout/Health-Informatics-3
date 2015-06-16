package model.exceptions;

/**
 * The exception to be thrown when the input of a function does not meet the requirements.
 * @author Louis Gosschalk
 */
public class InputMismatchException extends RuntimeException {
	/**
	 * Construct a new ColumnValueMismatchException.
	 *
	 * @param message The message you want ot add to this Exception.
	 */
	public InputMismatchException(String message) {
		super(message);
	}
}