package it.polimi.ingsw.server.controller.exceptions;

public class GameLoadException extends Exception{

    public GameLoadException(){
        System.err.println("GameLoadException occurred");
    }

    public GameLoadException(String game) {
        super("Game <"+game+"> failed to load!");
    }

    public GameLoadException(String game, Throwable cause) {
        super("Game <"+game+"> failed to load!", cause);
    }

    @Override
    public String toString() {
        return "GameLoadException{}";
    }

}
