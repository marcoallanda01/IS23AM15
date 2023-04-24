package it.polimi.ingsw.comunication;

import it.polimi.ingsw.server.communication.GameSetUp;
import it.polimi.ingsw.server.communication.TCPServer;
import it.polimi.ingsw.server.model.Tile;

import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;
import java.util.Set;
/*
public class TCPClientController implements ClientController {
    TCPServer tcpServer;
    public TCPClientController(TCPServer tcpServer) {
    }

    @Override
    public void gameSetUp(GameSetUp gameSetUp) {
        tcpServer.sendMsgToAll(gameSetUp);
    }

    @Override
    public void notifyWinner(String nickname) {
        //tcpServer.sendMsgToAll(new Object());
    }

    @Override
    public void notifyChangePlayers(List<String> nicknames) {
        //tcpServer.sendMsgToAll(new Object());
    }

    @Override
    public void notifyChangeBoard(List<Tile> tiles) {
        //tcpServer.sendMsgToAll(new Object());
    }

    @Override
    public void notifyChangeBookShelf(String nickname, List<Tile> tiles) {
       // tcpServer.sendMsgToAll(new Object());
    }

    @Override
    public void notifyChangePlayerPoints(String nickname, int points) {
       //tcpServer.sendMsgToAll(new Object());
    }

    @Override
    public void notifyChangeTurn(String nickname) {
       // tcpServer.sendMsgToAll(new Object());
    }

    @Override
    public void notifyChangePersonalGoalCard(String nickname, String card) throws RemoteException {
       // tcpServer.sendMsgToAll(new Object());
    }

    @Override
    public void notifyChangeCommonGoalCards(Map<String, List<Integer>> cardsToTokens) throws RemoteException {
        //tcpServer.sendMsgToAll(new Object());
    }

    @Override
    public void notifyChangeCommonGoals(Set<String> goals) throws RemoteException {
        //tcpServer.sendMsgToAll(new Object());
    }
}*/
