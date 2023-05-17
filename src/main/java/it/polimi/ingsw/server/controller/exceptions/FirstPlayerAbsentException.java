package it.polimi.ingsw.server.controller.exceptions;

public class FirstPlayerAbsentException extends Exception{
    public FirstPlayerAbsentException(){
        System.err.println("FirstPlayerAbsentException occurred");
    }

    public FirstPlayerAbsentException(Throwable cause) {
        super("FirstPlayerAbsentException occurred!", cause);
    }

    @Override
    public String toString() {
        return "FirstPlayerAbsentException{}";
    }
}
