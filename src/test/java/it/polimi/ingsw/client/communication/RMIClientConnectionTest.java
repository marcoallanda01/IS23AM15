package it.polimi.ingsw.client.communication;

import it.polimi.ingsw.communication.responses.*;
import it.polimi.ingsw.communication.rmi.RMIClient;
import it.polimi.ingsw.communication.rmi.RMIServer;
import it.polimi.ingsw.server.model.Tile;
import it.polimi.ingsw.server.model.TileType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

import static java.lang.Thread.sleep;
import static org.junit.jupiter.api.Assertions.assertEquals;


class RMIClientConnectionTest extends UnicastRemoteObject implements RMIServer {
    ClientNotificationListener clientNotificationListener;
    RMIClientConnection rmiClientConnection;
    List<String> notificationsSentToTheListener;
    RMIClient rmiClient;

    protected RMIClientConnectionTest() throws RemoteException {
        try {
            Registry registry = LocateRegistry.createRegistry(100);
            try {
                registry.bind("ServerRMIApp", this);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println("Server RMI ready");
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    @AfterEach
    void close() throws IOException, InterruptedException {
        this.rmiClientConnection.closeConnection();
    }

    @BeforeEach
    @Test
    void constructorTest() throws Exception {
        notificationsSentToTheListener = new ArrayList<>();
        clientNotificationListener = new ClientNotificationListener() {
            @Override
            public void notifyGame(GameSetUp gameSetUp) {
                notificationsSentToTheListener.add(gameSetUp.toJson());
            }

            @Override
            public void notifyWinner(String nickname) {
                notificationsSentToTheListener.add(nickname);
            }

            @Override
            public void notifyBoard(Set<Tile> tiles) {
                notificationsSentToTheListener.add(tiles.toString());
            }


            @Override
            public void notifyBookshelf(String nickname, Set<Tile> tiles) {
                notificationsSentToTheListener.add(nickname + tiles.toString());
            }

            @Override
            public void notifyPoints(String nickname, int points) {
                notificationsSentToTheListener.add(nickname + points);
            }

            @Override
            public void notifyTurn(String nickname) {
                notificationsSentToTheListener.add(nickname);
            }

            @Override
            public void notifyPersonalGoalCard(String nickname, String card) {
                notificationsSentToTheListener.add(nickname + card);
            }

            @Override
            public void notifyCommonGoalCards(Map<String, List<Integer>> cardsToTokens) {
                notificationsSentToTheListener.add(cardsToTokens.toString());
            }

            @Override
            public void notifyCommonGoals(Set<String> goals) {
                notificationsSentToTheListener.add(goals.toString());
            }

            @Override
            public void notifyChatMessage(String nickname, String message, String date) {
                notificationsSentToTheListener.add(nickname + message + date);
            }

            @Override
            public void notifyDisconnection(String nickname) {
                notificationsSentToTheListener.add(nickname);
            }

            @Override
            public void notifyGameSaved(String game) {
                notificationsSentToTheListener.add(game);
            }

            @Override
            public void notifyPing() {
                //notificationsSentToTheListener.add();
            }

            @Override
            public void notifyReconnection(String nickname) {
                notificationsSentToTheListener.add(nickname);
            }

            @Override
            public void notifyLoadedGamePlayers(Set<String> nicknames) {

            }

            @Override
            public void notifyHello(boolean lobbyReady, String firstPlayerId, boolean loadedGame) {
                notificationsSentToTheListener.add(lobbyReady + firstPlayerId + loadedGame);
            }

            @Override
            public void notifySavedGames(Set<String> games) {

            }

            @Override
            public void notifyJoinResponse(boolean result, String error, String id) {

            }

            @Override
            public void notifyLoadGameResponse(boolean result, String error) {

            }
        };
        rmiClientConnection = new RMIClientConnection("localhost", 100, clientNotificationListener);
        rmiClientConnection.openConnection();
        rmiClientConnection.notifyHello(this.hello().lobbyReady, this.hello().firstPlayerId,this.hello().loadedGame);
        assertEquals("[trueNoFirsttrue]", notificationsSentToTheListener.toString());
        this.joinNewAsFirst(rmiClientConnection,  "player1", 4, "123");
        assertEquals(rmiClientConnection, this.rmiClient);
    }

    @Override
    public Hello hello() throws RemoteException {
        return new Hello(true, true);
    }

    @Override
    public void joinNewAsFirst(RMIClient client, String player, int numPlayersGame, String idFirstPlayer) throws RemoteException {
        this.rmiClient = client;
    }

    @Override
    public void joinNewAsFirst(RMIClient client, String player, int numPlayersGame, String idFirstPlayer, boolean easyRules) throws RemoteException {
        this.rmiClient = client;
    }

    @Override
    public JoinResponse join(RMIClient client, String player) throws RemoteException {
        return null;
    }

    @Override
    public SavedGames getSavedGames() throws RemoteException {
        return null;
    }

    @Override
    public LoadGameResponse loadGame(String game, String idFirstPlayer) throws RemoteException {
        return null;
    }

    @Override
    public LoadedGamePlayers getLoadedGamePlayers() throws RemoteException {
        return null;
    }

    @Override
    public void joinLoadedAsFirst(RMIClient client, String player, String idFirstPlayer) throws RemoteException {

    }

    @Override
    public void disconnect(String playerId) throws RemoteException {

    }

    @Override
    public void reconnect(RMIClient client, String playerId) throws RemoteException {

    }

    @Override
    public void pickTiles(String playerId, Set<Tile> tiles) throws RemoteException {

    }

    @Override
    public void putTiles(String playerId, List<TileType> tiles, int column) throws RemoteException {

    }

    @Override
    public void saveGame(String playerId, String gameName) throws RemoteException {

    }

    @Override
    public void sendMessage(String playerId, String player, String message) throws RemoteException {

    }

    @Override
    public void pong(String playerId) throws RemoteException {

    }

}