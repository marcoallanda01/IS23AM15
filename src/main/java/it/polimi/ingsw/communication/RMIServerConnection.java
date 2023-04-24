package it.polimi.ingsw.communication;

import it.polimi.ingsw.server.controller.ChatController;
import it.polimi.ingsw.server.controller.Lobby;
import it.polimi.ingsw.server.controller.PlayController;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

public class RMIServerConnection extends UnicastRemoteObject implements RMIServer, Connection{
    private List<RMIClient> rmiClients;
    private ChatController chatController;
    private PlayController playController;
    private Lobby lobby;

    public RMIServerConnection() throws RemoteException {
        this.rmiClients = new ArrayList<>();
    }
    public void openConnection() throws RemoteException {
        // Bind the remote object's stub in the registry
        //DO NOT CALL Registry registry = LocateRegistry.getRegistry();
        Registry registry = LocateRegistry.createRegistry(TCPClient.Settings.PORT);
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
        this.playController.addServerCommunication(new RMIServerCommunication(c));
        System.out.println("client logged in");
    }

    // Method to close RMI connection
    public void closeConnection() {
        try {
            // Unexport the remote object
            UnicastRemoteObject.unexportObject(this, true);
            System.out.println("RMI server connection closed.");
        } catch (RemoteException e) {
            System.err.println("Error closing RMI connection: " + e.getMessage());
        }
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
