package it.polimi.ingsw.server.controller.exceptions;

public class SaveException extends Exception{

    /**
     * SaveException constructor. Use when there is a problem while game is saving
     * @param message message to print
     */
    public SaveException(String message){
        super(message);
        System.err.println(this +message);
    }
}
