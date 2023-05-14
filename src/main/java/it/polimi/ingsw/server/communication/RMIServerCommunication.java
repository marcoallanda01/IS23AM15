package it.polimi.ingsw.server.communication;

import it.polimi.ingsw.communication.responses.GameSetUp;
import it.polimi.ingsw.communication.rmi.RMIClient;
import it.polimi.ingsw.server.controller.Lobby;
import it.polimi.ingsw.server.model.Tile;

import java.rmi.RemoteException;
import java.util.*;

public class RMIServerCommunication extends ResponseServer implements ServerCommunication{

    private final List<RMIClient> clientsInGame;
    public RMIServerCommunication(Lobby lobby, String sharedLock){
        super(lobby, sharedLock);
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

    //ServerCommunication methods
    /**
     * Send one GameSetUp object to every player
     */
    @Override
    public void gameSetUp() {
        tryStartGame();
        this.clientsInGame.forEach((c) -> {
            try {
                c.notifyGame( new GameSetUp(
                        playController.getPlayers(),
                        new ArrayList<>(playController.getEndGameGoals())
                ));
            } catch (RemoteException e) {
                System.err.println("RMI gameSetUp: Remote Exception thrown with client "+c);
            }
        });
    }

    /**
     * If in game, function notifies the disconnection of a player to all the others
     *
     * @param playerName player that disconnect
     */
    @Override
    public void notifyDisconnection(String playerName) {
        this.clientsInGame.forEach(c->{
            try {
                c.notifyDisconnection(playerName);
            } catch (RemoteException e) {
                System.err.println("RMI notifyDisconnection: Remote Exception thrown with client "+c);
            }
        });
    }

    /**
     * Notify to all clients that a player reconnected
     *
     * @param playerName name of the player who reconnected
     */
    @Override
    public void notifyReconnection(String playerName) {
        this.clientsInGame.forEach(c->{
            try {
                c.notifyReconnection(playerName);
            } catch (RemoteException e) {
                System.err.println("RMI notifyReconnection: Remote Exception thrown with client "+c);
            }
        });
    }

    /**
     * Send a message to all players
     *
     * @param sender  sender's name
     * @param date    date of message creation
     * @param message actual message to be sent
     */
    @Override
    public void notifyMessage(String sender, String date, String message) {
        this.clientsInGame.forEach(c->{
            try {
                c.notifyChatMessage(sender, date, message);
            } catch (RemoteException e) {
                System.err.println("RMI notifyMessage: Remote Exception thrown with client "+c);
            }
        });
    }

    /**
     * Send a message to all players
     *
     * @param sender   sender's name
     * @param date     date of message creation
     * @param message  actual message to be sent
     * @param receiver receiver's name
     */
    @Override
    public void notifyMessage(String sender, String date, String message, String receiver) {
        // TODO
        /*try {
                this.clientsInGame.notifyChatMessage(sender, date, message);
            } catch (RemoteException e) {
                System.err.println("RMI notifyReconnection: Remote Exception thrown with client "+c);
            }
        });*/
    }

    /**
     * Notify change in the board to all clients in game
     *
     * @param tiles board
     */
    @Override
    public void notifyChangeBoard(List<Tile> tiles) {

    }

    /**
     * Notify to all clients change in player's bookshelf
     *
     * @param playerName player's name
     * @param tiles      bookshelf
     */
    @Override
    public void notifyChangeBookShelf(String playerName, List<Tile> tiles) {

    }

    /**
     * Notify change in point of a player to all clients
     *
     * @param playerName player's name
     * @param points     new points
     */
    @Override
    public void updatePlayerPoints(String playerName, int points) {

    }

    /**
     * Notify to all player whom turn is
     *
     * @param playerName current player
     */
    @Override
    public void notifyTurn(String playerName) {

    }

    /**
     * Notify to all clients a change in common goals cards and tokens
     *
     * @param cardsAndTokens cards with associated tokens
     */
    @Override
    public void sendCommonGoalsCards(Map<String, List<Integer>> cardsAndTokens) {

    }

    /**
     * Send notification of the winner to all players
     *
     * @param playerName name of the winner
     */
    @Override
    public void notifyWinner(String playerName) {

    }

    /**
     * Handle the disconnection of the last player terminating the game
     */
    @Override
    public void handleLastPlayerDisconnection() {

    }


    //ResponseServer methods

    /**
     * Close a client connection
     *
     * @param client client object. A cast is needed
     */
    @Override
    protected void closeClient(Object client) {

    }

    /**
     * Get name of a playing client form his connection
     *
     * @param client client object. A cast is needed
     * @return client's nickname. Return null if client is not in game
     */
    @Override
    protected String getPlayerNameFromClient(Object client) {
        return null;
    }

    /**
     * Add a playing client
     *
     * @param client object (need cast)
     * @param id     player's id
     */
    @Override
    protected void addPlayingClient(Object client, String id) {

    }

    /**
     * Ping a client to keep connection alive
     *
     * @param client object
     */
    @Override
    protected void ping(Object client) {

    }
}
