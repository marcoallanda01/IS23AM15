package it.polimi.ingsw.communication;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RMIServer extends Remote {
    void putTiles() throws RemoteException;
    void login(RMIClient rmiClient) throws RemoteException;
}
