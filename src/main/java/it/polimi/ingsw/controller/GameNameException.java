package it.polimi.ingsw.controller;

public class GameNameException extends Exception {
    public GameNameException(){
        System.err.println("GameNameException occurred");
    }

    public GameNameException(String name) {
        super("Game <"+name+"> do not exists!");
    }

    @Override
    public String toString() {
        return "GameNameException{}";
    }
}
