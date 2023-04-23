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
    private RMIServerApp rmiServerApp;

    public RMIClientController(RMIServerApp rmiServerApp) {
        this.rmiServerApp = rmiServerApp;
    }

    @Override
    public void gameSetUp(GameSetUp gameSetUp) throws RemoteException{
        List<RMIClient> clients = rmiServerApp.getClients();
        for(RMIClient client : clients) {
            client.gameSetUp(gameSetUp);
        }
    }

    @Override
    public void notifyWinner(String nickname) throws RemoteException{
        List<RMIClient> clients = rmiServerApp.getClients();
        for(RMIClient client : clients) {
            client.notifyWinner(nickname);
        }
    }

    @Override
    public void notifyChangePlayers(List<String> nicknames) throws RemoteException{

    }

    @Override
    public void notifyChangeBoard(List<Tile> tiles) throws RemoteException{

    }

    @Override
    public void notifyChangeBookShelf(String nickname, List<Tile> tiles) throws RemoteException{

    }

    @Override
    public void updatePlayerPoints(String nickname, int points) throws RemoteException{

    }

    @Override
    public void notifyTurn(String nickname) throws RemoteException{

    }
}
