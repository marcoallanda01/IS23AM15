package it.polimi.ingsw.communication;

import it.polimi.ingsw.server.model.Tile;
import it.polimi.ingsw.server.model.TileType;
import org.junit.jupiter.api.Test;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

class ServerAppTest {

    @Test
    void RMICLITest() {
        ServerApp serverApp = new ServerApp("RMI CLI");
        ClientApp clientApp = new ClientApp("RMI CLI");
        ClientController cc = serverApp.getClientController();
        try{
            cc.notifyWinner("mynick");

            List<String> p = new ArrayList<>();
            p.add("p1");
            p.add("p2");
            cc.notifyChangePlayers(p);

            List<Tile> b = new ArrayList<>();
            b.add(new Tile(TileType.BOOK));
            b.add(new Tile(TileType.TROPHY));
            cc.notifyChangeBoard(b);

            List<Tile> t = new ArrayList<>();
            t.add(new Tile(TileType.BOOK));
            t.add(new Tile(TileType.TROPHY));
            cc.notifyChangeBookShelf("p1", t);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }
}