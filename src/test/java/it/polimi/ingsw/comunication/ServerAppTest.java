package it.polimi.ingsw.comunication;

import org.junit.jupiter.api.Test;

import java.rmi.RemoteException;

class ServerAppTest {

    @Test
    void RMICLITest() {
        ServerApp serverApp = new ServerApp("RMI CLI");
        ClientApp clientApp = new ClientApp("RMI CLI");
        ClientController cc = serverApp.getClientController();
        try{
            cc.notifyWinner("mynick");
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }
}