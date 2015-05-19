package model.data;

/**
 * Exception that will be thrown when the name of a table is not set
 * Created by jens on 5/19/15.
 */
public class NameNotSetException extends Exception {
	public NameNotSetException(String message) {
		super(message);
	}
}