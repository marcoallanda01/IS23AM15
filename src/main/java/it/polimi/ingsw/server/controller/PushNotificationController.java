package it.polimi.ingsw.server.controller;

import it.polimi.ingsw.server.communication.ServerCommunication;
import it.polimi.ingsw.server.model.Tile;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

public class PushNotificationController {

    private final List<ServerCommunication> servers;
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

    public void notifyChangeBoard(List<Tile> tiles, boolean added){
        servers.forEach((s) -> s.notifyChangeBoard(tiles, added));
    }

    public void updatePlayerPoints(String playerName, int points){
        servers.forEach((s) -> s.updatePlayerPoints(playerName, points));
    }

    public void sendCommonGoalsCards(Map<String, List<Integer>> cardsAndTokens) {
        servers.forEach((s) -> s.sendCommonGoalsCards(cardsAndTokens));
    }

}
