package model.exceptions;

/**
 * The exception to be thrown when the amount of values doesn't match the amount of columns.
 *
 * Created by jens on 4/29/15.
 */
public class ColumnValueMismatchException extends RuntimeException {
	/**
	 * Construct a new ColumnValueMismatchException.
	 *
	 * @param message The message you want ot add to this Exception.
	 */
	public ColumnValueMismatchException(String message) {
		super(message);
	}
}