package it.polimi.ingsw.server.controller;

import it.polimi.ingsw.server.communication.ServerCommunication;
import it.polimi.ingsw.server.listeners.TurnListener;
import it.polimi.ingsw.server.model.Tile;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class PushNotificationController {

    private List<ServerCommunication> servers;
    private TurnListener turnListener;
    public PushNotificationController(@NotNull List<ServerCommunication> servers){
        this.servers = servers;
    }

    public void notifyTurnChange(String playerName){
        servers.forEach((s) -> s.notifyTurn(playerName));
    }

    public void notifyDisconnection(String playerName){}
    public void notifyReconnection(String playerName){}

    public void notifyChangeBoard(List<Tile> tiles, boolean added){}

}
