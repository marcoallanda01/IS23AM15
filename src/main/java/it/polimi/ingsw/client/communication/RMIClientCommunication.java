package it.polimi.ingsw.client.communication;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.communication.responses.*;
import it.polimi.ingsw.server.model.Tile;
import it.polimi.ingsw.server.model.TileType;

import java.rmi.RemoteException;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * this class is an RMI based implementation of ClientCommunication
 * it uses a RMIClientConnection to call methods on the server
 * note that some methods expect a return value blocking the thread
 */
public class RMIClientCommunication implements ClientCommunication {
    private ExecutorService executorService;
    private RMIClientConnection rmiClientConnection;
    /**
     * @param rmiClientConnection the implementation of the connection
     */
    public RMIClientCommunication(RMIClientConnection rmiClientConnection) {
        executorService = Executors.newCachedThreadPool();
        this.rmiClientConnection = rmiClientConnection;
    }
    /**
     * this method calls Hello on the server
     */
    @Override
    public void hello() {
        executorService.submit(() -> {
            Hello hello = null;
            try {
                hello = rmiClientConnection.getServer().hello(rmiClientConnection);
                rmiClientConnection.notifyHello(hello.lobbyReady, hello.firstPlayerId, hello.loadedGame);
            } catch (RemoteException e) {
                Client.getInstance().getLogger().log(e);
                throw new ClientCommunicationException();
            }
        });
    }
    /**
     * this method calls JoinNewAsFirst on the server
     */
    @Override
    public void joinNewAsFirst(String player, int numPlayersGame, String idFirstPlayer) {
        executorService.submit(() -> {
            FirstJoinResponse firstJoinResponse = null;
            try {
                firstJoinResponse = rmiClientConnection.getServer().joinNewAsFirst(rmiClientConnection, player, numPlayersGame, idFirstPlayer);
                rmiClientConnection.notifyFirstJoinResponse(firstJoinResponse.result);
            } catch (RemoteException e) {
                Client.getInstance().getLogger().log(e);
                throw new ClientCommunicationException();
            }
        });
    }
    /**
     * this method calls JoinNewAsFirst on the server
     */
    @Override
    public void joinNewAsFirst(String player, int numPlayersGame, String idFirstPlayer, boolean easyRules) {
        executorService.submit(() -> {
            FirstJoinResponse firstJoinResponse = null;
            try {
                firstJoinResponse = rmiClientConnection.getServer().joinNewAsFirst(rmiClientConnection, player, numPlayersGame, idFirstPlayer, easyRules);
                rmiClientConnection.notifyFirstJoinResponse(firstJoinResponse.result);
            } catch (RemoteException e) {
                Client.getInstance().getLogger().log(e);
                throw new ClientCommunicationException();
            }
        });
    }
    /**
     * this method calls GetSavedGames on the server
     */
    @Override
    public void getSavedGames() {
        executorService.submit(() -> {
            SavedGames savedGames = null;
            try {
                savedGames = rmiClientConnection.getServer().getSavedGames();
                rmiClientConnection.notifySavedGames(savedGames.names);
            } catch (RemoteException e) {
                Client.getInstance().getLogger().log(e);
                throw new ClientCommunicationException();
            }
        });
    }
    /**
     * this method calls LoadGame on the server
     */
    @Override
    public void loadGame(String game, String idFirstPlayer) {
        executorService.submit(() -> {
            LoadGameResponse loadGameResponse = null;
            try {
                loadGameResponse = rmiClientConnection.getServer().loadGame(game, idFirstPlayer);
                rmiClientConnection.notifyLoadGameResponse(loadGameResponse.result, loadGameResponse.error);
            } catch (RemoteException e) {
                Client.getInstance().getLogger().log(e);
                throw new ClientCommunicationException();
            }
        });
    }
    /**
     * this method calls GetLoadedPlayers on the server
     */
    @Override
    public void getLoadedGamePlayers() {
        executorService.submit(() -> {
            LoadedGamePlayers loadedGamePlayers = null;
            try {
                loadedGamePlayers = rmiClientConnection.getServer().getLoadedGamePlayers();
                rmiClientConnection.notifyLoadedGamePlayers(loadedGamePlayers.names);
            } catch (RemoteException e) {
                Client.getInstance().getLogger().log(e);
                throw new ClientCommunicationException();
            }
        });
    }
    /**
     * this method calls JoinLoadedAsFirst on the server
     */
    @Override
    public void joinLoadedAsFirst(String player, String idFirstPlayer) {
        executorService.submit(() -> {
            FirstJoinResponse firstJoinResponse = null;
            try {
                firstJoinResponse = rmiClientConnection.getServer().joinLoadedAsFirst(rmiClientConnection, player, idFirstPlayer);
                rmiClientConnection.notifyFirstJoinResponse(firstJoinResponse.result);
            } catch (RemoteException e) {
                Client.getInstance().getLogger().log(e);
                throw new ClientCommunicationException();
            }
        });
    }
    /**
     * this method calls Join on the server
     */
    @Override
    public void join(String player) {
        executorService.submit(() -> {
            JoinResponse joinResponse = null;
            try {
                joinResponse = rmiClientConnection.getServer().join(rmiClientConnection, player);
                rmiClientConnection.notifyJoinResponse(joinResponse.result, joinResponse.error, joinResponse.id);
            } catch (RemoteException e) {
                Client.getInstance().getLogger().log(e);
                throw new ClientCommunicationException();
            }
        });
    }
    /**
     * this method calls Disconnect on the server
     */
    @Override
    public void disconnect(String playerId) {
        try {
            rmiClientConnection.getServer().disconnect(playerId);
        } catch (RemoteException e) {
            Client.getInstance().getLogger().log(e);
            throw new ClientCommunicationException();
        }
    }
    /**
     * this method calls Reconnect on the server
     */
    @Override
    public void reconnect(String playerId) {
        try {
            rmiClientConnection.getServer().reconnect(rmiClientConnection, playerId);
        } catch (RemoteException e) {
            Client.getInstance().getLogger().log(e);
            throw new ClientCommunicationException();
        }
    }
    /**
     * this method calls PickTilesCommand on the server
     */
    @Override
    public void pickTiles(String playerId, Set<Tile> tiles) {
        try {
            rmiClientConnection.getServer().pickTiles(playerId, tiles);
        } catch (RemoteException e) {
            Client.getInstance().getLogger().log(e);
            throw new ClientCommunicationException();
        }
    }
    /**
     * this method calls PutTilesCommand on the server
     */
    @Override
    public void putTiles(String playerId, List<TileType> tiles, int column) {
        try {
            rmiClientConnection.getServer().putTiles(playerId, tiles, column);
        } catch (RemoteException e) {
            Client.getInstance().getLogger().log(e);
            throw new ClientCommunicationException();
        }
    }
    /**
     * this method calls SendMessage on the server (to one player)
     */
    @Override
    public void sendMessage(String playerId, String message, String receiverNickname) {
        try {
            rmiClientConnection.getServer().sendMessage(playerId, message, receiverNickname);
        } catch (RemoteException e) {
            Client.getInstance().getLogger().log(e);
            throw new ClientCommunicationException();
        }
    }
    /**
     * this method calls SendMessage on the server (to all)
     */
    @Override
    public void sendMessage(String playerId, String message) {
        try {
            rmiClientConnection.getServer().sendMessage(playerId, message);
        } catch (RemoteException e) {
            Client.getInstance().getLogger().log(e);
            throw new ClientCommunicationException();
        }
    }
    /**
     * this method calls pong on the server
     */
    @Override
    public void pong(String playerId) {
        try {
            rmiClientConnection.getServer().pong(playerId);
            System.out.println("Pong sent");
        } catch (RemoteException e) {
            Client.getInstance().getLogger().log(e);
            throw new ClientCommunicationException();
        }
    }

    @Override
    public void saveGame(String playerId, String gameName) {
        try {
            rmiClientConnection.getServer().saveGame(playerId, gameName);
        } catch (RemoteException e) {
            Client.getInstance().getLogger().log(e);
            throw new ClientCommunicationException();
        }
    }
}
