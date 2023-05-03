package it.polimi.ingsw.communication.rmi;

import it.polimi.ingsw.server.communication.GameSetUp;
import it.polimi.ingsw.server.model.Tile;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface RMIClient extends Remote {
    // send one GameSetUp object to every player
    public void gameSetUp(GameSetUp gameSetUp) throws RemoteException;
    // notifications methods
    public void notifyWinner(String nickname) throws RemoteException;
    public void notifyChangePlayers(List<String> nicknames) throws RemoteException;
    public void notifyChangeBoard(List<Tile> tiles) throws RemoteException;
    public void notifyChangeBookShelf(String nickname, List<Tile> tiles) throws RemoteException;
    public void notifyChangePlayerPoints(String nickname, int points) throws RemoteException;
    public void notifyChangeTurn(String nickname) throws RemoteException;
    void notifyChangePersonalGoalCard(String nickname, String card) throws RemoteException;
    void notifyChangeCommonGoalCards(Map<String, List<Integer>> cardsToTokens) throws RemoteException;
    void notifyChangeCommonGoals(Set<String> goals) throws RemoteException;
}
