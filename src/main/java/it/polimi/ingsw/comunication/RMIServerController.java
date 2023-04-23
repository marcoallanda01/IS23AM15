package it.polimi.ingsw.comunication;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RMIServerController implements ServerController {
    private RMIServer rmiServer;

    public RMIServerController() {

    }

    @Override
    public void putTiles() {
        rmiServer.putTiles();
    }
}
