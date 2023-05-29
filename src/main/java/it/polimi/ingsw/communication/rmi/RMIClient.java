package it.polimi.ingsw.communication.rmi;

import it.polimi.ingsw.communication.responses.GameSetUp;
import it.polimi.ingsw.server.model.Tile;
import it.polimi.ingsw.server.model.TileType;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * The RMIClient interface defines the remote methods that can be called by the server to notify the client of various events.
 */
public interface RMIClient extends Remote {
    /**
     * Notifies the client about the game setup.
     *
     * @param gameSetUp The GameSetUp object containing the game setup information.
     * @throws RemoteException if a remote communication error occurs.
     */
    void notifyGame(GameSetUp gameSetUp) throws RemoteException;

    /**
     * Notifies the client about the winner of the game.
     *
     * @param nickname The nickname of the winning player.
     * @throws RemoteException if a remote communication error occurs.
     */
    void notifyWinner(String nickname) throws RemoteException;

    /**
     * Notifies the client about the updated board state.
     *
     * @param tiles The set of tiles representing the current state of the board.
     * @throws RemoteException if a remote communication error occurs.
     */
    void notifyBoard(Set<Tile> tiles) throws RemoteException;

    /**
     * Notifies the client about the bookshelf state of a player.
     *
     * @param nickname The nickname of the player.
     * @param tiles    The set of tiles representing the player's bookshelf.
     * @throws RemoteException if a remote communication error occurs.
     */
    void notifyBookshelf(String nickname, Set<Tile> tiles) throws RemoteException;

    /**
     * Notifies the client about the updated points of a player.
     *
     * @param nickname The nickname of the player.
     * @param points   The updated points of the player.
     * @throws RemoteException if a remote communication error occurs.
     */
    void notifyPoints(String nickname, int points) throws RemoteException;

    /**
     * Notifies the client about the current turn of a player.
     *
     * @param nickname The nickname of the player who has the current turn.
     * @throws RemoteException if a remote communication error occurs.
     */
    void notifyTurn(String nickname) throws RemoteException;

    /**
     * Notifies the client about the personal goal card of a player.
     *
     * @param nickname The nickname of the player.
     * @param card     The personal goal card of the player.
     * @throws RemoteException if a remote communication error occurs.
     */
    void notifyPersonalGoalCard(String nickname, String card) throws RemoteException;

    /**
     * Notifies the client about the common cards and their associated tokens.
     *
     * @param cardsToTokens The mapping of common cards to their tokens.
     * @throws RemoteException if a remote communication error occurs.
     */
    void notifyCommonCards(Map<String, List<Integer>> cardsToTokens) throws RemoteException;

    /**
     * Notifies the client about the available common goals.
     *
     * @param goals The set of available common goals.
     * @throws RemoteException if a remote communication error occurs.
     */
    void notifyCommonGoals(Set<String> goals) throws RemoteException;

    /**
     * Notifies the client about a chat message sent by a player.
     *
     * @param nickname The nickname of the player who sent the message.
     * @param message  The chat message.
     * @param date     The date of the chat message.
     * @throws RemoteException if a remote communication error occurs.
     */
    void notifyChatMessage(String nickname, String message, String date) throws RemoteException;

    /**
     * Notifies the client about the disconnection of a player.
     *
     * @param nickname The nickname of the disconnected player.
     * @throws RemoteException if a remote communication error occurs.
     */
    void notifyDisconnection(String nickname) throws RemoteException;

    /**
     * Notifies the client that the game has been successfully saved.
     *
     * @param game The name of the saved game.
     * @throws RemoteException if a remote communication error occurs.
     */
    void notifyGameSaved(String game) throws RemoteException;

    /**
     * Notifies the client about a ping message from the server.
     *
     * @throws RemoteException if a remote communication error occurs.
     */
    void notifyPing() throws RemoteException;

    /**
     * Notifies the client about the reconnection of a player.
     *
     * @param nickname The nickname of the reconnected player.
     * @throws RemoteException if a remote communication error occurs.
     */
    void notifyReconnection(String nickname) throws RemoteException;

    /**
     * Notifies the client about an error occurred on the server.
     *
     * @param message The error message.
     * @throws RemoteException if a remote communication error occurs.
     */
    void notifyError(String message) throws RemoteException;

    /**
     * Notifies the client about the tiles picked by a player.
     *
     * @param nickname The nickname of the player who picked the tiles.
     * @param tiles    The list of tiles picked by the player.
     * @throws RemoteException if a remote communication error occurs.
     */
    void notifyPickedTiles(String nickname, List<TileType> tiles) throws RemoteException;
}