package it.polimi.ingsw.comunication;

import it.polimi.ingsw.client.ViewController;
import it.polimi.ingsw.server.communication.GameSetUp;
import it.polimi.ingsw.server.model.Tile;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface RMIClient extends Remote {
    // send one GameSetUp object to every player
    public void gameSetUp(GameSetUp gameSetUp) throws RemoteException;
    // notifications methods
    public void notifyWinner(String nickname) throws RemoteException;
    public void notifyChangePlayers(List<String> nicknames) throws RemoteException;
    public void notifyChangeBoard(List<Tile> tiles) throws RemoteException;
    public void notifyChangeBookShelf(String nickname, List<Tile> tiles) throws RemoteException;
    public void updatePlayerPoints(String nickname, int points) throws RemoteException;
    public void notifyTurn(String nickname) throws RemoteException;
}
