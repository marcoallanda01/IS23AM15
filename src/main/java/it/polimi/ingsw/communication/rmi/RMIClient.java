package it.polimi.ingsw.communication.rmi;

import it.polimi.ingsw.communication.responses.GameSetUp;
import it.polimi.ingsw.server.model.Tile;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface RMIClient extends Remote {
    void notifyGame(GameSetUp gameSetUp) throws RemoteException;
    void notifyWinner(String nickname) throws RemoteException;
    void notifyBoard(Set<Tile> tiles, boolean added) throws RemoteException;
    void notifyBookshelf(String nickname, Set<Tile> tiles) throws RemoteException;
    void notifyPoints(String nickname, int points) throws RemoteException;
    void notifyTurn(String nickname) throws RemoteException;
    void notifyPersonalGoalCard(String nickname, String card) throws RemoteException;
    void notifyCommonGoalCards(Map<String, List<Integer>> cardsToTokens) throws RemoteException;
    void notifyCommonGoals(Set<String> goals) throws RemoteException;
    void notifyChatMessage(String nickname, String message, String date) throws RemoteException;
    void notifyDisconnection(String nickname) throws RemoteException;
    void notifyGameSaved(String game) throws RemoteException;
    void notifyPing() throws RemoteException;
    void notifyReconnection(String nickname) throws RemoteException;
}
