package it.polimi.ingsw.comunication;

public class ClientApp {
    private ServerController serverAPI;

    public  ClientApp(String settings) {
        if (settings == "RMI") {
            serverAPI = new RMIServerController();
        } else {
            serverAPI = new TCPServerController(new TCPServer(), new NotificationHandler());
        }
    }
}
