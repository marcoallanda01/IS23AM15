package it.polimi.ingsw.client;

/**
 * Enum that represents the possible states of the client
 */
public enum ClientStates {
    /**
     * The client is starting up
     */
    STARTUP,
    /**
     * The client is logging in
     */
    LOGIN,
    /**
     * The client is creating a lobby
     */
    CREATE_LOBBY,
    /**
     * The client is creating a game
     */
    CREATE_GAME,
    /**
     * The client is loading a game
     */
    LOAD_GAME,
    /**
     * The client is choosing a name
     */
    LOAD_NAMES,
    /**
     * The client is joining a lobby
     */
    LOBBY,
    /**
     * The client is playing a game
     */
    IN_GAME,
    /**
     * The client is in the end game screen
     */
    END_GAME
}
