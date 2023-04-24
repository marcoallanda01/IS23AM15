package it.polimi.ingsw.comunication;

import it.polimi.ingsw.client.ViewController;
import it.polimi.ingsw.server.communication.GameSetUp;
import it.polimi.ingsw.server.model.Tile;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class RMIClientConnection extends UnicastRemoteObject implements RMIClient, Connection{
    private ViewController viewController;
    private RMIServer rmiServer;

    public RMIClientConnection() throws Exception {
    }
    public void setViewController(ViewController viewController) {
        this.viewController = viewController;
    }
    public void openConnection() throws Exception {
        // Getting the registry
        Registry registry;

        registry = LocateRegistry.getRegistry(Settings.SERVER_NAME, Settings.PORT);

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
            System.err.println("Error closing RMI connection: " + e.getMessage());
        }
    }
    @Override
    public void gameSetUp(GameSetUp gameSetUp)  throws RemoteException {
        viewController.showGame(gameSetUp);
    }

    @Override
    public void notifyWinner(String nickname)  throws RemoteException {
        viewController.showWinner(nickname);
    }

    @Override
    public void notifyChangePlayers(List<String> nicknames)  throws RemoteException {
        viewController.showPlayers(nicknames);
    }

    @Override
    public void notifyChangeBoard(List<Tile> tiles)  throws RemoteException {
        viewController.showBoard(tiles);
    }

    @Override
    public void notifyChangeBookShelf(String nickname, List<Tile> tiles)  throws RemoteException {
        viewController.showBookshelf(nickname, tiles);
    }

    @Override
    public void notifyChangePlayerPoints(String nickname, int points) throws RemoteException {
        viewController.showPoints(nickname, points);
    }

    @Override
    public void notifyChangeTurn(String nickname) throws RemoteException {
        viewController.showTurn(nickname);
    }

    @Override
    public void notifyChangePersonalGoalCard(String nickname, String card) throws RemoteException {
        viewController.showPersonalGoalCard(nickname, card);
    }

    @Override
    public void notifyChangeCommonGoalCards(Map<String, List<Integer>> cardsToTokens) throws RemoteException {
        viewController.showCommonGoalCards(cardsToTokens);
    }

    @Override
    public void notifyChangeCommonGoals(Set<String> goals) throws RemoteException {
        viewController.showCommonGoals(goals);
    }

    public RMIServer getServer() {
        return this.rmiServer;
    }
}
