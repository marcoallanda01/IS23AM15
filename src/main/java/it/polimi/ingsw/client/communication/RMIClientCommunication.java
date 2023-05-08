package it.polimi.ingsw.client.communication;

import it.polimi.ingsw.communication.responses.*;
import it.polimi.ingsw.server.model.Tile;
import it.polimi.ingsw.server.model.TileType;

import java.rmi.RemoteException;
import java.util.List;
import java.util.Set;

/**
 * this class is an RMI based implementation of ClientCommunication
 * it uses a RMIClientClientConnection to call methods on the server
 * note that all of its methods block the main thread, therefore
 * it is advised to call them asynchronously or use multiple instances of ClientCommunication
 * NB: this class is thread safe because RMI is thread safe
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
    public Hello hello() {
        try {
            return this.rmiClientConnection.getServer().hello();
        } catch (RemoteException e) {
            throw new ClientCommunicationException();
        }
    }
    /**
     * this method calls JoinNewAsFirst on the server and waits for its response (thread blocking)
     */
    @Override
    public BooleanResponse joinNewAsFirst(String player, int numPlayersGame, String idFirstPlayer) {
        try {
            return this.rmiClientConnection.getServer().joinNewAsFirst(player, numPlayersGame, idFirstPlayer);
        } catch (RemoteException e) {
            throw new ClientCommunicationException();
        }
    }
    /**
     * this method calls JoinNewAsFirst on the server and waits for its response (thread blocking)
     */
    @Override
    public BooleanResponse joinNewAsFirst(String player, int numPlayersGame, String idFirstPlayer, boolean easyRules) {
        try {
            return this.rmiClientConnection.getServer().joinNewAsFirst(player, numPlayersGame, idFirstPlayer, true);
        } catch (RemoteException e) {
            throw new ClientCommunicationException();
        }
    }
    /**
     * this method calls GetSavedGames on the server and waits for its response (thread blocking)
     */
    @Override
    public SavedGames getSavedGames() {
        try {
            return this.rmiClientConnection.getServer().getSavedGames();
        } catch (RemoteException e) {
            throw new ClientCommunicationException();
        }
    }
    /**
     * this method calls LoadGame on the server and waits for its response (thread blocking)
     */
    @Override
    public LoadGameResponse loadGame(String game, String idFirstPlayer) {
        try {
            return this.rmiClientConnection.getServer().loadGame(game, idFirstPlayer);
        } catch (RemoteException e) {
            throw new ClientCommunicationException();
        }
    }
    /**
     * this method calls GetLoadedPlayers on the server and waits for its response (thread blocking)
     */
    @Override
    public LoadedGamePlayers getLoadedGamePlayers() {
        try {
            return this.rmiClientConnection.getServer().getLoadedGamePlayers();
        } catch (RemoteException e) {
            throw new ClientCommunicationException();
        }
    }
    /**
     * this method calls JoinLoadedAsFirst on the server and waits for its response (thread blocking)
     */
    @Override
    public BooleanResponse joinLoadedAsFirst(String player, String idFirstPlayer) {
        try {
            return this.rmiClientConnection.getServer().joinLoadedAsFirst(player, idFirstPlayer);
        } catch (RemoteException e) {
            throw new ClientCommunicationException();
        }
    }
    /**
     * this method calls Join on the server and waits for its response (thread blocking)
     */
    @Override
    public JoinResponse join(String player) {
        try {
            return this.rmiClientConnection.getServer().join(player);
        } catch (RemoteException e) {
            throw new ClientCommunicationException();
        }
    }
    /**
     * this method calls Disconnect on the server and waits for its response (thread blocking)
     */
    @Override
    public BooleanResponse disconnect(String playerId) {
        try {
            return this.rmiClientConnection.getServer().disconnect(playerId);
        } catch (RemoteException e) {
            throw new ClientCommunicationException();
        }
    }
    /**
     * this method calls Reconnect on the server and waits for its response (thread blocking)
     */
    @Override
    public BooleanResponse reconnect(String playerId) {
        try {
            return this.rmiClientConnection.getServer().reconnect(playerId);
        } catch (RemoteException e) {
            throw new ClientCommunicationException();
        }
    }
    /**
     * this method calls PickTilesCommand on the server and waits for its response (thread blocking)
     */
    @Override
    public BooleanResponse pickTiles(String playerId, Set<Tile> tiles) {
        try {
            return this.rmiClientConnection.getServer().pickTiles(playerId, tiles);
        } catch (RemoteException e) {
            throw new ClientCommunicationException();
        }
    }
    /**
     * this method calls PutTilesCommand on the server and waits for its response (thread blocking)
     */
    @Override
    public BooleanResponse putTiles(String playerId, List<TileType> tiles, int column) {
        try {
            return this.rmiClientConnection.getServer().putTiles(playerId, tiles, column);
        } catch (RemoteException e) {
            throw new ClientCommunicationException();
        }
    }
    /**
     * this method calls SendMessage on the server and waits for its response (thread blocking)
     */
    @Override
    public BooleanResponse sendMessage(String playerId, String player, String message) {
        try {
            return this.rmiClientConnection.getServer().sendMessage(playerId, player, message);
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
            this.rmiClientConnection.getServer().pong(playerId);
        } catch (RemoteException e) {
            throw new ClientCommunicationException();
        }
    }
}
