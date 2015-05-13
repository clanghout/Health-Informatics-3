package exceptions;

/**
 * Exception that is thrown when a string specifying a DataFile can 
 * not be deduced to an existing class.
 * @author Paul
 *
 */
public class DataFileNotRecognizedException extends RuntimeException {
	/**
	 * Construct a new DataFileNotFoundException.
	 *
	 * @param message The message you want to add to this Exception.
	 */
	public DataFileNotRecognizedException(String message) {
		super(message);
	}
}
