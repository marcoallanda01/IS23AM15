package it.polimi.ingsw.server.model.exceptions;

/**
 * Class that represents an exception thrown while creating a pattern using invalid parameters
 */
public class InvalidPatternParameterException extends Exception {
    private String message;

    /**
     * Default constructor
     */
    public InvalidPatternParameterException(){
        super("InvalidPatternParameterException occurred");
        this.message = "InvalidPatternParameterException occurred";
    }

    /**
     * Creates a runtime exception with message as its detail message,
     * it also sets the private attribute message to message
     * @param message the message
     */
    public InvalidPatternParameterException(String message) {
        super(message);
        this.message = message;
    }

    /**
     * Creates a runtime exception with message as its detail message and cause as the cause,
     * it also sets the private attribute message to message
     * @param message the message
     * @param cause the cause
     */
    public InvalidPatternParameterException(String message, Throwable cause) {
        super(message, cause);
        this.message = message;
    }

    /**
     * Gets the message
     * @return the message
     */
    @Override
    public String getMessage() {
        return message;
    }
}
