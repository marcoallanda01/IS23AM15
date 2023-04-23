package it.polimi.ingsw.comunication;

import it.polimi.ingsw.server.communication.GameSetUp;
import it.polimi.ingsw.server.model.Tile;

import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.List;

public class RMIClientController implements ClientController, Remote {
    private RMIServer rmiServer;
    public RMIClientController() throws RemoteException {
        this.rmiServer = new RMIServerApp();
    }


    @Override
    public void gameSetUp(GameSetUp gameSetUp) {
        rmiServer.getClients().forEach(c -> c.gameSetUp(gameSetUp));
    }

    @Override
    public void notifyWinner(String nickname) {
        rmiServer.getClients().forEach(c -> c.notifyWinner(nickname));
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
