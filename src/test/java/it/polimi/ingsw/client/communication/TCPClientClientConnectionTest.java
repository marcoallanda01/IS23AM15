package it.polimi.ingsw.client.communication;

import it.polimi.ingsw.client.View;
import it.polimi.ingsw.communication.responses.BoardUpdate;
import it.polimi.ingsw.communication.responses.BookShelfUpdate;
import it.polimi.ingsw.communication.responses.GameSetUp;
import it.polimi.ingsw.communication.responses.Winner;
import it.polimi.ingsw.server.model.Message;
import it.polimi.ingsw.server.model.Tile;
import it.polimi.ingsw.server.model.TileType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static java.lang.Thread.sleep;
import static org.junit.jupiter.api.Assertions.assertEquals;


class TCPClientClientConnectionTest {
    ExecutorService serverExecutorService = Executors.newCachedThreadPool();
    ClientNotificationListener clientNotificationListener;
    TCPClientClientConnection tcpClientConnection;
    List<String> notificationsSentToTheListener = new ArrayList<>();
    Socket serverSideClientSocket;
    ServerSocket serverSocket;

    Future<?> acceptingThread;
    @AfterEach
    void close() throws IOException, InterruptedException {
        this.serverSocket.close();
    }

    @BeforeEach
    @Test
    void constructorTest() throws InterruptedException, ExecutionException {
            try {
                this.serverSocket = new ServerSocket(100);
                serverExecutorService = Executors.newCachedThreadPool();
                this.acceptingThread = serverExecutorService.submit(() -> {
                    try {
                        this.serverSideClientSocket = serverSocket.accept();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            } catch (IOException e) {
                System.err.println(e.getMessage());
            }
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
                public void notifyBoard(Set<Tile> tiles, boolean added) {
                    notificationsSentToTheListener.add(tiles.toString() + added);
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
                    notificationsSentToTheListener.add();
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
            tcpClientConnection = new TCPClientClientConnection("localhost", 100, clientNotificationListener);
            tcpClientConnection.openConnection();
            acceptingThread.get();
            assertEquals(new TCPClientResponseBuffer(), tcpClientConnection.getBuffer());
    }
    //TODO: assert buffer in while loop
    @Test
    void gameNotificationTest() throws InterruptedException, ExecutionException {
        Future<?> sentMessage = serverExecutorService.submit(() -> sendToClient(new GameSetUp(new ArrayList<>(), new ArrayList<>()).toJson()));
        while (!tcpClientConnection.getBuffer().toString().equals((new TCPClientResponseBuffer()).toString())) {
            Thread.sleep(500);
        }
        Thread.sleep(500);
        assertEquals(List.of(new GameSetUp(new ArrayList<>(), new ArrayList<>()).toJson()).toString(), notificationsSentToTheListener.toString());
    }
    @Test
    void winnerNotificationTest() throws InterruptedException, ExecutionException {
        Future<?> sentMessage = serverExecutorService.submit(() -> sendToClient(new Winner("player1").toJson()));
        sentMessage.get();
        while (!tcpClientConnection.getBuffer().toString().equals((new TCPClientResponseBuffer()).toString())) {
            Thread.sleep(500);
        }
        Thread.sleep(500);
        assertEquals(List.of("player1").toString(), notificationsSentToTheListener.toString());
    }
    @Test
    void boardNotificationTest() throws InterruptedException, ExecutionException {
        Future<?> sentMessage = serverExecutorService.submit(() -> sendToClient(new BoardUpdate(Set.of(new Tile(TileType.BOOK)), true).toJson()));
        sentMessage.get();
        while (!tcpClientConnection.getBuffer().toString().equals((new TCPClientResponseBuffer()).toString())) {
            Thread.sleep(100);
        }
        Thread.sleep(500);
        assertEquals(List.of(Set.of(new Tile(TileType.BOOK)).toString() + "true").toString(), notificationsSentToTheListener.toString());
    }
    @Test
    void bookshelfNotificationTest() throws InterruptedException, ExecutionException {
        Future<?> sentMessage = serverExecutorService.submit(() -> sendToClient(new BookShelfUpdate("player2", Set.of(new Tile(TileType.BOOK))).toJson()));
        sentMessage.get();
        while (!tcpClientConnection.getBuffer().toString().equals((new TCPClientResponseBuffer()).toString())) {
            Thread.sleep(100);
        }
        Thread.sleep(500);
        assertEquals(List.of("player2"+Set.of(new Tile(TileType.BOOK))).toString(), notificationsSentToTheListener.toString());
    }
    public String sendToClient(String json) throws IOException {
        try {
            PrintWriter out;
            out = new PrintWriter(serverSideClientSocket.getOutputStream());
            out.println(json);
            out.close();
        } catch (IOException e) {
                e.printStackTrace();
            if (serverSideClientSocket != null)
                serverSideClientSocket.close();
        } finally {
            return json;
        }
    }
}