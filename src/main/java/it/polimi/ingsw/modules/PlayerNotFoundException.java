package it.polimi.ingsw.modules;

public class PlayerNotFoundException extends Exception{
    public PlayerNotFoundException(){
        System.err.println("Player not found");
    }
}