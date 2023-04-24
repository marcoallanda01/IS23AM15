package it.polimi.ingsw.communication;

import it.polimi.ingsw.server.communication.GameSetUp;
import it.polimi.ingsw.server.model.Tile;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class RMIClientController implements ClientController, Remote {
    private RMIServerConnection rmiServerConnection;

    public RMIClientController(RMIServerConnection rmiServerConnection) {
        this.rmiServerConnection = rmiServerConnection;
    }

    @Override
    public void gameSetUp(GameSetUp gameSetUp) throws RemoteException{
        List<RMIClient> clients = rmiServerConnection.getClients();
        for(RMIClient client : clients) {
            client.gameSetUp(gameSetUp);
        }
    }

    @Override
    public void notifyWinner(String nickname) throws RemoteException{
        List<RMIClient> clients = rmiServerConnection.getClients();
        for(RMIClient client : clients) {
            client.notifyWinner(nickname);
        }
    }

    @Override
    public void notifyChangePlayers(List<String> nicknames) throws RemoteException{
        List<RMIClient> clients = rmiServerConnection.getClients();
        for(RMIClient client : clients) {
            client.notifyChangePlayers(nicknames);
        }
    }

    @Override
    public void notifyChangeBoard(List<Tile> tiles) throws RemoteException{
        List<RMIClient> clients = rmiServerConnection.getClients();
        for(RMIClient client : clients) {
            client.notifyChangeBoard(tiles);
        }
    }

    @Override
    public void notifyChangeBookShelf(String nickname, List<Tile> tiles) throws RemoteException{
        List<RMIClient> clients = rmiServerConnection.getClients();
        for(RMIClient client : clients) {
            client.notifyChangeBookShelf(nickname, tiles);
        }
    }

    @Override
    public void notifyChangePlayerPoints(String nickname, int points) throws RemoteException{
        List<RMIClient> clients = rmiServerConnection.getClients();
        for(RMIClient client : clients) {
            client.notifyChangePlayerPoints(nickname, points);
        }
    }

    @Override
    public void notifyChangeTurn(String nickname) throws RemoteException{
        List<RMIClient> clients = rmiServerConnection.getClients();
        for(RMIClient client : clients) {
            client.notifyChangeTurn(nickname);
        }
    }

    @Override
    public void notifyChangePersonalGoalCard(String nickname, String card) throws RemoteException {
        List<RMIClient> clients = rmiServerConnection.getClients();
        for(RMIClient client : clients) {
            client.notifyChangePersonalGoalCard(nickname, card);
        }
    }

    @Override
    public void notifyChangeCommonGoalCards(Map<String, List<Integer>> cardsToTokens) throws RemoteException {
        List<RMIClient> clients = rmiServerConnection.getClients();
        for(RMIClient client : clients) {
            client.notifyChangeCommonGoalCards(cardsToTokens);
        }
    }

    @Override
    public void notifyChangeCommonGoals(Set<String> goals) throws RemoteException {
        List<RMIClient> clients = rmiServerConnection.getClients();
        for(RMIClient client : clients) {
            client.notifyChangeCommonGoals(goals);
        }
    }
}
