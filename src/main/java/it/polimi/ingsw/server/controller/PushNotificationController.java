package it.polimi.ingsw.server.controller;

import it.polimi.ingsw.server.communication.ServerCommunication;
import it.polimi.ingsw.server.model.Tile;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class PushNotificationController{

    /**
     * List of ServerCommunication to notify when there is a change
     */
    private final List<ServerCommunication> servers;

    /**
     * Register a server to witch notify changes in model
     * @param server server
     * @return true if server was added
     */
    public boolean addServer(ServerCommunication server){
        if(!servers.contains(server)){
            servers.add(server);
            return true;
        }
        return false;
    }

    /**
     * Delete subscription of a server to witch changes in model were notified
     * @param server server
     * @return true if server was removed
     */
    public boolean removeServer(ServerCommunication server){
        return servers.remove(server);
    }

    /**
     * Create controller to witch register to receive notification about the game
     * @param servers list of the servers
     */
    public PushNotificationController(@NotNull List<ServerCommunication> servers){
        this.servers = servers;
    }

    /**
     * Notify servers about current turn
     * @param playerName current turn player
     */
    public void notifyTurnChange(String playerName){
        servers.forEach((s) -> s.notifyTurn(playerName));
    }

    /**
     * Notify servers about a disconnection
     * @param playerName player who disconnected
     */
    public void notifyDisconnection(String playerName){
        servers.forEach((s) -> s.notifyDisconnection(playerName));
    }

    /**
     * Notify servers about a reconnection
     * @param playerName player who reconnected
     */
    public void notifyReconnection(String playerName){
        servers.forEach((s) -> s.notifyReconnection(playerName));
    }

    /**
     * Notify servers about a change in the board
     * @param tiles all tiles in the board
     */
    public void notifyChangeBoard(List<Tile> tiles){
        servers.forEach((s) -> s.notifyChangeBoard(tiles));
    }

    /**
     * Notify servers about a change in a player's points
     * @param playerName name of the player
     * @param points player's points updated
     */
    public void updatePlayerPoints(String playerName, int points){
        servers.forEach((s) -> s.updatePlayerPoints(playerName, points));
    }

    /**
     * Send to the servers common goals card change
     * @param cardsAndTokens new situation about common cards
     */
    public void notifyCommonGoalsCards(Map<String, List<Integer>> cardsAndTokens) {
        servers.forEach((s) -> s.sendCommonGoalsCards(cardsAndTokens));
    }

    /**
     * Notify servers about a change in a bookshelf
     * @param bookShelfTiles all tiles in the board
     * @param playerName bookshelf player
     */
    public void notifyChangeBookShelf(String playerName, List<Tile> bookShelfTiles){
        servers.forEach((s) -> s.notifyChangeBookShelf(playerName, bookShelfTiles));
    }

    /**
     * Notify servers about a stating game
     */
    public void notifyGameSetUp(){
        servers.forEach((s)->
            new Thread(s::gameSetUp).start()
        );
    }

    /**
     * Notify servers about the winner of the game
     * @param playerName winner's name
     */
    public void notifyWinner(String playerName){
        servers.forEach((s) -> s.notifyWinner(playerName));
    }

    /**
     * Notify servers about a message sent
     * @param sender sender's name
     * @param date date of the message
     * @param message actual message
     * @param receiver receiver's name, if empty means all players are receivers
     */
    public void notifyMessage(String sender, String date, String message, Optional<String> receiver){
        if(receiver.isPresent()){
            servers.forEach((s) -> s.notifyMessage(sender, date, message, receiver.get()));
        }else{
            servers.forEach((s) -> s.notifyMessage(sender, date, message));
        }
    }

    /**
     * Notify servers that no one is connected to the game anymore
     */
    public void notifyLastPlayerDisconnection(){
        servers.forEach(ServerCommunication::handleLastPlayerDisconnection);
    }
}
