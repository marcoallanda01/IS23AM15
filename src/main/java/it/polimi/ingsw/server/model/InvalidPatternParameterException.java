package it.polimi.ingsw.server.model;

public class InvalidPatternParameterException extends Exception {
    public InvalidPatternParameterException(){
        System.err.println("InvalidPatternParameterException occurred");
    }

    public InvalidPatternParameterException(String message) {
        super(message);
    }

    public InvalidPatternParameterException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public String toString() {
        return "InvalidPatternParameterException{}";
    }
}
