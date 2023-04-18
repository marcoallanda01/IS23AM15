package it.polimi.ingsw.server;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Optional;

public interface RMIServer extends Remote{
    public Optional<String> join(RMIClient client) throws RemoteException;
    void send (String message) throws RemoteException;
}
