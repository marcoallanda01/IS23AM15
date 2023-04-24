package it.polimi.ingsw.communication;

import it.polimi.ingsw.client.ViewController;
import it.polimi.ingsw.server.controller.PlayController;
import it.polimi.ingsw.server.model.Tile;
import it.polimi.ingsw.server.model.TileType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.rmi.RemoteException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class RMIServerCommunicationTest {
    private static ServerApp serverApp;
    private static ClientApp clientApp;
    private static ViewController viewController;
    private static ClientController clientController;
    private static List<ServerCommunication> serverCommunications;

    @BeforeAll
    public static void setUp() {
        serverApp = new ServerApp("RMI CLI");
        clientApp = new ClientApp("RMI CLI");
        try {
            serverApp.getConnection().openConnection();
            clientApp.getConnection().openConnection();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        clientController = serverApp.getClientController();
        serverCommunications = serverApp.getControllerProvider().getPlayController().getServerCommunications();
        viewController = clientApp.getViewController();
    }
    void gameSetUpTest() {}
    @Test
    void notifyWinnerTest() {
        try {
            this.serverCommunications.get(0).notifyWinner("winner player");
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
        assertEquals("winner is: winner player", this.viewController.getLastShowed());
    }
    @Test
    void notifyChangePlayersTest() {
        List<String> p = new ArrayList<>();
        p.add("p1");
        p.add("p2");
        try {
            this.serverCommunications.get(0).notifyChangePlayers(p);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
        assertEquals("players are: [p1, p2]", this.viewController.getLastShowed());
    }
    @Test
    void notifyChangeBoard() {
        List<Tile> b = new ArrayList<>();
        b.add(new Tile(TileType.BOOK));
        b.add(new Tile(TileType.TROPHY));
        try {
            this.serverCommunications.get(0).notifyChangeBoard(b);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
        assertEquals("board tiles are: [Tile{x=-1, y=-1, type=BOOK}, Tile{x=-1, y=-1, type=TROPHY}]", this.viewController.getLastShowed());
    }
    @Test
    void notifyChangeBookShelf() {
        List<Tile> t = new ArrayList<>();
        t.add(new Tile(TileType.BOOK));
        t.add(new Tile(TileType.TROPHY));
        try {
            this.serverCommunications.get(0).notifyChangeBookShelf("p1", t);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
        assertEquals("p1's bookshelf tiles are: [Tile{x=-1, y=-1, type=BOOK}, Tile{x=-1, y=-1, type=TROPHY}]", this.viewController.getLastShowed());
    }
    @Test
    void notifyChangePlayerPoints() {
        try {
            this.serverCommunications.get(0).notifyChangePlayerPoints("p1", 10);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
        assertEquals("p1's points are: 10", this.viewController.getLastShowed());
    }
    @Test
    void notifyChangeTurn() {
        try {
            this.serverCommunications.get(0).notifyChangeTurn("p1");
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
        assertEquals("p1's turn started", this.viewController.getLastShowed());
    }
    @Test
    void notifyChangePersonalGoalCard() {
        try {
            this.serverCommunications.get(0).notifyChangePersonalGoalCard("p1", "fancyCard");
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
        assertEquals("p1's card has changed: fancyCard", this.viewController.getLastShowed());
    }
    @Test
    void notifyChangeCommonGoalCards() {
        Map<String, List<Integer>> cardsToTokens = new HashMap<>();
        List<Integer> tokens = new ArrayList<>();
        tokens.add(2);
        tokens.add(4);
        cardsToTokens.put("whateverCard", tokens);
        try {
            this.serverCommunications.get(0).notifyChangeCommonGoalCards(cardsToTokens);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
        assertEquals("common goal cards have changed: " + cardsToTokens, this.viewController.getLastShowed());
    }
    @Test
    void notifyChangeCommonGoals() {
        Set<String> goals = new HashSet<>();
        goals.add("myGoal1");
        goals.add(("myGoal2"));
        try {
            this.serverCommunications.get(0).notifyChangeCommonGoals(goals);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
        assertEquals("common goals have changed: " + goals, this.viewController.getLastShowed());
    }
}
