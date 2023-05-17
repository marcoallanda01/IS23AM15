package it.polimi.ingsw.server.controller.exceptions;

public class SaveException extends Exception{
    public SaveException(){
        System.err.println(this +"Error saving the game!");
    }

    public SaveException(String message){
        System.err.println(this +message);
    }
}
