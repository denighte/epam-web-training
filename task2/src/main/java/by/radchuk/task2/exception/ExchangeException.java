package by.radchuk.task2.exception;

/**
 * Default exception for all stock operation errors.
 */
public class ExchangeException extends Exception {
    /**
     * Constructs a new exception with null as its detail message.
     */
    public ExchangeException() {
        super();
    }
    /**
     * Constructs a new exception with the specified detail message.
     * @param message error message.
     */
    public ExchangeException(final String message) {
        super(message);
    }
}
