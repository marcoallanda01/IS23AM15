package it.polimi.ingsw.comunication;

import it.polimi.ingsw.server.communication.GameSetUp;
import it.polimi.ingsw.server.model.Tile;

import java.net.Socket;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class TCPClientController implements ClientController {
    TCPServer tcpServer;
    public TCPClientController(TCPServer tcpServer) {
    }

    @Override
    public void gameSetUp(GameSetUp gameSetUp) {
        tcpServer.sendObjectToClients(gameSetUp);
    }

    @Override
    public void notifyWinner(String nickname) {
        tcpServer.sendObjectToClients(new Object());
    }

    @Override
    public void notifyChangePlayers(List<String> nicknames) {
        tcpServer.sendObjectToClients(new Object());
    }

    @Override
    public void notifyChangeBoard(List<Tile> tiles) {
        tcpServer.sendObjectToClients(new Object());
    }

    @Override
    public void notifyChangeBookShelf(String nickname, List<Tile> tiles) {
        tcpServer.sendObjectToClients(new Object());
    }

    @Override
    public void notifyChangePlayerPoints(String nickname, int points) {
        tcpServer.sendObjectToClients(new Object());
    }

    @Override
    public void notifyChangeTurn(String nickname) {
        tcpServer.sendObjectToClients(new Object());
    }

    @Override
    public void notifyChangePersonalGoalCard(String nickname, String card) throws RemoteException {
        tcpServer.sendObjectToClients(new Object());
    }

    @Override
    public void notifyChangeCommonGoalCards(Map<String, List<Integer>> cardsToTokens) throws RemoteException {
        tcpServer.sendObjectToClients(new Object());
    }

    @Override
    public void notifyChangeCommonGoals(Set<String> goals) throws RemoteException {
        tcpServer.sendObjectToClients(new Object());
    }
}
