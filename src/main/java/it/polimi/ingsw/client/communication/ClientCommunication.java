package it.polimi.ingsw.client.communication;

import it.polimi.ingsw.server.model.Tile;
import it.polimi.ingsw.server.model.TileType;

import java.util.List;
import java.util.Set;

/**
 * this class should be implemented from the client class that sends messages
 * to the server, being it through RMI or TCP or other
 * this class can be used from the client (being it a controller, an
 * observer or directly the view) to send messages to the server
 * this class is thread blocking, the implementations of this class MUST be thread safe
 * the client is supposed to access this class asynchronously
 * the client MUST use only one instance of this class
 */
public interface ClientCommunication {

    // Methods for the connection and lobby creation
    public void hello();
    public void joinNewAsFirst(String player, int numPlayersGame, String idFirstPlayer);
    public void joinNewAsFirst(String player, int numPlayersGame, String idFirstPlayer, boolean easyRules);
    /**
     * Use this function to join the game without creating it
     * @param player player name
     * @return JoinResponse
     */
    public void join(String player);

    public void getSavedGames();
    public void loadGame(String game, String idFirstPlayer);
    /**
     * @return null if the game is not loaded, but created (or there is no game), else return the list of possible players
     */
    public void getLoadedGamePlayers();
    public void joinLoadedAsFirst(String player, String idFirstPlayer);
    /**
     * This function remove the player from the lobby if the game is not started, otherwise send it to
     * the WC (disconnection resilience)
     * @param playerId id of the player
     */
    public void disconnect(String playerId);
    public void reconnect(String playerId);
    // Methods for the play of the turn
    public void pickTiles(String playerId, Set<Tile> tiles);
    public void putTiles(String playerId, List<TileType> tiles, int column);
    public void sendMessage(String playerId, String message, String receiverNickname);

    void sendMessage(String playerId, String message);

    public void pong(String playerId);

}
