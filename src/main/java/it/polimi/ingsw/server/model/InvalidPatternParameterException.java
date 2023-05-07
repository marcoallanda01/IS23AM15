package it.polimi.ingsw.server.model;

public class InvalidPatternParameterException extends Exception {
    private String message;
    public InvalidPatternParameterException(){
        this.message = "InvalidPatternParameterException occurred";
        System.err.println(message);
    }

    public InvalidPatternParameterException(String message) {
        super(message);
        this.message = message;
    }

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
