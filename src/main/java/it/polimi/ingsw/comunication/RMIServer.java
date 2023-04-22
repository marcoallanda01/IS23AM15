package it.polimi.ingsw.comunication;

import java.rmi.Remote;

public interface RMIServer extends Remote {
    void putTiles();
}
