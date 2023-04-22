package it.polimi.ingsw.comunication;

public class ServerApp {
    private ClientController clientController;

    public  ServerApp(String settings) {
        if (settings == "RMI") {
            clientController = new RMIClientController();
        } else {
            clientController = new TCPClientController();
        }
    }
}
