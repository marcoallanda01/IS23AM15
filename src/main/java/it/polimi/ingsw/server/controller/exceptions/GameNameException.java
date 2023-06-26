package it.polimi.ingsw.server.controller.exceptions;


/**
 * Exception used when game with a specific name is not available
 */
public class GameNameException extends Exception {

    /**
     * GameNameException constructor. Use when game with a specific name is not available.
     */
    public GameNameException(){
        super();
        System.err.println("GameNameException occurred");
    }

    /**
     * GameNameException constructor. Use when game with a specific name is not available.
     * @param name game name
     */
    public GameNameException(String name) {
        super("Game <"+name+"> do not exists!");
    }

    @Override
    public String toString() {
        return "GameNameException{}";
    }
}
