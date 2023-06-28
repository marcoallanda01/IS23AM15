package it.polimi.ingsw.server.model.exceptions;

/**
 * Exception used when it's asked something abut a player that doesn't exists
 */
public class PlayerNotFoundException extends Exception{

    /**
     * PlayerNotFoundException constructor
     */
    public PlayerNotFoundException(){
        super();
        System.err.println("Player not found");
    }

    /**
     * PlayerNotFoundException constructor
     * @param message exception message
     */
    public PlayerNotFoundException(String message) {
        super(message);
    }
}
