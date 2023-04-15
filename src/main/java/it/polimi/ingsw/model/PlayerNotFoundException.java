package it.polimi.ingsw.model;

public class PlayerNotFoundException extends Exception{
    public PlayerNotFoundException(){
        System.err.println("Player not found");
    }
}
