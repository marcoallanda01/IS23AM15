package it.polimi.ingsw.server.model;

public class PlayerNotFoundException extends Exception{
    public PlayerNotFoundException(){
        System.err.println("Player not found");
    }
    public PlayerNotFoundException(String message) {
        super(message);
    }
}