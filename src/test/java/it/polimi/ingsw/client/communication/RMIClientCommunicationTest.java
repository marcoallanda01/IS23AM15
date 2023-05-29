package it.polimi.ingsw.client.communication;

import it.polimi.ingsw.client.Client;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * for simplicity, this test also implements RMIServer, this test class is also the server itself
 * @throws RemoteException
 */
class RMIClientCommunicationTest extends UnicastRemoteObject implements RMIServer {
    ClientNotificationListener clientNotificationListener;
    RMIClientConnection rmiClientConnection;
    RMIClientCommunication rmiClientCommunication;
    List<String> notificationsSentToTheListener;
    RMIClient rmiClient;

    protected RMIClientCommunicationTest() throws RemoteException {
        Client.main();
        try {
            Registry registry = LocateRegistry.createRegistry(1002);
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
            public synchronized void notifyGame(GameSetUp gameSetUp) {
                notificationsSentToTheListener.add(gameSetUp.toJson());
            }

            @Override
            public synchronized void notifyWinner(String nickname) {
                notificationsSentToTheListener.add(nickname);
            }

            @Override
            public synchronized void notifyBoard(Set<Tile> tiles) {
                notificationsSentToTheListener.add(tiles.toString());
            }


            @Override
            public synchronized void notifyBookshelf(String nickname, Set<Tile> tiles) {
                notificationsSentToTheListener.add(nickname + tiles.toString());
            }

            @Override
            public synchronized void notifyPoints(String nickname, int points) {
                notificationsSentToTheListener.add(nickname + points);
            }

            @Override
            public synchronized void notifyTurn(String nickname) {
                notificationsSentToTheListener.add(nickname);
            }

            @Override
            public synchronized void notifyPersonalGoalCard(String nickname, String card) {
                notificationsSentToTheListener.add(nickname + card);
            }

            @Override
            public synchronized void notifyCommonCards(Map<String, List<Integer>> cardsToTokens) {
                notificationsSentToTheListener.add(cardsToTokens.toString());
            }

            @Override
            public synchronized void notifyCommonGoals(Set<String> goals) {
                notificationsSentToTheListener.add(goals.toString());
            }

            @Override
            public synchronized void notifyChatMessage(String nickname, String message, String date) {
                notificationsSentToTheListener.add(nickname + message + date);
            }

            @Override
            public synchronized void notifyDisconnection(String nickname) {
                notificationsSentToTheListener.add(nickname);
            }

            @Override
            public synchronized void notifyGameSaved(String game) {
                notificationsSentToTheListener.add(game);
            }

            @Override
            public synchronized void notifyPing() {
                //notificationsSentToTheListener.add();
            }

            @Override
            public synchronized void notifyReconnection(String nickname) {
                notificationsSentToTheListener.add(nickname);
            }

            @Override
            public synchronized void notifyFirstJoinResponse(boolean result) {
                notificationsSentToTheListener.add(String.valueOf(result));
            }

            @Override
            public synchronized void notifyLoadedGamePlayers(Set<String> nicknames) {

            }

            @Override
            public synchronized void notifyHello(boolean lobbyReady, String firstPlayerId, boolean loadedGame) {
                notificationsSentToTheListener.add(lobbyReady + firstPlayerId + loadedGame);
            }

            @Override
            public void notifySavedGames(Set<String> games) {

            }

            @Override
            public synchronized void notifyJoinResponse(boolean result, String error, String id) {

            }

            @Override
            public synchronized void notifyLoadGameResponse(boolean result, String error) {

            }

            @Override
            public synchronized void notifyError(String message) {
                notificationsSentToTheListener.add(message);
            }

            @Override
            public void notifyPickedTiles(String nickname, List<TileType> tiles) {

            }
        };
        rmiClientConnection = new RMIClientConnection("localhost", 1002, clientNotificationListener);
        rmiClientCommunication = new RMIClientCommunication(rmiClientConnection);
        rmiClientConnection.openConnection();
    }
    @Test
    void helloTest() throws RemoteException, InterruptedException {
        rmiClientCommunication.hello();
        Thread.sleep(500);
        assertEquals("[trueNoFirsttrue]", notificationsSentToTheListener.toString());
    }
    @Test
    void joinNewAsFirstTest() throws RemoteException, InterruptedException {
        rmiClientCommunication.joinNewAsFirst("player1", 4, "123");
        Thread.sleep(500);
        assertEquals("[tutto ok, true]", notificationsSentToTheListener.toString());
    }

    @Override
    public Hello hello(RMIClient client) throws RemoteException {
        return new Hello(true, true);
    }

    @Override
    public FirstJoinResponse joinNewAsFirst(RMIClient client, String player, int numPlayersGame, String idFirstPlayer) throws RemoteException {
        System.out.println(client);
        this.rmiClient = client;
        // in this implementation joinNewAsFirst also sends a push notification to the client
        this.rmiClient.notifyError("tutto ok");
        return new FirstJoinResponse(true);
    }

    @Override
    public FirstJoinResponse joinNewAsFirst(RMIClient client, String player, int numPlayersGame, String idFirstPlayer, boolean easyRules) throws RemoteException {
        this.rmiClient = client;
        return new FirstJoinResponse(true);
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
    public FirstJoinResponse joinLoadedAsFirst(RMIClient client, String player, String idFirstPlayer) throws RemoteException {
        return null;
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
    public void sendMessage(String playerId, String message) throws RemoteException {

    }

    @Override
    public void pong(String playerId) throws RemoteException {

    }

}