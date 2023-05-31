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
 * RMI based implementation of ClientCommunication
 * it uses a RMIClientConnection to call methods on the server
 * note that some methods expect a return value,
 * the ClientCommunication class is responsible for dedicating
 * threads that wait for that return value and call the appropriate functions
 * on ClientCommunication (in  place of the server)
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
     * Hello to establish a connection.
     */
    @Override
    public void hello() {
        executorService.submit(() -> {
            Hello hello = null;
            try {
                hello = rmiClientConnection.getServer().hello(rmiClientConnection);
                Client.getInstance().getLogger().log("Client called hello(" + rmiClientConnection + ")");
                rmiClientConnection.notifyHello(hello.lobbyReady, hello.firstPlayerId, hello.loadedGame);
                Client.getInstance().getLogger().log("Server returned: " + hello.toJson());
            } catch (RemoteException e) {
                Client.getInstance().getLogger().log(e);
            }
        });
    }
    /**
     * Requests to join a new game as the first player.
     *
     * @param player           The name of the player joining the game.
     * @param numPlayersGame   The number of players in the game.
     * @param idFirstPlayer    The ID of the first player.
     */
    @Override
    public void joinNewAsFirst(String player, int numPlayersGame, String idFirstPlayer) {
        executorService.submit(() -> {
            FirstJoinResponse firstJoinResponse = null;
            try {
                firstJoinResponse = rmiClientConnection.getServer().joinNewAsFirst(rmiClientConnection, player, numPlayersGame, idFirstPlayer);
                Client.getInstance().getLogger().log("Client called joinNewAsFirst(" + player + ", " + numPlayersGame + ", " + idFirstPlayer + ")");
                rmiClientConnection.notifyFirstJoinResponse(firstJoinResponse.result);
                Client.getInstance().getLogger().log("Server responded: " + firstJoinResponse.toJson());
            } catch (RemoteException e) {
                Client.getInstance().getLogger().log(e);
            }
        });
    }

    /**
     * Requests to join a new game as the first player with specified rules.
     *
     * @param player           The name of the player joining the game.
     * @param numPlayersGame   The number of players in the game.
     * @param idFirstPlayer    The ID of the first player.
     * @param easyRules        The flag indicating if easy rules should be applied.
     */
    @Override
    public void joinNewAsFirst(String player, int numPlayersGame, String idFirstPlayer, boolean easyRules) {
        executorService.submit(() -> {
            FirstJoinResponse firstJoinResponse = null;
            try {
                firstJoinResponse = rmiClientConnection.getServer().joinNewAsFirst(rmiClientConnection, player, numPlayersGame, idFirstPlayer, easyRules);
                Client.getInstance().getLogger().log("Client called joinNewAsFirst(" + player + ", " + numPlayersGame + ", " + idFirstPlayer + ", " + easyRules + ")");
                rmiClientConnection.notifyFirstJoinResponse(firstJoinResponse.result);
                Client.getInstance().getLogger().log("Server responded: " + firstJoinResponse.toJson());
            } catch (RemoteException e) {
                Client.getInstance().getLogger().log(e);
            }
        });
    }

    /**
     * Requests to get the list of saved games from the server.
     */
    @Override
    public void getSavedGames() {
        executorService.submit(() -> {
            SavedGames savedGames = null;
            try {
                savedGames = rmiClientConnection.getServer().getSavedGames();
                Client.getInstance().getLogger().log("Client called getSavedGames()");
                rmiClientConnection.notifySavedGames(savedGames.names);
                Client.getInstance().getLogger().log("Server responded: " + savedGames.toJson());
            } catch (RemoteException e) {
                Client.getInstance().getLogger().log(e);
            }
        });
    }

    /**
     * Requests to load a game from the server.
     *
     * @param game             The name of the game to be loaded.
     * @param idFirstPlayer    The ID of the first player.
     */
    @Override
    public void loadGame(String game, String idFirstPlayer) {
        executorService.submit(() -> {
            LoadGameResponse loadGameResponse = null;
            try {
                loadGameResponse = rmiClientConnection.getServer().loadGame(game, idFirstPlayer);
                Client.getInstance().getLogger().log("Client called loadGame(" + game + ", " + idFirstPlayer + ")");
                rmiClientConnection.notifyLoadGameResponse(loadGameResponse.result, loadGameResponse.error);
                Client.getInstance().getLogger().log("Server responded: " + loadGameResponse.toJson());
            } catch (RemoteException e) {
                Client.getInstance().getLogger().log(e);
            }
        });
    }
    /**
     * Requests to get the list of players in a loaded game from the server.
     */
    @Override
    public void getLoadedGamePlayers() {
        executorService.submit(() -> {
            LoadedGamePlayers loadedGamePlayers = null;
            try {
                loadedGamePlayers = rmiClientConnection.getServer().getLoadedGamePlayers();
                Client.getInstance().getLogger().log("Client called getLoadedGamePlayers()");
                rmiClientConnection.notifyLoadedGamePlayers(loadedGamePlayers.names);
                Client.getInstance().getLogger().log("Server responded: " + loadedGamePlayers.toJson());
            } catch (RemoteException e) {
                Client.getInstance().getLogger().log(e);
            }
        });
    }
    /**
     * Requests to join a loaded game as the first player.
     *
     * @param player           The name of the player joining the game.
     * @param idFirstPlayer    The ID of the first player.
     */
    @Override
    public void joinLoadedAsFirst(String player, String idFirstPlayer) {
        executorService.submit(() -> {
            FirstJoinResponse firstJoinResponse = null;
            try {
                firstJoinResponse = rmiClientConnection.getServer().joinLoadedAsFirst(rmiClientConnection, player, idFirstPlayer);
                Client.getInstance().getLogger().log("Client called joinLoadedAsFirst(" + player + ", " + idFirstPlayer + ")");
                rmiClientConnection.notifyFirstJoinResponse(firstJoinResponse.result);
                Client.getInstance().getLogger().log("Server responded: " + firstJoinResponse.toJson());
            } catch (RemoteException e) {
                Client.getInstance().getLogger().log(e);
            }
        });
    }

    /**
     * Requests to join an existing game without creating it.
     *
     * @param player the name of the player joining the game
     */
    @Override
    public void join(String player) {
        executorService.submit(() -> {
            JoinResponse joinResponse = null;
            try {
                joinResponse = rmiClientConnection.getServer().join(rmiClientConnection, player);
                Client.getInstance().getLogger().log("Client called join(" + player + ")");
                rmiClientConnection.notifyJoinResponse(joinResponse.result, joinResponse.error, joinResponse.id);
                Client.getInstance().getLogger().log("Server responded: " + joinResponse.toJson());
            } catch (RemoteException e) {
                Client.getInstance().getLogger().log(e);
            }
        });
    }

    /**
     * Requests to disconnect a player from the server.
     *
     * @param playerId The ID of the player to disconnect.
     */
    @Override
    public void disconnect(String playerId) {
        try {
            rmiClientConnection.getServer().disconnect(playerId);
            rmiClientConnection.closeConnection();
            Client.getInstance().getLogger().log("Client called disconnect(" + playerId + ")");
        } catch (RemoteException e) {
            Client.getInstance().getLogger().log(e);
        } catch (Exception e) {
            // no details about the server side error (no information disclosed)
            Client.getInstance().getLogger().log("Server encountered an error while logging you out");
        }
    }

    /**
     * Requests to reconnect a player to the server.
     *
     * @param playerId The ID of the player to reconnect.
     */
    @Override
    public void reconnect(String playerId) {
        executorService.submit(() -> {
            try {
                rmiClientConnection.getServer().reconnect(rmiClientConnection, playerId);
                Client.getInstance().getLogger().log("Client called reconnect(" + playerId + ")");
            } catch (RemoteException e) {
                Client.getInstance().getLogger().log(e);
            }
        });
    }

    /**
     * Requests to pick tiles for a player.
     *
     * @param playerId The ID of the player picking tiles.
     * @param tiles    The set of tiles to be picked.
     */
    @Override
    public void pickTiles(String playerId, Set<Tile> tiles) {
        executorService.submit(() -> {
            try {
                rmiClientConnection.getServer().pickTiles(playerId, tiles);
                Client.getInstance().getLogger().log("Client called pickTiles(" + playerId + ", " + tiles + ")");
            } catch (RemoteException e) {
                Client.getInstance().getLogger().log(e);
            }
        });
    }

    /**
     * Requests to put tiles in the bookshelf.
     *
     * @param playerId The ID of the player putting tiles.
     * @param tiles    The list of tiles to be placed.
     * @param column   The bookshelf column where the tiles are to be put.
     */
    @Override
    public void putTiles(String playerId, List<TileType> tiles, int column) {
        executorService.submit(() -> {
            try {
                rmiClientConnection.getServer().putTiles(playerId, tiles, column);
                Client.getInstance().getLogger().log("Client called putTiles(" + playerId + ", " + tiles + ", " + column + ")");
            } catch (RemoteException e) {
                Client.getInstance().getLogger().log(e);
            }
        });
    }

    /**
     * Requests to send a message from one player to another player.
     *
     * @param playerId         The ID of the player sending the message.
     * @param message          The content of the message.
     * @param receiverNickname The nickname of the message receiver.
     */
    @Override
    public void sendMessage(String playerId, String message, String receiverNickname) {
        executorService.submit(() -> {
            try {
                rmiClientConnection.getServer().sendMessage(playerId, message, receiverNickname);
                Client.getInstance().getLogger().log("Client called sendMessage(" + playerId + ", " + message + ", " + receiverNickname + ")");
            } catch (RemoteException e) {
                Client.getInstance().getLogger().log(e);
            }
        });
    }

    /**
     * Requests to send a message from one player to all players.
     *
     * @param playerId The ID of the player sending the message.
     * @param message  The content of the message.
     */
    @Override
    public void sendMessage(String playerId, String message) {
        executorService.submit(() -> {
            try {
                rmiClientConnection.getServer().sendMessage(playerId, message);
                Client.getInstance().getLogger().log("Client called sendMessage(" + playerId + ", " + message + ")");
            } catch (RemoteException e) {
                Client.getInstance().getLogger().log(e);
            }
        });
    }

    /**
     * Requests a ping, notifies that the player is active.
     *
     * @param playerId The ID of the player sending the pong.
     */
    @Override
    public void pong(String playerId) {
        executorService.submit(() -> {
            try {
                rmiClientConnection.getServer().pong(playerId);
            } catch (RemoteException e) {
                Client.getInstance().getLogger().log(e);
            }
        });
    }

    /**
     * Requests to save the game state.
     *
     * @param playerId  The ID of the player saving the game.
     * @param gameName  The name of the save.
     */
    @Override
    public void saveGame(String playerId, String gameName) {
        executorService.submit(() -> {
            try {
                rmiClientConnection.getServer().saveGame(playerId, gameName);
                Client.getInstance().getLogger().log("Client called saveGame(" + playerId + ", " + gameName + ")");
            } catch (RemoteException e) {
                Client.getInstance().getLogger().log(e);
            }
        });
    }
}
