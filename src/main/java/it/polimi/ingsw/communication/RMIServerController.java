package it.polimi.ingsw.communication;

import java.rmi.RemoteException;

public class RMIServerController implements ServerController {

    private RMIClientConnection rmiClientConnection;
    public RMIServerController(RMIClientConnection rmiClientConnection) {
        this.rmiClientConnection = rmiClientConnection;
    }
    @Override
    public void putTiles() throws RemoteException {
        rmiClientConnection.getServer().putTiles();
    }
}
