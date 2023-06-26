package it.polimi.ingsw.server.controller.exceptions;

/**
 * Exception used when the lobby is not full
 */
public class EmptyLobbyException extends Exception {

    /**
     * EmptyLobbyException constructor. Used when the lobby is not full.
     * @param numPlayers number of players in the lobby
     * @param size size of the lobby
     */
    public EmptyLobbyException(int numPlayers, int size) {
        super("EmptyLobbyException occurred, players "+numPlayers+"/"+size+"!");
    }
}
