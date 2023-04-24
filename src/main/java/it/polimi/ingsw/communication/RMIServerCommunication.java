package it.polimi.ingsw.communication;

import it.polimi.ingsw.server.communication.GameSetUp;
import it.polimi.ingsw.server.model.Tile;

import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class RMIServerCommunication implements ServerCommunication{
    private RMIClient rmiClient;
    public RMIServerCommunication(RMIClient rmiClient) {
        this.rmiClient = rmiClient;
    }

    @Override
    public void gameSetUp(GameSetUp gameSetUp) throws RemoteException{
        rmiClient.gameSetUp(gameSetUp);
    }

    @Override
    public void notifyWinner(String nickname) throws RemoteException{
        rmiClient.notifyWinner(nickname);
    }

    @Override
    public void notifyChangePlayers(List<String> nicknames) throws RemoteException{
        rmiClient.notifyChangePlayers(nicknames);
    }

    @Override
    public void notifyChangeBoard(List<Tile> tiles) throws RemoteException{
        rmiClient.notifyChangeBoard(tiles);
    }

    @Override
    public void notifyChangeBookShelf(String nickname, List<Tile> tiles) throws RemoteException{
        rmiClient.notifyChangeBookShelf(nickname, tiles);
    }

    @Override
    public void notifyChangePlayerPoints(String nickname, int points) throws RemoteException{
        rmiClient.notifyChangePlayerPoints(nickname, points);
    }

    @Override
    public void notifyChangeTurn(String nickname) throws RemoteException{
        rmiClient.notifyChangeTurn(nickname);
    }

    @Override
    public void notifyChangePersonalGoalCard(String nickname, String card) throws RemoteException {
        rmiClient.notifyChangePersonalGoalCard(nickname, card);
    }

    @Override
    public void notifyChangeCommonGoalCards(Map<String, List<Integer>> cardsToTokens) throws RemoteException {
        rmiClient.notifyChangeCommonGoalCards(cardsToTokens);
    }

    @Override
    public void notifyChangeCommonGoals(Set<String> goals) throws RemoteException {
        rmiClient.notifyChangeCommonGoals(goals);
    }
}
