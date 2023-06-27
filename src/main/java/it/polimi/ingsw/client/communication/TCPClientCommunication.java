package it.polimi.ingsw.client.communication;

import it.polimi.ingsw.communication.commands.*;
import it.polimi.ingsw.server.model.Tile;
import it.polimi.ingsw.server.model.TileType;

import java.util.List;
import java.util.Set;

/**
 * TCP based implementation of ClientCommunication
 * uses a TCPClientConnection to send messages to the server
 */
public class TCPClientCommunication implements ClientCommunication {
    private TCPClientConnection tcpClientConnection;
    /**
     * @param tcpClientConnection the implementation of the connection
     */
    public TCPClientCommunication(TCPClientConnection tcpClientConnection) {
        this.tcpClientConnection = tcpClientConnection;
    }
    /**
     * Sends a Hello to the server.
     */
    @Override
    public void hello() {
        tcpClientConnection.sendToServer((new HelloCommand()).toJson());
    }

    /**
     * Sends a JoinNewAsFirst to the server.
     *
     * @param player          The nickname of the player joining the game.
     * @param numPlayersGame  The number of players in the game.
     * @param idFirstPlayer   The ID of the first player.
     */
    @Override
    public void joinNewAsFirst(String player, int numPlayersGame, String idFirstPlayer) {
        joinNewAsFirst(player, numPlayersGame, idFirstPlayer, false);
    }

    /**
     * Sends a JoinNewAsFirst to the server.
     *
     * @param player          The nickname of the player joining the game.
     * @param numPlayersGame  The number of players in the game.
     * @param idFirstPlayer   The ID of the first player.
     * @param easyRules       A flag indicating whether to use easy rules.
     */
    @Override
    public void joinNewAsFirst(String player, int numPlayersGame, String idFirstPlayer, boolean easyRules) {
        tcpClientConnection.sendToServer((new JoinNewAsFirst(player, numPlayersGame, idFirstPlayer, easyRules)).toJson());
    }

    /**
     * Sends a GetSavedGames to the server.
     */
    @Override
    public void getSavedGames() {
        tcpClientConnection.sendToServer((new GetSavedGames()).toJson());
    }

    /**
     * Sends a LoadGame to the server.
     *
     * @param game            The name of the game save to load.
     * @param idFirstPlayer   The ID of the first player.
     */
    @Override
    public void loadGame(String game, String idFirstPlayer) {
        tcpClientConnection.sendToServer((new LoadGame(idFirstPlayer, game)).toJson());
    }

    /**
     * Sends a GetLoadedPlayers to the server.
     */
    @Override
    public void getLoadedGamePlayers() {
        tcpClientConnection.sendToServer((new GetLoadedPlayers()).toJson());
    }

    /**
     * Sends a JoinLoadedAsFirst to the server.
     *
     * @param player          The nickname of the player joining the game.
     * @param idFirstPlayer   The ID of the first player.
     */
    @Override
    public void joinLoadedAsFirst(String player, String idFirstPlayer) {
        tcpClientConnection.sendToServer((new JoinLoadedAsFirst(player, idFirstPlayer)).toJson());
    }

    /**
     * Sends a Join to the server.
     *
     * @param player  The nickname of the player joining the game.
     */
    @Override
    public void join(String player) {
        tcpClientConnection.sendToServer((new Join(player)).toJson());
    }

    /**
     * Sends a Disconnect to the server.
     *
     * @param playerId  The ID of the player to disconnect.
     */
    @Override
    public void disconnect(String playerId) {
        tcpClientConnection.sendToServer((new Disconnect(playerId)).toJson());
        tcpClientConnection.closeConnection();
    }

    /**
     * Sends a Reconnect to the server.
     *
     * @param playerId  The ID of the player to reconnect.
     */
    @Override
    public void reconnect(String playerId) {
        tcpClientConnection.sendToServer((new Reconnect(playerId)).toJson());
    }

    /**
     * Sends a PickTilesCommand to the server.
     *
     * @param playerId  The ID of the player picking tiles.
     * @param tiles     The set of tiles to be picked.
     */
    @Override
    public void pickTiles(String playerId, Set<Tile> tiles) {
        tcpClientConnection.sendToServer((new PickTilesCommand(playerId, tiles)).toJson());
    }

    /**
     * Sends a PutTilesCommand to the server.
     *
     * @param playerId  The ID of the player putting tiles.
     * @param tiles     The list of tiles to be placed.
     * @param column    The column where the tiles will be placed.
     */
    @Override
    public void putTiles(String playerId, List<TileType> tiles, int column) {
        tcpClientConnection.sendToServer((new PutTilesCommand(playerId, tiles, column)).toJson());
    }

    /**
     * Sends a SendMessage to the server (to a specific player).
     *
     * @param playerId          The ID of the player sending the message.
     * @param message           The message to be sent.
     * @param receiverNickname  The nickname of the message receiver.
     */
    @Override
    public void sendMessage(String playerId, String message, String receiverNickname) {
        tcpClientConnection.sendToServer((new SendMessage(playerId, message, receiverNickname)).toJson());
    }

    /**
     * Sends a SendMessage to the server (to all players).
     *
     * @param playerId  The ID of the player sending the message.
     * @param message   The message to be sent.
     */
    @Override
    public void sendMessage(String playerId, String message) {
        tcpClientConnection.sendToServer((new SendMessage(playerId, message)).toJson());
    }

    /**
     * Sends a Pong to the server.
     *
     * @param playerId  The ID of the player sending the pong.
     */
    @Override
    public void pong(String playerId) {
        tcpClientConnection.sendToServer((new Pong(playerId)).toJson());
    }

    /**
     * Sends a SaveGame to the server.
     *
     * @param playerId   The ID of the player saving the game.
     * @param gameName   The name of the save.
     */
    @Override
    public void saveGame(String playerId, String gameName) {
        tcpClientConnection.sendToServer((new SaveGame(playerId, gameName)).toJson());
    }
}
