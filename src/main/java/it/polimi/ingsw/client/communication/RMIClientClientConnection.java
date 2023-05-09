package it.polimi.ingsw.client.communication;

import it.polimi.ingsw.communication.rmi.RMIClient;
import it.polimi.ingsw.communication.rmi.RMIServer;
import it.polimi.ingsw.communication.responses.GameSetUp;
import it.polimi.ingsw.server.model.Tile;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashSet;
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
public class RMIClientClientConnection extends UnicastRemoteObject implements RMIClient, ClientConnection {
    private ClientNotificationListener clientNotificationListener;
    private RMIServer rmiServer;
    private final String hostname;
    private final int port;

    public RMIClientClientConnection(String hostname, int port, ClientNotificationListener clientNotificationListener) throws Exception {
        this.hostname = hostname;
        this.port = port;
        this.clientNotificationListener = clientNotificationListener;
    }
    public void openConnection() throws Exception {
        // Getting the registry
        Registry registry;

        registry = LocateRegistry.getRegistry(hostname, port);

        // Looking up the registry for the remote object
        this.rmiServer = (RMIServer) registry.lookup("ServerService");
    }
    // Method to close RMI connection
    public void closeConnection() {
        try {
            // Unexport the remote object
            UnicastRemoteObject.unexportObject(this, true);
            System.out.println("RMI client connection closed.");
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
        clientNotificationListener.notifyCommonGoalCards(cardsToTokens);
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
        clientNotificationListener.notifyPing();
    }
    @Override
    public void notifyReconnection(String nickname) throws RemoteException {
        clientNotificationListener.notifyReconnection(nickname);
    }


    public void notifyHello(boolean lobbyReady, String firstPlayerId, boolean loadedGame) throws RemoteException {
        clientNotificationListener.notifyHello(lobbyReady, firstPlayerId, loadedGame);
    }
    public void notifySavedGames(Set<String> games) throws RemoteException {
        clientNotificationListener.notifySavedGames(games);
    }
    public void notifyJoinResponse(boolean result, String error, String id) throws RemoteException {
        clientNotificationListener.notifyJoinResponse(result, error, id);
    }
    public void notifyLoadGameResponse(boolean result, String error) throws RemoteException {
        clientNotificationListener.notifyLoadGameResponse(result, error);
    }
    public void notifyLoadedGamePlayers(Set<String> nicknames) throws RemoteException {
        clientNotificationListener.notifyLoadedGamePlayers(nicknames);
    }
}
