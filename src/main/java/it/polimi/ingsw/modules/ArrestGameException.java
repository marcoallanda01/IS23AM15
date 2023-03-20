package it.polimi.ingsw.modules;

public class ArrestGameException extends Exception{


    public ArrestGameException() {
        System.err.println("ArrestGameException occurred");
    }

    public ArrestGameException(String message) {
        super(message);
    }

    public ArrestGameException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public String toString() {
        return "ArrestGameException{}";
    }
}
