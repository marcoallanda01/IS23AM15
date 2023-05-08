package it.polimi.ingsw.client.communication;

import it.polimi.ingsw.client.View;
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
 * Class that creates the RMI client connection endpoint (implementing Connection)
 * this class is also an UnicastRemoteObject accessible from the server through the interface RMIClient
 * therefore this class is responsible for receiving notifications from the server
 * it does also offer the possibility to retrieve a reference to the RMIServer Interface representing
 * the UnicastRemoteObject registered from the server
 */
public class RMIClientConnection extends UnicastRemoteObject implements RMIClient, Connection{
    private View view;
    private RMIServer rmiServer;
    private final String hostname;
    private final int port;

    public RMIClientConnection(String hostname, int port, View view) throws Exception {
        this.hostname = hostname;
        this.port = port;
        this.view = view;
    }
    public void setViewController(View view) {
        this.view = view;
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
        view.showGame(gameSetUp);
    }

    @Override
    public void notifyWinner(String nickname)  throws RemoteException {
        view.showWinner(nickname);
    }

    @Override
    public void notifyChangePlayers(Set<String> nicknames)  throws RemoteException {
        view.showPlayers(nicknames);
    }

    @Override
    public void notifyChangeBoard(Set<Tile> tiles)  throws RemoteException {
        view.showBoard(tiles);
    }

    @Override
    public void notifyChangeBookShelf(String nickname, Set<Tile> tiles)  throws RemoteException {
        view.showBookshelf(nickname, tiles);
    }

    @Override
    public void notifyChangePlayerPoints(String nickname, int points) throws RemoteException {
        view.showPoints(nickname, points);
    }

    @Override
    public void notifyChangeTurn(String nickname) throws RemoteException {
        view.showTurn(nickname);
    }

    @Override
    public void notifyChangePersonalGoalCard(String nickname, String card) throws RemoteException {
        view.showPersonalGoalCard(nickname, card);
    }

    @Override
    public void notifyChangeCommonGoalCards(Map<String, List<Integer>> cardsToTokens) throws RemoteException {
        view.showCommonGoalCards(cardsToTokens);
    }

    @Override
    public void notifyChangeCommonGoals(Set<String> goals) throws RemoteException {
        view.showCommonGoals(goals);
    }

    public RMIServer getServer() {
        return this.rmiServer;
    }
}
