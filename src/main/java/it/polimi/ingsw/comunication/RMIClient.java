package it.polimi.ingsw.comunication;

import java.rmi.Remote;

public interface RMIClient extends Remote {

    void printPoints();
}
