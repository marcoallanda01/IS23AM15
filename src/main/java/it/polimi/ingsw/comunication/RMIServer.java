package it.polimi.ingsw.comunication;

import it.polimi.ingsw.server.controller.ChatController;
import it.polimi.ingsw.server.controller.Lobby;
import it.polimi.ingsw.server.controller.PlayController;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface RMIServer extends Remote {
    void putTiles() throws RemoteException;
    void login(RMIClient rmiClient) throws RemoteException;
}
