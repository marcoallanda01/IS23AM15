package it.polimi.ingsw.comunication;

import it.polimi.ingsw.server.communication.responses.Disconnection;

public class NotificationHandler {
    private TCPClient tcpClient;
    public NotificationHandler() {
        // starts listening on the channel
    }
    public void handle(Object notification) {
        if (notification instanceof Disconnection) {
            return;
        }
        return;
    }
    public void startListening() {}
    public void stopListening() {}
}
