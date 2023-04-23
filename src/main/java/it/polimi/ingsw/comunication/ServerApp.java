package it.polimi.ingsw.comunication;

import java.rmi.RemoteException;

public class ServerApp {
    private ClientController clientController;

    public  ServerApp(String settings) {
        if (settings == "RMI") {
            try {
                clientController = new RMIClientController();
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        } else {
            clientController = new TCPClientController();
        }
    }
}
