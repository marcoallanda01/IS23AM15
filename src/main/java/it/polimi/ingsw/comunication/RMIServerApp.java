package it.polimi.ingsw.comunication;

import it.polimi.ingsw.server.controller.ChatController;
import it.polimi.ingsw.server.controller.Lobby;
import it.polimi.ingsw.server.controller.PlayController;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

public class RMIServerApp extends UnicastRemoteObject implements RMIServer{
    private List<RMIClient> rmiClients;
    private ChatController chatController;
    private PlayController playController;
    private Lobby lobby;
    public RMIServerApp() throws RemoteException {
        this.rmiClients = new ArrayList<>();
        this.start();
    }
    private void start() throws RemoteException {
        // Bind the remote object's stub in the registry
        //DO NOT CALL Registry registry = LocateRegistry.getRegistry();
        Registry registry = LocateRegistry.createRegistry(Settings.PORT);
        try {
            registry.bind("ServerService", this);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Server ready");
    }

    public void login(RMIClient c) throws RemoteException {
        this.rmiClients.add(c);
        System.out.println("client logged in");
    }


    public void setLobby(Lobby lobby)  throws RemoteException {
        this.lobby = lobby;
    }


    public void setPlayController(PlayController playController) {
        this.playController = playController;
    }


    public void setChatController(ChatController chatController) {
        this.chatController = chatController;
    }

    public void putTiles(){
        // call putTiles on the model controller
    }


    public List<RMIClient> getClients()  throws RemoteException {
        return this.rmiClients;
    }
}
