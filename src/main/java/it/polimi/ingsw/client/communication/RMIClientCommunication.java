package it.polimi.ingsw.client.communication;

import it.polimi.ingsw.communication.responses.*;
import it.polimi.ingsw.communication.rmi.RMIClient;
import it.polimi.ingsw.communication.rmi.RMIServer;
import it.polimi.ingsw.server.model.Tile;
import it.polimi.ingsw.server.model.TileType;

import java.rmi.RemoteException;
import java.util.List;
import java.util.Set;

/**
 * this class is an RMI based implementation of ClientCommunication
 * it uses a RMIClientClientConnection to call methods on the server
 * note that some methods expect a return value blocking the thread
 */
public class RMIClientCommunication implements ClientCommunication {

    private RMIClientClientConnection rmiClientConnection;
    /**
     * @param rmiClientConnection the implementation of the connection
     */
    public RMIClientCommunication(RMIClientClientConnection rmiClientConnection) {
        this.rmiClientConnection = rmiClientConnection;
    }
    /**
     * this method calls Hello on the server and waits for its response (blocking its thread)
     */
    @Override
    public void hello() {
        try {
            Hello hello = rmiClientConnection.getServer().hello();
            rmiClientConnection.notifyHello(hello.lobbyReady, hello.firstPlayerId, hello.loadedGame);
        } catch (RemoteException e) {
            throw new ClientCommunicationException();
        }
    }
    /**
     * this method calls JoinNewAsFirst on the server and waits for its response (thread blocking)
     */
    @Override
    public void joinNewAsFirst(String player, int numPlayersGame, String idFirstPlayer) {
        try {
            rmiClientConnection.getServer().joinNewAsFirst(rmiClientConnection, player, numPlayersGame, idFirstPlayer);
        } catch (RemoteException e) {
            throw new ClientCommunicationException();
        }
    }
    /**
     * this method calls JoinNewAsFirst on the server and waits for its response (thread blocking)
     */
    @Override
    public void joinNewAsFirst(String player, int numPlayersGame, String idFirstPlayer, boolean easyRules) {
        try {
            rmiClientConnection.getServer().joinNewAsFirst(rmiClientConnection, player, numPlayersGame, idFirstPlayer, true);
        } catch (RemoteException e) {
            throw new ClientCommunicationException();
        }
    }
    /**
     * this method calls GetSavedGames on the server and waits for its response (thread blocking)
     */
    @Override
    public void getSavedGames() {
        try {
            SavedGames savedGames = rmiClientConnection.getServer().getSavedGames();
            rmiClientConnection.notifySavedGames(savedGames.names);
        } catch (RemoteException e) {
            throw new ClientCommunicationException();
        }
    }
    /**
     * this method calls LoadGame on the server and waits for its response (thread blocking)
     */
    @Override
    public void loadGame(String game, String idFirstPlayer) {
        try {
            LoadGameResponse loadGameResponse = rmiClientConnection.getServer().loadGame(game, idFirstPlayer);
            rmiClientConnection.notifyLoadGameResponse(loadGameResponse.result, loadGameResponse.error);
        } catch (RemoteException e) {
            throw new ClientCommunicationException();
        }
    }
    /**
     * this method calls GetLoadedPlayers on the server and waits for its response (thread blocking)
     */
    @Override
    public void getLoadedGamePlayers() {
        try {
            LoadedGamePlayers loadedGamePlayers = rmiClientConnection.getServer().getLoadedGamePlayers();
            rmiClientConnection.notifyLoadedGamePlayers(loadedGamePlayers.names);
        } catch (RemoteException e) {
            throw new ClientCommunicationException();
        }
    }
    /**
     * this method calls JoinLoadedAsFirst on the server and waits for its response (thread blocking)
     */
    @Override
    public void joinLoadedAsFirst(String player, String idFirstPlayer) {
        try {
            rmiClientConnection.getServer().joinLoadedAsFirst(rmiClientConnection, player, idFirstPlayer);
        } catch (RemoteException e) {
            throw new ClientCommunicationException();
        }
    }
    /**
     * this method calls Join on the server and waits for its response (thread blocking)
     */
    @Override
    public void join(String player) {
        try {
            JoinResponse joinResponse = rmiClientConnection.getServer().join(rmiClientConnection, player);
            rmiClientConnection.notifyJoinResponse(joinResponse.result, joinResponse.error, joinResponse.id);
        } catch (RemoteException e) {
            throw new ClientCommunicationException();
        }
    }
    /**
     * this method calls Disconnect on the server and waits for its response (thread blocking)
     */
    @Override
    public void disconnect(String playerId) {
        try {
            rmiClientConnection.getServer().disconnect(playerId);
        } catch (RemoteException e) {
            throw new ClientCommunicationException();
        }
    }
    /**
     * this method calls Reconnect on the server and waits for its response (thread blocking)
     */
    @Override
    public void reconnect(String playerId) {
        try {
            rmiClientConnection.getServer().reconnect(rmiClientConnection, playerId);
        } catch (RemoteException e) {
            throw new ClientCommunicationException();
        }
    }
    /**
     * this method calls PickTilesCommand on the server and waits for its response (thread blocking)
     */
    @Override
    public void pickTiles(String playerId, Set<Tile> tiles) {
        try {
            rmiClientConnection.getServer().pickTiles(playerId, tiles);
        } catch (RemoteException e) {
            throw new ClientCommunicationException();
        }
    }
    /**
     * this method calls PutTilesCommand on the server and waits for its response (thread blocking)
     */
    @Override
    public void putTiles(String playerId, List<TileType> tiles, int column) {
        try {
            rmiClientConnection.getServer().putTiles(playerId, tiles, column);
        } catch (RemoteException e) {
            throw new ClientCommunicationException();
        }
    }
    /**
     * this method calls SendMessage on the server and waits for its response (thread blocking)
     */
    @Override
    public void sendMessage(String playerId, String player, String message) {
        try {
            rmiClientConnection.getServer().sendMessage(playerId, player, message);
        } catch (RemoteException e) {
            throw new ClientCommunicationException();
        }
    }
    /**
     * this method calls pong on the server, doesn't wait
     */
    @Override
    public void pong(String playerId) {
        try {
            rmiClientConnection.getServer().pong(playerId);
        } catch (RemoteException e) {
            throw new ClientCommunicationException();
        }
    }
}
