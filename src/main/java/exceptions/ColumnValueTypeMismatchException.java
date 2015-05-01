package exceptions;

/**
 * Created by jens on 4/29/15.
 */
public class ColumnValueTypeMismatchException extends RuntimeException {
    public ColumnValueTypeMismatchException(String message) {
        super(message);
    }
}
