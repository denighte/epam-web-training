package by.radchuk.task3.exception;

/**
 * text exception class.
 */
public class TextException extends Exception {
    /**
     * Constructs a new exception with null as its detail message.
     */
    public TextException() {
        super();
    }
    /**
     * Constructs a new exception with the specified detail message.
     * @param message error message.
     */
    public TextException(final String message) {
        super(message);
    }

    /**
     * Constructs a new exception with the specified cause.
     * @param exception cause.
     */
    public TextException(final Throwable exception) {
        super(exception);
    }
}
