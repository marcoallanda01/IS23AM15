package it.polimi.ingsw.comunication;

import it.polimi.ingsw.server.communication.GameSetUp;
import it.polimi.ingsw.server.model.Tile;

import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class TCPClientController implements ClientController{
    public TCPClientController(TCPServer tcpServer, NotificationHandler notificationHandler) {
    }

    @Override
    public void gameSetUp(GameSetUp gameSetUp) {

    }

    @Override
    public void notifyWinner(String nickname) {

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
    public void notifyChangePlayerPoints(String nickname, int points) {

    }

    @Override
    public void notifyChangeTurn(String nickname) {

    }

    @Override
    public void notifyChangePersonalGoalCard(String nickname, String card) throws RemoteException {

    }

    @Override
    public void notifyChangeCommonGoalCards(Map<String, List<Integer>> cardsToTokens) throws RemoteException {

    }

    @Override
    public void notifyChangeCommonGoals(Set<String> goals) throws RemoteException {

    }
}
