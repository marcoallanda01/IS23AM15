package it.polimi.ingsw.client.communication;

import it.polimi.ingsw.communication.rmi.RMIClient;
import it.polimi.ingsw.communication.rmi.RMIServer;
import it.polimi.ingsw.communication.responses.GameSetUp;
import it.polimi.ingsw.server.model.Tile;

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
        this.rmiServer.login(this);
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
    @Override
    public void gameSetUp(GameSetUp gameSetUp)  throws RemoteException {
        clientNotificationListener.notifyGame(gameSetUp);
    }

    @Override
    public void notifyWinner(String nickname)  throws RemoteException {
        clientNotificationListener.notifyWinner(nickname);
    }

    @Override
    public void notifyChangePlayers(Set<String> nicknames)  throws RemoteException {
        clientNotificationListener.notifyPlayers(nicknames);
    }

    @Override
    public void notifyChangeBoard(Set<Tile> tiles)  throws RemoteException {
        clientNotificationListener.notifyBoard(tiles);
    }

    @Override
    public void notifyChangeBookShelf(String nickname, Set<Tile> tiles)  throws RemoteException {
        clientNotificationListener.notifyBookshelf(nickname, tiles);
    }

    @Override
    public void notifyChangePlayerPoints(String nickname, int points) throws RemoteException {
        clientNotificationListener.notifyPoints(nickname, points);
    }

    @Override
    public void notifyChangeTurn(String nickname) throws RemoteException {
        clientNotificationListener.notifyTurn(nickname);
    }

    @Override
    public void notifyChangePersonalGoalCard(String nickname, String card) throws RemoteException {
        clientNotificationListener.notifyPersonalGoalCard(nickname, card);
    }

    @Override
    public void notifyChangeCommonGoalCards(Map<String, List<Integer>> cardsToTokens) throws RemoteException {
        clientNotificationListener.notifyCommonGoalCards(cardsToTokens);
    }

    @Override
    public void notifyChangeCommonGoals(Set<String> goals) throws RemoteException {
        clientNotificationListener.notifyCommonGoals(goals);
    }

    public RMIServer getServer() {
        return this.rmiServer;
    }
}
