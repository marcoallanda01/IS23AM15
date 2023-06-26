package it.polimi.ingsw.client.communication;

import it.polimi.ingsw.server.model.Tile;
import it.polimi.ingsw.server.model.TileType;

import java.util.List;
import java.util.Set;

/**
 * this class should be implemented from the client class that sends messages
 * to the server, being it through RMI or TCP or other,
 * this class can be used from the client (being it a controller, an
 * observer or directly the view) to send messages to the server
 */
public interface ClientCommunication {

    /**
     * Hello to establish a connection.
     */
    void hello();

    /**
     * Requests to join a new game as the first player.
     *
     * @param player         the name of the player joining the game
     * @param numPlayersGame the number of players in the game
     * @param idFirstPlayer  the ID of the first player
     */
    void joinNewAsFirst(String player, int numPlayersGame, String idFirstPlayer);

    /**
     * Requests to join a new game as the first player with easy rules.
     *
     * @param player         the name of the player joining the game
     * @param numPlayersGame the number of players in the game
     * @param idFirstPlayer  the ID of the first player
     * @param easyRules      indicates whether to use easy rules for the game
     */
    void joinNewAsFirst(String player, int numPlayersGame, String idFirstPlayer, boolean easyRules);

    /**
     * Requests to join an existing game without creating it.
     *
     * @param player the name of the player joining the game
     */
    void join(String player);

    /**
     * Requests to get the list of saved games from the server.
     */
    void getSavedGames();

    /**
     * Requests to load a game from the server.
     *
     * @param game             The name of the game to be loaded.
     * @param idFirstPlayer    The ID of the first player.
     */
    void loadGame(String game, String idFirstPlayer);

    /**
     * Requests to get the list of players in a loaded game from the server.
     */
    void getLoadedGamePlayers();

    /**
     * Requests to join a loaded game as the first player.
     *
     * @param player         the name of the player joining the game
     * @param idFirstPlayer  the ID of the first player
     */
    void joinLoadedAsFirst(String player, String idFirstPlayer);

    /**
     * Requests to disconnect a player from the server.
     *
     * @param playerId The ID of the player to disconnect.
     */
    void disconnect(String playerId);

    /**
     * Requests to reconnect a player to the server.
     *
     * @param playerId The ID of the player to reconnect.
     */
    void reconnect(String playerId);

    /**
     * Requests to pick tiles for a player.
     *
     * @param playerId The ID of the player picking tiles.
     * @param tiles    The set of tiles to be picked.
     */
    void pickTiles(String playerId, Set<Tile> tiles);

    /**
     * Requests to put tiles in the bookshelf.
     *
     * @param playerId The ID of the player putting tiles.
     * @param tiles    The list of tiles to be placed.
     * @param column   The bookshelf column where the tiles are to be put.
     */
    void putTiles(String playerId, List<TileType> tiles, int column);

    /**
     * Requests to send a message from one player to another player.
     *
     * @param playerId         The ID of the player sending the message.
     * @param message          The content of the message.
     * @param receiverNickname The nickname of the message receiver.
     */
    void sendMessage(String playerId, String message, String receiverNickname);

    /**
     * Requests to send a message from one player to all players.
     *
     * @param playerId The ID of the player sending the message.
     * @param message  The content of the message.
     */
    void sendMessage(String playerId, String message);

    /**
     * Requests a ping, notifies that the player is active.
     *
     * @param playerId The ID of the player sending the pong.
     */
    void pong(String playerId);

    /**
     * Requests to save the game state.
     *
     * @param playerId  The ID of the player saving the game.
     * @param gameName  The name of the save.
     */
    void saveGame(String playerId, String gameName);
}