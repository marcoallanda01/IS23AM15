package it.polimi.ingsw.client.communication;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.communication.rmi.RMIClient;
import it.polimi.ingsw.communication.rmi.RMIServer;
import it.polimi.ingsw.communication.responses.GameSetUp;
import it.polimi.ingsw.server.model.Tile;
import it.polimi.ingsw.server.model.TileType;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Creates the RMI client connection endpoint (implementing ClientConnection).
 * This class is also an UnicastRemoteObject accessible from the server through the interface RMIClient.
 * Therefore, it is responsible for receiving notifications from the server.
 * It also offers the possibility to retrieve a reference to the RMIServer Interface representing
 * the UnicastRemoteObject registered from the server.
 * There are also some methods that are used from RMIClientCommunication to notify the returned
 * values of some specific methods called on the server.
 * RMIClientConnection is responsible for synchronizing such methods since ClientNotificationListener
 * does not guarantee synchronization
 */
public class RMIClientConnection extends UnicastRemoteObject implements RMIClient, ClientConnection {
    private ClientNotificationListener clientNotificationListener;
    private RMIServer rmiServer;
    private final String hostname;
    private final int port;

    /**
     * Constructs an instance of RMIClientConnection.
     *
     * @param hostname                   the hostname of the RMI server
     * @param port                       the port of the RMI server
     * @param clientNotificationListener the client notification listener
     * @throws Exception if an exception occurs during the RMI setup
     */
    public RMIClientConnection(String hostname, int port, ClientNotificationListener clientNotificationListener) throws Exception {
        this.hostname = hostname;
        this.port = port;
        this.clientNotificationListener = clientNotificationListener;
    }

    /**
     * Opens the RMI client connection.
     *
     * @throws ClientConnectionException if an error occurs while opening the RMI client connection
     */
    public void openConnection() throws ClientConnectionException {
        Client.getInstance().getLogger().log("Opening RMI client connection...");
        Registry registry;
        Client.getInstance().getLogger().log("Locating registry...");
        try {
            registry = LocateRegistry.getRegistry(hostname, port);
        } catch (RemoteException e) {
            Client.getInstance().getLogger().log(e);
            throw new ClientConnectionException("Error while locating registry.");
        }
        Client.getInstance().getLogger().log("Looking up the registry for ServerRMIApp...");
        try {
            this.rmiServer = (RMIServer) registry.lookup("ServerRMIApp");
        } catch (RemoteException | NotBoundException e) {
            Client.getInstance().getLogger().log(e);
            throw new ClientConnectionException("Error while opening RMI client connection.");
        }
        Client.getInstance().getLogger().log("RMI client connection open.");
        Client.getInstance().onConnectionReady();
    }

    /**
     * Closes the RMI client connection.
     *
     * @throws ClientConnectionException if an error occurs while closing the RMI client connection
     */
    public void closeConnection() throws ClientConnectionException {
        try {
            UnicastRemoteObject.unexportObject(this, true);
            Client.getInstance().getLogger().log("RMI client connection closed.");
        } catch (RemoteException e) {
            throw new ClientConnectionException("Error while closing RMI client connection.");
        }
    }

    /**
     * Retrieves the RMIServer instance representing the remote server object.
     *
     * @return the RMIServer instance
     */
    public RMIServer getServer() {
        return this.rmiServer;
    }

    /**
     * Notifies the client about a game setup.
     *
     * @param gameSetUp the game setup information
     * @throws RemoteException if an RMI error occurs during the remote method invocation
     */
    @Override
    public void notifyGame(GameSetUp gameSetUp) throws RemoteException {
        Client.getInstance().getLogger().log("Server called notifyGame(" + gameSetUp.toJson() +")");
        clientNotificationListener.notifyGame(gameSetUp);
    }

    /**
     * Notifies the client about the winner of the game.
     *
     * @param nickname the nickname of the winner
     * @throws RemoteException if an RMI error occurs during the remote method invocation
     */
    @Override
    public synchronized void notifyWinner(String nickname) throws RemoteException {
        Client.getInstance().getLogger().log("Server called notifyWinner(" + nickname + ")");
        clientNotificationListener.notifyWinner(nickname);
    }

    /**
     * Notifies the client about the game board.
     *
     * @param tiles the set of tiles representing the game board
     * @throws RemoteException if an RMI error occurs during the remote method invocation
     */
    @Override
    public void notifyBoard(Set<Tile> tiles) throws RemoteException {
        Client.getInstance().getLogger().log("Server called notifyBoard(" + tiles + ")");
        clientNotificationListener.notifyBoard(tiles);
    }

    /**
     * Notifies the client about the bookshelf of a player.
     *
     * @param nickname the nickname of the player
     * @param tiles    the set of tiles representing the bookshelf
     * @throws RemoteException if an RMI error occurs during the remote method invocation
     */
    @Override
    public void notifyBookshelf(String nickname, Set<Tile> tiles) throws RemoteException {
        Client.getInstance().getLogger().log("Server called notifyBookshelf(" + nickname + "," + tiles + ")");
        clientNotificationListener.notifyBookshelf(nickname, tiles);
    }

    /**
     * Notifies the client about the points earned by a player.
     *
     * @param nickname the nickname of the player
     * @param points   the points earned
     * @throws RemoteException if an RMI error occurs during the remote method invocation
     */
    @Override
    public void notifyPoints(String nickname, int points) throws RemoteException {
        Client.getInstance().getLogger().log("Server called notifyPoints(" + nickname + "," + points + ")");
        clientNotificationListener.notifyPoints(nickname, points);
    }

    /**
     * Notifies the client about the current turn of a player.
     *
     * @param nickname the nickname of the player
     * @throws RemoteException if an RMI error occurs during the remote method invocation
     */
    @Override
    public void notifyTurn(String nickname) throws RemoteException {
        Client.getInstance().getLogger().log("Server called notifyTurn(" + nickname + ")");
        clientNotificationListener.notifyTurn(nickname);
    }

    /**
     * Notifies the client about the personal goal card of a player.
     *
     * @param nickname the nickname of the player
     * @param card     the personal goal card
     * @throws RemoteException if an RMI error occurs during the remote method invocation
     */
    @Override
    public void notifyPersonalGoalCard(String nickname, String card) throws RemoteException {
        Client.getInstance().getLogger().log("Server called notifyPersonalGoalCard(" + nickname + "," + card + ")");
        clientNotificationListener.notifyPersonalGoalCard(nickname, card);
    }

    /**
     * Notifies the client about the common cards and their associated tokens.
     *
     * @param cardsToTokens a map representing the common cards and their associated tokens
     * @throws RemoteException if an RMI error occurs during the remote method invocation
     */
    @Override
    public void notifyCommonCards(Map<String, List<Integer>> cardsToTokens) throws RemoteException {
        Client.getInstance().getLogger().log("Server called notifyCommonCards(" + cardsToTokens + ")");
        clientNotificationListener.notifyCommonCards(cardsToTokens);
    }

    /**
     * Notifies the client about the common goals.
     *
     * @param goals the set of common goals
     * @throws RemoteException if an RMI error occurs during the remote method invocation
     */
    @Override
    public void notifyCommonGoals(Set<String> goals) throws RemoteException {
        Client.getInstance().getLogger().log("Server called notifyCommonGoals(" + goals + ")");
        clientNotificationListener.notifyCommonGoals(goals);
    }

    /**
     * Notifies the client about a chat message.
     *
     * @param nickname the nickname of the sender
     * @param message  the chat message
     * @param date     the date of the chat message
     * @throws RemoteException if an RMI error occurs during the remote method invocation
     */
    @Override
    public void notifyChatMessage(String nickname, String message, String date) throws RemoteException {
        Client.getInstance().getLogger().log("Server called notifyChatMessage(" + nickname + "," + message + "," + date + ")");
        clientNotificationListener.notifyChatMessage(nickname, message, date);
    }

    /**
     * Notifies the client about a player's disconnection.
     *
     * @param nickname the nickname of the disconnected player
     * @throws RemoteException if an RMI error occurs during the remote method invocation
     */
    @Override
    public void notifyDisconnection(String nickname) throws RemoteException {
        Client.getInstance().getLogger().log("Server called notifyDisconnection(" + nickname + ")");
        clientNotificationListener.notifyDisconnection(nickname);
    }

    /**
     * Notifies the client that the game has been saved.
     *
     * @param game the saved game information
     * @throws RemoteException if an RMI error occurs during the remote method invocation
     */
    @Override
    public void notifyGameSaved(String game) throws RemoteException {
        Client.getInstance().getLogger().log("Server called notifyGameSaved(" + game + ")");
        clientNotificationListener.notifyGameSaved(game);
    }

    /**
     * Notifies the client about a ping from the server.
     *
     * @throws RemoteException if an RMI error occurs during the remote method invocation
     */
    @Override
    public void notifyPing() throws RemoteException {
        clientNotificationListener.notifyPing();
    }

    /**
     * Notifies the client about a player's reconnection.
     *
     * @param nickname the nickname of the reconnected player
     * @throws RemoteException if an RMI error occurs during the remote method invocation
     */
    @Override
    public void notifyReconnection(String nickname) throws RemoteException {
        Client.getInstance().getLogger().log("Server called notifyReconnection(" + nickname + ")");
        clientNotificationListener.notifyReconnection(nickname);
    }

    /**
     * Notifies the client about an error.
     *
     * @param message the error message
     * @throws RemoteException if an RMI error occurs during the remote method invocation
     */
    @Override
    public void notifyError(String message) throws RemoteException {
        Client.getInstance().getLogger().log("Server called notifyError(" + message + ")");
        clientNotificationListener.notifyError(message);
    }

    /**
     * Notifies the client about the tiles picked by a player.
     *
     * @param nickname the nickname of the player
     * @param tiles    the list of tiles picked
     * @throws RemoteException if an RMI error occurs during the remote method invocation
     */
    @Override
    public void notifyPickedTiles(String nickname, List<TileType> tiles) throws RemoteException {
        Client.getInstance().getLogger().log("Server called notifyPickedTiles(" + nickname + "," + tiles + ")");
        clientNotificationListener.notifyPickedTiles(nickname, tiles);
    }

    /**
     * Notifies the client about the lobby status and the first player's ID.
     *
     * @param lobbyReady    the lobby readiness status
     * @param firstPlayerId the ID of the first player
     * @param loadedGame    indicates if the game is loaded from a saved state
     * @throws RemoteException if an RMI error occurs during the remote method invocation
     */
    public synchronized void notifyHello(boolean lobbyReady, String firstPlayerId, boolean loadedGame) {
        Client.getInstance().getLogger().log("Server called notifyHello(" + lobbyReady + "," + firstPlayerId + "," + loadedGame + ")");
        clientNotificationListener.notifyHello(lobbyReady, firstPlayerId, loadedGame);
    }

    /**
     * Notifies the client about the saved games available.
     *
     * @param games the set of saved game names
     * @throws RemoteException if an RMI error occurs during the remote method invocation
     */
    public synchronized void notifySavedGames(Set<String> games) {
        Client.getInstance().getLogger().log("Server called notifySavedGames(" + games + ")");
        clientNotificationListener.notifySavedGames(games);
    }

    /**
     * Notifies the client about the response to a join request.
     *
     * @param result the result of the join request (true if successful, false otherwise)
     * @param error  the error message if the join request was unsuccessful
     * @param id     the player's ID if the join request was successful
     * @throws RemoteException if an RMI error occurs during the remote method invocation
     */
    public synchronized void notifyJoinResponse(boolean result, String error, String id) {
        Client.getInstance().getLogger().log("Server called notifyJoinResponse(" + result + "," + error + "," + id + ")");
        clientNotificationListener.notifyJoinResponse(result, error, id);
    }

    /**
     * Notifies the client about the response to the first join request.
     *
     * @param result the result of the first join request (true if successful, false otherwise)
     * @throws RemoteException if an RMI error occurs during the remote method invocation
     */
    public synchronized void notifyFirstJoinResponse(boolean result) {
        Client.getInstance().getLogger().log("Server called notifyFirstJoinResponse(" + result + ")");
        clientNotificationListener.notifyFirstJoinResponse(result);
    }

    /**
     * Notifies the client about the response to a game loading request.
     *
     * @param result the result of the game loading request (true if successful, false otherwise)
     * @param error  the error message if the game loading request was unsuccessful
     * @throws RemoteException if an RMI error occurs during the remote method invocation
     */
    public synchronized void notifyLoadGameResponse(boolean result, String error) {
        Client.getInstance().getLogger().log("Server called notifyLoadGameResponse(" + result + "," + error + ")");
        clientNotificationListener.notifyLoadGameResponse(result, error);
    }

    /**
     * Notifies the client about the players in a loaded game.
     *
     * @param nicknames the set of player nicknames in the loaded game
     * @throws RemoteException if an RMI error occurs during the remote method invocation
     */
    public synchronized void notifyLoadedGamePlayers(Set<String> nicknames) {
        Client.getInstance().getLogger().log("Server called notifyLoadedGamePlayers(" + nicknames + ")");
        clientNotificationListener.notifyLoadedGamePlayers(nicknames);
    }
}
