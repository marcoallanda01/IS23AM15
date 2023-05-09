package it.polimi.ingsw.client.communication;

import it.polimi.ingsw.communication.commands.*;
import it.polimi.ingsw.communication.responses.*;
import it.polimi.ingsw.server.model.Tile;
import it.polimi.ingsw.server.model.TileType;

import java.math.BigInteger;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * this class is a TCP based implementation of ClientCommunication
 * it uses a TCPClientClientConnection to send messages to the server
 */
public class TCPClientCommunication implements ClientCommunication {
    private TCPClientClientConnection tcpClientConnection;
    /**
     * @param tcpClientConnection the implementation of the connection
     */
    public TCPClientCommunication(TCPClientClientConnection tcpClientConnection) {
        this.tcpClientConnection = tcpClientConnection;
    }
    /**
     * this method sends a Hello to the server
     */
    @Override
    public void hello() {
        tcpClientConnection.sendToServer((new HelloCommand()).toJson());
    }
    /**
     * this method sends a JoinNewAsFirst to the server
     */
    @Override
    public void joinNewAsFirst(String player, int numPlayersGame, String idFirstPlayer) {
        joinNewAsFirst(player, numPlayersGame, idFirstPlayer, false);
    }
    /**
     * this method sends a JoinNewAsFirst to the server
     */
    @Override
    public void joinNewAsFirst(String player, int numPlayersGame, String idFirstPlayer, boolean easyRules) {
        tcpClientConnection.sendToServer((new JoinNewAsFirst(player, numPlayersGame, idFirstPlayer, easyRules)).toJson());
    }
    /**
     * this method sends a GetSavedGames to the server
     */
    @Override
    public void getSavedGames() {
        tcpClientConnection.sendToServer((new GetSavedGames()).toJson());
    }
    /**
     * this method sends a LoadGame to the server
     */
    @Override
    public void loadGame(String game, String idFirstPlayer) {
        tcpClientConnection.sendToServer((new LoadGame(idFirstPlayer, game)).toJson());
    }
    /**
     * this method sends a GetLoadedPlayers to the server
     */
    @Override
    public void getLoadedGamePlayers() {
        tcpClientConnection.sendToServer((new GetLoadedPlayers()).toJson());
    }
    /**
     * this method sends a JoinLoadedAsFirst to the server
     */
    @Override
    public void joinLoadedAsFirst(String player, String idFirstPlayer) {
        tcpClientConnection.sendToServer((new JoinLoadedAsFirst(player, idFirstPlayer)).toJson());
    }
    /**
     * this method sends a Join to the server
     */
    @Override
    public void join(String player) {
        tcpClientConnection.sendToServer((new Join(player)).toJson());
    }
    /**
     * this method sends a Disconnect to the server
     */
    @Override
    public void disconnect(String playerId) {
        tcpClientConnection.sendToServer((new Disconnect(playerId)).toJson());
    }
    /**
     * this method sends a Reconnect to the server
     */
    @Override
    public void reconnect(String playerId) {
        tcpClientConnection.sendToServer((new Reconnect(playerId)).toJson());
    }
    /**
     * this method sends a PickTilesCommand to the server
     */
    @Override
    public void pickTiles(String playerId, Set<Tile> tiles) {
        tcpClientConnection.sendToServer((new PickTilesCommand(playerId, tiles)).toJson());
    }
    /**
     * this method sends a PutTilesCommand to the server
     */
    @Override
    public void putTiles(String playerId, List<TileType> tiles, int column) {
        tcpClientConnection.sendToServer((new PutTilesCommand(playerId, tiles, column)).toJson());
    }
    /**
     * this method sends a SendMessage to the server
     */
    @Override
    public void sendMessage(String playerId, String player, String message) {
        tcpClientConnection.sendToServer((new SendMessage(playerId, player, message)).toJson());
    }
    /**
     * this method sends a Pong to the server, doesn't wait
     */
    @Override
    public void pong(String playerId) {
        tcpClientConnection.sendToServer((new Pong(playerId)).toJson());
    }

}
