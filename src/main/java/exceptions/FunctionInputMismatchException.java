package exceptions;

/**
 * The exception to be thrown when the input of a function does not meet the requirements.
 * @author Louis Gosschalk
 */
public class FunctionInputMismatchException extends RuntimeException {
	/**
	 * Construct a new ColumnValueMismatchException.
	 *
	 * @param message The message you want ot add to this Exception.
	 */
	public FunctionInputMismatchException(String message) {
		super(message);
	}
}