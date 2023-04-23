package it.polimi.ingsw.comunication;

import it.polimi.ingsw.client.ViewController;
import it.polimi.ingsw.server.communication.GameSetUp;
import it.polimi.ingsw.server.model.Tile;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;

public class RMIClientApp implements RMIClient {
    private ViewController viewController;
    private RMIServer rmiServer;

    public RMIClientApp(ViewController viewController) throws Exception {
        this.viewController = viewController;
        this.start();
    }

    private void start() throws Exception {
        // Getting the registry
        Registry registry;

        registry = LocateRegistry.getRegistry(Settings.SERVER_NAME, Settings.PORT);

        // Looking up the registry for the remote object
        this.rmiServer = (RMIServer) registry.lookup("ChatService");
        this.rmiServer.login(this);
    }
    @Override
    public void gameSetUp(GameSetUp gameSetUp) {
        viewController.showGame(gameSetUp);
    }

    @Override
    public void notifyWinner(String nickname) {
        viewController.showWinner(nickname);
    }

    @Override
    public void notifyChangePlayers(List<String> nicknames) {

    }

    @Override
    public void notifyChangeBoard(List<Tile> tiles) {

    }

    @Override
    public void notifyChangeBookShelf(String nickname, List<Tile> tiles) {

    }

    @Override
    public void updatePlayerPoints(String nickname, int points) {

    }

    @Override
    public void notifyTurn(String nickname) {

    }
}
