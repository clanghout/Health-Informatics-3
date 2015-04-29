package exceptions;

/**
 * Created by jens on 4/29/15.
 */
public class ColumnValueTypeMismatchEception extends RuntimeException {
    public ColumnValueTypeMismatchEception(String message) {
        super(message);
    }
}
