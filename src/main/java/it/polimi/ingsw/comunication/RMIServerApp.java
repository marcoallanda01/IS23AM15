package it.polimi.ingsw.comunication;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.List;

public class RMIServerApp implements RMIServer{
    private List<RMIClient> rmiClients;

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
    public void putTiles(){
        // call putTiles on the model controller
    }

    @Override
    public List<RMIClient> getClients() {
        return this.rmiClients;
    }
}
