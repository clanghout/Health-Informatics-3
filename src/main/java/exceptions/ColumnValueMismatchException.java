package exceptions;

/**
 * Created by jens on 4/29/15.
 */
public class ColumnValueMismatchException extends RuntimeException {
    public ColumnValueMismatchException(String message) {
        super(message);
    }
}