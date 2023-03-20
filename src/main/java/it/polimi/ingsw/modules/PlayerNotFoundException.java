package it.polimi.ingsw.modules;

public class PlayerNotFoundException extends Exception{
    public PlayerNotFoundException(){
        System.out.println("Player not found");
    }
}
