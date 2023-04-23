package it.polimi.ingsw.comunication;

import it.polimi.ingsw.client.ViewController;
import it.polimi.ingsw.server.communication.GameSetUp;
import it.polimi.ingsw.server.model.Tile;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

public class RMIClientApp extends UnicastRemoteObject implements RMIClient {
    private ViewController viewController;
    private RMIServer rmiServer;

    public RMIClientApp() throws Exception {
        this.start();
    }
    public void setViewController(ViewController viewController) {
        this.viewController = viewController;
    }
    private void start() throws Exception {
        // Getting the registry
        Registry registry;

        registry = LocateRegistry.getRegistry(Settings.SERVER_NAME, Settings.PORT);

        // Looking up the registry for the remote object
        this.rmiServer = (RMIServer) registry.lookup("ServerService");
        this.rmiServer.login(this);
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

    }

    @Override
    public void notifyChangeBoard(List<Tile> tiles)  throws RemoteException {

    }

    @Override
    public void notifyChangeBookShelf(String nickname, List<Tile> tiles)  throws RemoteException {

    }

    @Override
    public void updatePlayerPoints(String nickname, int points) throws RemoteException {

    }

    @Override
    public void notifyTurn(String nickname) throws RemoteException {

    }

    public RMIServer getServer() {
        return this.rmiServer;
    }
}
