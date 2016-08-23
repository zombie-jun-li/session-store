package exception;

/**
 * Created by li on 2016/8/6.
 */
public class NullException extends RuntimeException {
    public NullException() {
        super();
    }

    public NullException(String message) {
        super(message);
    }
}
