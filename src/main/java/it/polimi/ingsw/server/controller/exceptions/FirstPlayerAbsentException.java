package it.polimi.ingsw.server.controller.exceptions;

public class FirstPlayerAbsentException extends Exception{

    /**
     * FirstPlayerAbsentException constructor. Use when the lobby has no first player.
     */
    public FirstPlayerAbsentException(){
        super();
        System.err.println("FirstPlayerAbsentException occurred");
    }

    @Override
    public String toString() {
        return "FirstPlayerAbsentException{}";
    }
}
