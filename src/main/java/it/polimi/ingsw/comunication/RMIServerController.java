package it.polimi.ingsw.comunication;

public class RMIServerController implements ServerController {
    private RMIServer rmiServer;
    @Override
    public void putTiles() {
        rmiServer.putTiles();
    }
}
