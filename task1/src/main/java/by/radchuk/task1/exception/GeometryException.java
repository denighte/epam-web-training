package by.radchuk.task1.exception;


/**
 * Standard exception for all geometry classes.
 */
public class GeometryException extends RuntimeException {
    /**
     * constructor from a string.
     * @param message error message.
     */
    public GeometryException(final String message) {
        super(message);
    }

    /**
     * constructor from an exception - cause.
     * @param cause cause exception
     */
    public GeometryException(final Throwable cause) {
        super(cause);
    }
}
