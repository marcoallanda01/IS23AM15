package it.polimi.ingsw.comunication;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RMIServerController implements ServerController {

    private RMIClientApp rmiClientApp;
    public RMIServerController(RMIClientApp rmiClientApp) {
        this.rmiClientApp = rmiClientApp;
    }
    @Override
    public void putTiles() throws RemoteException {
        rmiClientApp.getServer().putTiles();
    }
}
