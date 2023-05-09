package it.polimi.ingsw.client.communication;

import it.polimi.ingsw.client.View;
import it.polimi.ingsw.communication.responses.BookShelfUpdate;
import it.polimi.ingsw.communication.responses.GameSetUp;
import it.polimi.ingsw.communication.responses.Winner;
import it.polimi.ingsw.server.model.Message;
import it.polimi.ingsw.server.model.Tile;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
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

    @BeforeEach
    @Test
    void constructorTest() throws InterruptedException {
            try {
                ServerSocket serverSocket = new ServerSocket(100);
                serverExecutorService = Executors.newCachedThreadPool();
                Future<?> acceptingThread = serverExecutorService.submit(() -> {
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
                public void notifyPlayers(Set<String> nicknames) {
                    notificationsSentToTheListener.add(nicknames.toString());
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
            };
            tcpClientConnection = new TCPClientClientConnection("localhost", 100, clientNotificationListener);
            tcpClientConnection.openConnection();
            assertEquals(new TCPClientResponseBuffer(), tcpClientConnection.getBuffer());
    }
    @Test
    void gameNotificationTest() throws InterruptedException, ExecutionException {
        Future<?> sentMessage = serverExecutorService.submit(() -> sendToClient(new GameSetUp(new ArrayList<>(), new ArrayList<>()).toJson()));
        Thread.sleep(500);
        assertEquals(List.of(sentMessage.get()).toString(), notificationsSentToTheListener.toString());
    }
    @Test
    void winnerNotificationTest() throws InterruptedException, ExecutionException {
        Future<?> sentMessage = serverExecutorService.submit(() -> sendToClient(new Winner("player1").toJson()));
        Thread.sleep(500);
        assertEquals(List.of("player1").toString(), notificationsSentToTheListener.toString());
    }
    public String sendToClient(String json) throws IOException {
        try {
            PrintWriter out;
            out = new PrintWriter(serverSideClientSocket.getOutputStream());
            out.println(json);
            out.close();
        } catch (IOException e) {
                e.printStackTrace();
        } finally {
            serverSideClientSocket.close();
            return json;
        }
    }
}