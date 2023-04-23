package it.polimi.ingsw.comunication;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RMIServerController implements ServerController {

    private RMIClient rmiClient;
    public RMIServerController(RMIClient rmiClient) {
        this.rmiClient = rmiClient;
    }
    @Override
    public void putTiles() {
        rmiClient.getServer().putTiles();
    }
}
