package model.exceptions;

/**
 * The exception to be thrown when the value type doesn't match the column type.
 *
 * Created by jens on 4/29/15.
 */
public class ColumnValueTypeMismatchException extends RuntimeException {
	/**
	 * Construct a new ColumnValueTypeMismatchException.
	 *
	 * @param message The message you want ot add to this Exception.
	 */
	public ColumnValueTypeMismatchException(String message) {
		super(message);
	}
}
