package it.polimi.ingsw.server.controller;

import it.polimi.ingsw.server.communication.ServerCommunication;
import it.polimi.ingsw.server.model.Tile;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

public class PushNotificationController{

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

    public void notifyTurnChange(String playerName){
        servers.forEach((s) -> s.notifyTurn(playerName));
    }

    public void notifyDisconnection(String playerName){
        servers.forEach((s) -> s.notifyDisconnection(playerName));
    }
    public void notifyReconnection(String playerName){
        servers.forEach((s) -> s.notifyReconnection(playerName));
    }

    public void notifyChangeBoard(List<Tile> tiles){
        servers.forEach((s) -> s.notifyChangeBoard(tiles));
    }

    public void updatePlayerPoints(String playerName, int points){
        servers.forEach((s) -> s.updatePlayerPoints(playerName, points));
    }

    public void sendCommonGoalsCards(Map<String, List<Integer>> cardsAndTokens) {
        servers.forEach((s) -> s.sendCommonGoalsCards(cardsAndTokens));
    }

    public void notifyChangeBookShelf(String playerName, List<Tile> bookShelfTiles){
        servers.forEach((s) -> s.notifyChangeBookShelf(playerName, bookShelfTiles));
    }

    public void notifyGameSetUp(){
        servers.forEach(ServerCommunication::gameSetUp);
    }

    public void notifyWinner(String playerName){
        servers.forEach((s) -> s.notifyWinner(playerName));
    }

}
