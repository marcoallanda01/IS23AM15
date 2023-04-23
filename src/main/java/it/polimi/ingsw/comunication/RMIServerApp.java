package it.polimi.ingsw.comunication;

import it.polimi.ingsw.server.controller.ChatController;
import it.polimi.ingsw.server.controller.Lobby;
import it.polimi.ingsw.server.controller.PlayController;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.List;

public class RMIServerApp implements RMIServer{
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
    }

    @Override
    public void setLobby(Lobby lobby) {
        this.lobby = lobby;
    }

    @Override
    public void setPlayController(PlayController playController) {
        this.playController = playController;
    }

    @Override
    public void setChatController(ChatController chatController) {
        this.chatController = chatController;
    }

    public void putTiles(){
        // call putTiles on the model controller
    }

    @Override
    public List<RMIClient> getClients() {
        return this.rmiClients;
    }
}
