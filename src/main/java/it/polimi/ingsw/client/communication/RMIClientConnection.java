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
 * Class that creates the RMI client connection endpoint (implementing ClientConnection)
 * this class is also an UnicastRemoteObject accessible from the server through the interface RMIClient
 * therefore this class is responsible for receiving notifications from the server
 * it does also offer the possibility to retrieve a reference to the RMIServer Interface representing
 * the UnicastRemoteObject registered from the server
 */
public class RMIClientConnection extends UnicastRemoteObject implements RMIClient, ClientConnection {
    private ClientNotificationListener clientNotificationListener;
    private RMIServer rmiServer;
    private final String hostname;
    private final int port;

    public RMIClientConnection(String hostname, int port, ClientNotificationListener clientNotificationListener) throws Exception {
        this.hostname = hostname;
        this.port = port;
        this.clientNotificationListener = clientNotificationListener;
    }
    public void openConnection() throws ClientConnectionException {
        Client.getInstance().getLogger().log("Opening RMI client connection...");
        // Getting the registry
        Registry registry;
        Client.getInstance().getLogger().log("Locating registry...");
        try {
            registry = LocateRegistry.getRegistry(hostname, port);
        } catch (RemoteException e) {
            e.printStackTrace();
            throw new ClientConnectionException();
        }
        Client.getInstance().getLogger().log("Looking up the registry for ServerRMIApp...");
        // Looking up the registry for the remote object
        try {
            this.rmiServer = (RMIServer) registry.lookup("ServerRMIApp");
        } catch (RemoteException e) {
            e.printStackTrace();
            throw new ClientConnectionException();
        } catch (NotBoundException e) {
            e.printStackTrace();
            throw new ClientConnectionException();
        }
        Client.getInstance().getLogger().log("RMI client connection open");
    }
    // Method to close RMI connection
    public void closeConnection() {
        try {
            // Unexport the remote object
            UnicastRemoteObject.unexportObject(this, true);
            Client.getInstance().getLogger().log("RMI client connection closed.");
        } catch (RemoteException e) {
            throw new ClientConnectionException();
        }
    }
    public RMIServer getServer() {
        return this.rmiServer;
    }
    @Override
    public void notifyGame(GameSetUp gameSetUp) throws RemoteException {
        clientNotificationListener.notifyGame(gameSetUp);
    }
    @Override
    public void notifyWinner(String nickname) throws RemoteException {
        clientNotificationListener.notifyWinner(nickname);
    }
    @Override
    public void notifyBoard(Set<Tile> tiles) throws RemoteException {
        clientNotificationListener.notifyBoard(tiles);
    }
    @Override
    public void notifyBookshelf(String nickname, Set<Tile> tiles) throws RemoteException {
        clientNotificationListener.notifyBookshelf(nickname, tiles);
    }
    @Override
    public void notifyPoints(String nickname, int points) throws RemoteException {
        clientNotificationListener.notifyPoints(nickname, points);
    }
    @Override
    public void notifyTurn(String nickname) throws RemoteException {
        clientNotificationListener.notifyTurn(nickname);
    }
    @Override
    public void notifyPersonalGoalCard(String nickname, String card) throws RemoteException {
        clientNotificationListener.notifyPersonalGoalCard(nickname, card);
    }
    @Override
    public void notifyCommonGoalCards(Map<String, List<Integer>> cardsToTokens) throws RemoteException {
        clientNotificationListener.notifyCommonCards(cardsToTokens);
    }
    @Override
    public void notifyCommonGoals(Set<String> goals) throws RemoteException {
        clientNotificationListener.notifyCommonGoals(goals);
    }
    @Override
    public void notifyChatMessage(String nickname, String message, String date) throws RemoteException {
        clientNotificationListener.notifyChatMessage(nickname, message, date);
    }
    @Override
    public void notifyDisconnection(String nickname) throws RemoteException {
        clientNotificationListener.notifyDisconnection(nickname);

    }
    @Override
    public void notifyGameSaved(String game) throws RemoteException {
        clientNotificationListener.notifyGameSaved(game);

    }
    @Override
    public void notifyPing() throws RemoteException {
        System.out.println("Ping received");
        clientNotificationListener.notifyPing();
    }
    @Override
    public void notifyReconnection(String nickname) throws RemoteException {
        clientNotificationListener.notifyReconnection(nickname);
    }

    @Override
    public void notifyError(String message) throws RemoteException {
        clientNotificationListener.notifyError(message);
    }

    @Override
    public void notifyPickedTiles(String nickname, List<TileType> tiles) throws RemoteException {
        clientNotificationListener.notifyPickedTiles(nickname, tiles);
    }


    public void notifyHello(boolean lobbyReady, String firstPlayerId, boolean loadedGame) {
        clientNotificationListener.notifyHello(lobbyReady, firstPlayerId, loadedGame);
    }
    public void notifySavedGames(Set<String> games) {
        clientNotificationListener.notifySavedGames(games);
    }
    public void notifyJoinResponse(boolean result, String error, String id) {
        clientNotificationListener.notifyJoinResponse(result, error, id);
    }
    public void notifyFirstJoinResponse(boolean result) {
        clientNotificationListener.notifyFirstJoinResponse(result);
    }
    public void notifyLoadGameResponse(boolean result, String error) {
        clientNotificationListener.notifyLoadGameResponse(result, error);
    }
    public void notifyLoadedGamePlayers(Set<String> nicknames) {
        clientNotificationListener.notifyLoadedGamePlayers(nicknames);
    }
}
