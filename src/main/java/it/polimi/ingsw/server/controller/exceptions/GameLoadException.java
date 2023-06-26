package it.polimi.ingsw.server.controller.exceptions;

/**
 * Exception used when there is a problem with the loading of the game
 */
public class GameLoadException extends Exception{

    /**
     * GameLoadException constructor. Use when there is a problem with the loading of the game.
     */
    public GameLoadException(){
        super();
        System.err.println("GameLoadException occurred");
    }

    /**
     * GameLoadException constructor. Use when there is a problem with the loading of the game.
     * @param game game name
     */
    public GameLoadException(String game) {
        super("Game <"+game+"> failed to load!");
    }

    /**
     * GameLoadException constructor. Use when there is a problem with the loading of the game.
     * @param game game name
     * @param cause cause of the exception
     */
    public GameLoadException(String game, Throwable cause) {
        super("Game <"+game+"> failed to load!", cause);
    }

    @Override
    public String toString() {
        return "GameLoadException{}";
    }

}
