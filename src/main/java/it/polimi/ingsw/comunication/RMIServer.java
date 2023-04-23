package it.polimi.ingsw.comunication;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface RMIServer extends Remote {
    void putTiles();
    List<RMIClient> getClients();
    void login(RMIClient rmiClient)  throws RemoteException;
}
