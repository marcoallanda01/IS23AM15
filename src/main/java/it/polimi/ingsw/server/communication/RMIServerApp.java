package it.polimi.ingsw.server.communication;

import it.polimi.ingsw.communication.commands.*;
import it.polimi.ingsw.communication.responses.*;
import it.polimi.ingsw.communication.rmi.RMIClient;
import it.polimi.ingsw.communication.rmi.RMIServer;
import it.polimi.ingsw.server.controller.*;
import it.polimi.ingsw.server.model.Tile;
import it.polimi.ingsw.server.model.TileType;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

public class RMIServerApp extends UnicastRemoteObject implements ServerCommunication, RMIServer {
    private final Map<RMIClient, String> playersIds;
    private final RMIRespondServer respondServer;
    private final int port;

    public RMIServerApp(int port, Lobby lobby, String sharedLock) throws RemoteException {
        super();
        this.port = port;
        this.playersIds = Collections.synchronizedMap(new HashMap<>());
        respondServer = new RMIRespondServer(lobby, sharedLock, playersIds);
    }

    /**
     * Start RMI server on this.port
     *
     * @throws RemoteException if something went wrong with the starting of the server
     */
    public void start() throws RemoteException, AlreadyBoundException {
        Registry registry = LocateRegistry.createRegistry(this.port);
        registry.bind("ServerRMIApp", this);
        System.out.println("Server RMI ready");
    }

    /**
     * HelloCommand to server
     * @param client RMIClient that says hello
     * @return Hello response
     * @throws RemoteException if something about connection went bad
     */
    @Override
    public Hello hello(RMIClient client) throws RemoteException {
        return respondServer.respondHello(new HelloCommand(), client);
    }

    /**
     * join as first player in a new game
     * @param client RMI client that joins
     * @param player player's name
     * @param numPlayersGame num of players of new game
     * @param idFirstPlayer first player's id
     * @return FirstJoinResponse
     * @throws RemoteException if something about connection went bad
     */
    @Override
    public FirstJoinResponse joinNewAsFirst(RMIClient client, String player, int numPlayersGame, String idFirstPlayer) throws RemoteException {
        return joinNewAsFirst(client, player, numPlayersGame, idFirstPlayer, true);
    }

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
    @Override
    public FirstJoinResponse joinNewAsFirst(RMIClient client, String player, int numPlayersGame, String idFirstPlayer, boolean easyRules) throws RemoteException {
        return respondServer.respondJoinNewAsFirst(new JoinNewAsFirst(player, numPlayersGame, idFirstPlayer, easyRules), client);
    }

    /**
     * join in a created game
     *
     * @param client RMI client that joins
     * @param player player's name
     * @return JoinResponse
     * @throws RemoteException if something about connection went bad
     */
    @Override
    public JoinResponse join(RMIClient client, String player) throws RemoteException {
        JoinResponse jr;
        try {
            jr = respondServer.respondJoin(new Join(player), client);
        } catch (FirstPlayerAbsentException e) {
            System.out.println(client+" Tried to join without a first player preset!");
            throw new RemoteException("Tried to join without a first player preset!");
        }
        return jr;
    }

    /**
     * Get the game that are saved on the server
     *
     * @return SavedGame response
     * @throws RemoteException if something about connection went bad
     */
    @Override
    public SavedGames getSavedGames() throws RemoteException {
        return respondServer.respondGetSavedGames(new GetSavedGames());
    }

    /**
     * load a game from a save
     *
     * @param game          game name
     * @param idFirstPlayer first player's id
     * @return LoadGameResponse
     * @throws RemoteException if something about connection went bad
     */
    @Override
    public LoadGameResponse loadGame(String game, String idFirstPlayer) throws RemoteException {
        return respondServer.respondLoadGame(new LoadGame(idFirstPlayer, game));
    }

    /**
     * Get player's nicknames from the game loaded
     *
     * @return LoadedGamePlayers response
     * @throws RemoteException if something about connection went bad
     */
    @Override
    public LoadedGamePlayers getLoadedGamePlayers() throws RemoteException {
        return respondServer.respondGetLoadedPlayers(new GetLoadedPlayers());
    }

    /**
     * join as first player in a loaded game
     *
     * @param client        RMI client that joins
     * @param player        player's name
     * @param idFirstPlayer first player's id
     * @return FirstJoinResponse
     * @throws RemoteException if something about connection went bad
     */
    @Override
    public FirstJoinResponse joinLoadedAsFirst(RMIClient client, String player, String idFirstPlayer) throws RemoteException {
        return respondServer.respondJoinLoadedAsFirst(new JoinLoadedAsFirst(player,idFirstPlayer), client);
    }

    /**
     * Disconnect from the game
     *
     * @param playerId player's id
     * @throws RemoteException if something about connection went bad
     */
    @Override
    public void disconnect(String playerId) throws RemoteException {
        if(this.playersIds.containsValue(playerId)){
            this.playersIds.forEach((k, v) -> {
                if(v.equals(playerId)){
                    respondServer.respondDisconnect(new Disconnect(playerId), k);
                }
            });
        }
    }

    /**
     * Reconnect to the game
     *
     * @param client client that reconnect
     * @param playerId player's id
     * @throws RemoteException if something about connection went bad
     */
    @Override
    public void reconnect(RMIClient client, String playerId) throws RemoteException {
        if(this.playersIds.containsValue(playerId)){
            respondServer.respondReconnect(new Reconnect(playerId), client);
        }
    }

    /**
     * Pick tiles command
     *
     * @param playerId player's id
     * @param tiles    tiles to pick
     * @throws RemoteException if something about connection went bad
     */
    @Override
    public void pickTiles(String playerId, Set<Tile> tiles) throws RemoteException {
        respondServer.respondPickTiles(new PickTilesCommand(playerId, tiles));
    }

    /**
     * Put tiles command
     *
     * @param playerId player's id
     * @param tiles    tiles to put in order
     * @param column   where to put the tiles
     * @throws RemoteException if something about connection went bad
     */
    @Override
    public void putTiles(String playerId, List<TileType> tiles, int column) throws RemoteException {
        respondServer.respondPutTiles(new PutTilesCommand(playerId,tiles, column));
    }

    /**
     * Save current game command
     *
     * @param playerId player's id
     * @param gameName name of the new save
     * @throws RemoteException if something about connection went bad
     */
    @Override
    public void saveGame(String playerId, String gameName) throws RemoteException {
        respondServer.respondSaveGame(new SaveGame(playerId, gameName));
    }

    /**
     * Send message to a player
     *
     * @param playerId         player's id
     * @param message          actual message
     * @param receiverNickname receiver's name
     * @throws RemoteException if something about connection went bad
     */
    @Override
    public void sendMessage(String playerId, String message, String receiverNickname) throws RemoteException {
        respondServer.respondSendMessage(new SendMessage(playerId, message, receiverNickname));
    }

    /**
     * Send message to all players
     *
     * @param playerId player's id
     * @param message  actual message
     * @throws RemoteException if something about connection went bad
     */
    @Override
    public void sendMessage(String playerId, String message) throws RemoteException {
        respondServer.respondSendMessage(new SendMessage(playerId, message));
    }

    /**
     * Respond to a ping
     *
     * @param playerId player's id
     * @throws RemoteException if something about connection went bad
     */
    @Override
    public void pong(String playerId) throws RemoteException {
        if(this.playersIds.containsValue(playerId)){
            this.playersIds.forEach((k, v) -> {
                if(v.equals(playerId)){
                    respondServer.respondPong(new Pong(playerId), k);
                }
            });
        }
    }

    //Server Communication methods
    /**
     * Send one GameSetUp object to every player
     */
    @Override
    public void gameSetUp() {
        respondServer.tryStartGame();
        respondServer.gameSetUp();
    }

    /**
     * If in game, function notifies the disconnection of a player to all the others
     *
     * @param playerName player that disconnect
     */
    @Override
    public void notifyDisconnection(String playerName) {
        this.playersIds.forEach((key, value) -> {
            try {
                key.notifyDisconnection(playerName);
            } catch (RemoteException e) {
                System.err.println("RMI notifyDisconnection: Remote Exception thrown with client "+value);
            }
        });
    }

    /**
     * Notify to all clients that a player reconnected
     *
     * @param playerName name of the player who reconnected
     */
    @Override
    public void notifyReconnection(String playerName) {
        this.playersIds.forEach((key, value) -> {
            try {
                key.notifyReconnection(playerName);
            } catch (RemoteException e) {
                System.err.println("RMI notifyReconnection: Remote Exception thrown with client "+value);
            }
        });
    }

    /**
     * Send a message to all players
     *
     * @param sender  sender's name
     * @param date    date of message creation
     * @param message actual message to be sent
     */
    @Override
    public void notifyMessage(String sender, String date, String message) {
        this.playersIds.forEach((key, value) -> {
            try {
                key.notifyChatMessage(sender, date, message);
            } catch (RemoteException e) {
                System.err.println("RMI notifyMessage: Remote Exception thrown with client "+value);
            }
        });
    }

    /**
     * Send a message to all players
     *
     * @param sender   sender's name
     * @param date     date of message creation
     * @param message  actual message to be sent
     * @param receiver receiver's name
     */
    @Override
    public void notifyMessage(String sender, String date, String message, String receiver) {
        this.playersIds.entrySet().stream()
                .filter(e->e.getValue().equals(receiver))
                .map(Map.Entry::getKey)
                .findFirst().ifPresent(c-> {
                    try {
                        c.notifyChatMessage(sender, message,date);
                    } catch (RemoteException e) {
                        System.err.println("RMI notifyMessage: Remote Exception thrown with client "+c);
                    }
                });
    }

    /**
     * Notify change in the board to all clients in game
     *
     * @param tiles board
     */
    @Override
    public void notifyChangeBoard(List<Tile> tiles) {
        this.playersIds.forEach((key, value) -> {
            try {
                key.notifyBoard(new HashSet<>(tiles));
            } catch (RemoteException e) {
                System.err.println("RMI notifyChangeBoard: Remote Exception thrown with client "+value);
            }
        });
    }

    /**
     * Notify to all clients change in player's bookshelf
     *
     * @param playerName player's name
     * @param tiles      bookshelf
     */
    @Override
    public void notifyChangeBookShelf(String playerName, List<Tile> tiles) {
        this.playersIds.forEach((key, value) -> {
            try {
                key.notifyBookshelf(playerName,new HashSet<>(tiles));
            } catch (RemoteException e) {
                System.err.println("RMI notifyChangeBookShelf: Remote Exception thrown with client "+value);
            }
        });
    }

    /**
     * Notify change in point of a player to all clients
     *
     * @param playerName player's name
     * @param points     new points
     */
    @Override
    public void updatePlayerPoints(String playerName, int points) {
        this.playersIds.forEach((key, value) -> {
            try {
                key.notifyPoints(playerName, points);
            } catch (RemoteException e) {
                System.err.println("RMI updatePlayerPoints: Remote Exception thrown with client "+value);
            }
        });
    }

    /**
     * Notify to all player whom turn is
     *
     * @param playerName current player
     */
    @Override
    public void notifyTurn(String playerName) {
        this.playersIds.forEach((key, value) -> {
            try {
                key.notifyTurn(playerName);
            } catch (RemoteException e) {
                System.err.println("RMI notifyTurn: Remote Exception thrown with client "+value);
            }
        });
    }

    /**
     * Notify to all clients a change in common goals cards and tokens
     *
     * @param cardsAndTokens cards with associated tokens
     */
    @Override
    public void sendCommonGoalsCards(Map<String, List<Integer>> cardsAndTokens) {
        this.playersIds.forEach((key, value) -> {
            try {
                key.notifyCommonGoalCards(cardsAndTokens);
            } catch (RemoteException e) {
                System.err.println("RMI sendCommonGoalsCards: Remote Exception thrown with client "+value);
            }
        });
    }

    /**
     * Notify the winner to all playing clients, close all playing clients, reset lobby
     *
     * @param playerName name of the winner
     */
    @Override
    public void notifyWinner(String playerName) {
        this.playersIds.forEach((key, value) -> {
            try {
                key.notifyWinner(playerName);
            } catch (RemoteException e) {
                System.err.println("RMI notifyWinner: Remote Exception thrown with client "+value);
            }
        });
        // Close all playing clients
        this.playersIds.forEach((key, value) -> {
            respondServer.closeClient(key);
        });
        //reset
        respondServer.reset();
    }

    /**
     * Handle the disconnection of the last player terminating the game
     */
    @Override
    public void handleLastPlayerDisconnection() {
        respondServer.reset();
    }
}