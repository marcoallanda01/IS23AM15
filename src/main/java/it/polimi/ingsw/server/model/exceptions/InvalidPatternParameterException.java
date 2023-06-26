package it.polimi.ingsw.server.model.exceptions;

/**
 * Pattern read was not consistent
 */
public class InvalidPatternParameterException extends Exception {
    private final String message;
    /**
     * InvalidPatternParameterException constructor
     */
    public InvalidPatternParameterException(){
        this.message = "InvalidPatternParameterException occurred";
        System.err.println(message);
    }

    /**
     * InvalidPatternParameterException constructor
     * @param message exception message
     */
    public InvalidPatternParameterException(String message) {
        super(message);
        this.message = message;
    }

    /**
     * InvalidPatternParameterException constructor
     * @param message exception message
     * @param cause exception cause
     */
    public InvalidPatternParameterException(String message, Throwable cause) {
        super(message, cause);
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "InvalidPatternParameterException{}";
    }
}
