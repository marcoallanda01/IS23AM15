package it.polimi.ingsw.comunication;

public class TCPServerController implements ServerController {
    private NotificationHandler notificationHandler;
    private TCPServer tcpServer;
    public TCPServerController(TCPServer tcpServer, NotificationHandler notificationHandler) {
        this.tcpServer = tcpServer;
        this.notificationHandler = notificationHandler;
    }
    @Override
    public void putTiles() {
        // connection.send(putTilesCommand();
        notificationHandler.startListening();
        // command = connection.receive();
        // if command instanceof putTilesResponse;
        // logic to handle
        // return
        // else
        // notificationHandler.handle(command);
    }
}
