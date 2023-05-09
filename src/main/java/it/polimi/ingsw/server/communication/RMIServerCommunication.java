package it.polimi.ingsw.server.communication;

import it.polimi.ingsw.communication.rmi.RMIClient;
import it.polimi.ingsw.server.controller.Lobby;
import it.polimi.ingsw.server.model.Tile;

import java.rmi.RemoteException;
import java.util.*;

public class RMIServerCommunication implements ServerCommunication{

    private final List<RMIClient> clientsInGame;
    public RMIServerCommunication(int port, Lobby lobby) throws RemoteException {
        this.clientsInGame = Collections.synchronizedList(new ArrayList<>());
    }

    /**
     * Add clients that play the game
     * @param client client to add to playing clients
     */
    public void addPlayingClient(RMIClient client){
        if(!clientsInGame.contains(client)){
            clientsInGame.add(client);
        }
    }

    /**
     * Remove playing client
     * @param client client who disconnected
     */
    public void removeClient(RMIClient client){
        clientsInGame.remove(client);
    }

    /**
     * Send one GameSetUp object to every player
     */
    @Override
    public void gameSetUp() {

    }

    /**
     * Send notification of the winner to all players
     *
     * @return true if there is a winner and the notification is sent, false otherwise
     */
    @Override
    public boolean sendWinner() {
        return false;
    }

    /**
     * If in game, function notifies the disconnection of a player to all the others
     *
     * @param playerName player that disconnect
     */
    @Override
    public void notifyDisconnection(String playerName) {

    }

    @Override
    public void notifyReconnection(String playerName) {

    }

    @Override
    public void notifyChangeBoard(List<Tile> tiles, boolean added) {

    }

    @Override
    public void notifyChangeBookShelf(String playerName, List<Tile> tiles) {

    }

    @Override
    public void updatePlayerPoints(String playerName, int points) {

    }

    @Override
    public void notifyTurn(String playerName) {

    }

    @Override
    public void sendCommonGoalsCards(Map<String, List<Integer>> cardsAndTokens) {

    }

    @Override
    public void notifyChangeToken(String card, List<Integer> tokens) {

    }
}
