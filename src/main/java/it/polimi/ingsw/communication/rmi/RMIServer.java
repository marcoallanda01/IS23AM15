package it.polimi.ingsw.communication.rmi;

import it.polimi.ingsw.communication.responses.*;
import it.polimi.ingsw.server.controller.ChatController;
import it.polimi.ingsw.server.controller.Lobby;
import it.polimi.ingsw.server.controller.PlayController;
import it.polimi.ingsw.server.model.Tile;
import it.polimi.ingsw.server.model.TileType;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.*;

public interface RMIServer extends Remote {
    /**
     * HelloCommand to server
     * @param client RMIClient that says hello
     * @return Hello response
     * @throws RemoteException if something about connection went bad
     */
    public Hello hello(RMIClient client) throws RemoteException;

    /**
     * join as first player in a new game
     * @param client RMI client that joins
     * @param player player's name
     * @param numPlayersGame num of players of new game
     * @param idFirstPlayer first player's id
     * @return FirstJoinResponse
     * @throws RemoteException if something about connection went bad
     */
    public FirstJoinResponse joinNewAsFirst(RMIClient client, String player, int numPlayersGame, String idFirstPlayer) throws RemoteException;
    /**
     * join as first player in a new game
     * @param client RMI client that joins
     * @param player player's name
     * @param numPlayersGame num of players of new game
     * @param idFirstPlayer first player's id
     * @param easyRules set true for easy rules game mode
     * @return FirstJoinResponse
     * @throws RemoteException if something about connection went bad
     */
    public FirstJoinResponse joinNewAsFirst(RMIClient client, String player, int numPlayersGame, String idFirstPlayer, boolean easyRules) throws RemoteException;
    /**
     * join in a created game
     * @param client RMI client that joins
     * @param player player's name
     * @return JoinResponse
     * @throws RemoteException if something about connection went bad
     */
    public JoinResponse join(RMIClient client, String player) throws RemoteException;
    /**
     * Get the game that are saved on the server
     * @return SavedGame response
     * @throws RemoteException if something about connection went bad
     */
    public SavedGames getSavedGames() throws RemoteException;
    /**
     * load a game from a save
     * @param game game name
     * @param idFirstPlayer first player's id
     * @return LoadGameResponse
     * @throws RemoteException if something about connection went bad
     */
    public LoadGameResponse loadGame(String game, String idFirstPlayer) throws RemoteException;
    /**
     * Get player's nicknames from the game loaded
     * @return LoadedGamePlayers response
     * @throws RemoteException if something about connection went bad
     */
    public LoadedGamePlayers getLoadedGamePlayers() throws RemoteException;
    /**
     * join as first player in a loaded game
     * @param client RMI client that joins
     * @param player player's name
     * @param idFirstPlayer first player's id
     * @return FirstJoinResponse
     * @throws RemoteException if something about connection went bad
     */
    public FirstJoinResponse joinLoadedAsFirst(RMIClient client, String player, String idFirstPlayer) throws RemoteException;
    /**
     * Disconnect from the game
     * @param playerId player's id
     * @throws RemoteException if something about connection went bad
     */
    public void disconnect(String playerId) throws RemoteException;
    /**
     * Reconnect to the game
     * @param playerId player's id
     * @throws RemoteException if something about connection went bad
     */
    public void reconnect(RMIClient client, String playerId) throws RemoteException;
    /**
     * Pick tiles command
     * @param playerId player's id
     * @param tiles tiles to pick
     * @throws RemoteException if something about connection went bad
     */
    public void pickTiles(String playerId, Set<Tile> tiles) throws RemoteException;
    /**
     * Put tiles command
     * @param playerId player's id
     * @param tiles tiles to put in order
     * @param column where to put the tiles
     * @throws RemoteException if something about connection went bad
     */
    public void putTiles(String playerId, List<TileType> tiles, int column) throws RemoteException;
    /**
     * Save current game command
     * @param playerId player's id
     * @param gameName name of the new save
     * @throws RemoteException if something about connection went bad
     */
    public void saveGame(String playerId, String gameName) throws RemoteException;
    /**
     * Send message to a player
     * @param playerId player's id
     * @param message actual message
     * @param receiverNickname receiver's name
     * @throws RemoteException if something about connection went bad
     */
    public void sendMessage(String playerId, String message, String receiverNickname) throws RemoteException;

    /**
     * Send message to all players
     * @param playerId player's id
     * @param message actual message
     * @throws RemoteException if something about connection went bad
     */
    public void sendMessage(String playerId, String message) throws RemoteException;

    /**
     * Respond to a ping
     * @param playerId player's id
     * @throws RemoteException if something about connection went bad
     */
    public void pong(String playerId) throws RemoteException;
}
