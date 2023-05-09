package it.polimi.ingsw.client.communication;

import it.polimi.ingsw.communication.responses.*;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.*;

/**
 * this class handles the TCP connection:
 * opens it, closes it, handles notifications
 * receives and sends messages from and to the server
 */
public class TCPClientConnection implements ClientConnection {
    private ClientNotificationListener clientNotificationListener;
    private String hostname;
    private int port;
    private Socket socket;
    private Object readLock, writeLock;
    private ExecutorService executorService;
    private Future<Void> notificationListener;

    private Integer waitingResponses;
    public  Socket getSocket() {
        return socket;
    }
    /**
     * @param clientNotificationListener the clientNotificationListener
     */
    public TCPClientConnection(String hostname, int port, ClientNotificationListener clientNotificationListener) {
        this.clientNotificationListener = clientNotificationListener;
        this.hostname = hostname;
        this.port = port;
    }
    /**
     * this method opens the TCP connection,
     * initializes the executorService
     * starts listening to notifications
     */
    public void openConnection() {
        readLock = new Object();
        writeLock = new Object();
        executorService = Executors.newCachedThreadPool();
        waitingResponses = 0;
        try {
            // Create a socket to connect to the server
            socket = new Socket(hostname, port);
            notificationListener = executorService.submit(() -> startNotificationHandler());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * this method closes the TCP connection
     * closes the executorService
     * stops listening to notifications
     */
    public void closeConnection() {
        try {
            if (socket != null) {
                socket.close();
                executorService.close();
                notificationListener.cancel(Boolean.TRUE);
                System.out.println("Socket closed.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * this method submits sendStringToServer to an executor service
     */
    public void sendToServer(String json) {
        executorService.submit(()->sendStringToServer(json));
    }

    /**
     * this method sends a message to the server, it locks the writeLock
     * @param json the message to be sent to the server
     */
    private void sendStringToServer(String json) {
        synchronized (writeLock) {
            try {
                // Create output stream for communication with the server
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                out.println(json);
            } catch (IOException e) {
                e.printStackTrace();
            }
            writeLock.notifyAll();
        }
    }
    /**
     * this method listen for messages and writes them in the buffer all the time, unless interrupted
     */
    private Void startNotificationHandler() {
        synchronized (readLock) {
            try {
                while (!Thread.currentThread().isInterrupted()) { // check interrupt flag
                    // Create output stream for communication with the server
                    Scanner in = new Scanner(socket.getInputStream());
                    String json = in.nextLine();
                    executorService.submit(()->dispatchNotification(json));
                }
                return null;
            } catch (IOException e) {
                throw new ClientConnectionException();
            }
        }
    }
    /**
     * this method handles the received message by calling the appropriate view method
     * @param json the notification string received from the server
     * @return true if the response has been handled correctly by the view, false otherwise
     */
    private Boolean dispatchNotification(String json) {
        if (BoardUpdate.fromJson(json).isPresent()) {
            BoardUpdate boardUpdate = BoardUpdate.fromJson(json).get();
            clientNotificationListener.notifyBoard(boardUpdate.tiles);
        } else if (BookShelfUpdate.fromJson(json).isPresent()) {
            BookShelfUpdate bookShelfUpdate = BookShelfUpdate.fromJson(json).get();
            clientNotificationListener.notifyBookshelf(bookShelfUpdate.player, bookShelfUpdate.tiles);
        } else if (ChatMessage.fromJson(json).isPresent()) {
            ChatMessage chatMessage = ChatMessage.fromJson(json).get();
            clientNotificationListener.notifyChatMessage(chatMessage.sender, chatMessage.message, chatMessage.date);
        } else if (CommonCards.fromJson(json).isPresent()) {
            CommonCards commonCards = CommonCards.fromJson(json).get();
            clientNotificationListener.notifyCommonGoalCards(commonCards.cardsAndTokens);
        } else if (CommonGoals.fromJson(json).isPresent()) {
            CommonGoals commonGoals = CommonGoals.fromJson(json).get();
            clientNotificationListener.notifyCommonGoals(commonGoals.goals);
        } else if (Disconnection.fromJson(json).isPresent()) {
            Disconnection disconnection = Disconnection.fromJson(json).get();
            clientNotificationListener.notifyDisconnection(disconnection.player);
        } else if (GameSaved.fromJson(json).isPresent()) {
            GameSaved gameSaved = GameSaved.fromJson(json).get();
            clientNotificationListener.notifyGameSaved(gameSaved.game);
        } else if (GameSetUp.fromJson(json).isPresent()) {
            GameSetUp gameSetUp = GameSetUp.fromJson(json).get();
            clientNotificationListener.notifyGame(gameSetUp);
        }  else if (Hello.fromJson(json).isPresent()) {
            Hello hello = Hello.fromJson(json).get();
            clientNotificationListener.notifyHello(hello.lobbyReady, hello.firstPlayerId, hello.loadedGame);
        }  else if (JoinResponse.fromJson(json).isPresent()) {
            JoinResponse joinResponse = JoinResponse.fromJson(json).get();
            clientNotificationListener.notifyJoinResponse(joinResponse.result, joinResponse.error, joinResponse.id);
        } else if (LoadedGamePlayers.fromJson(json).isPresent()) {
            LoadedGamePlayers loadedGamePlayers = LoadedGamePlayers.fromJson(json).get();
            clientNotificationListener.notifyLoadedGamePlayers(loadedGamePlayers.names);
        } else if (LoadGameResponse.fromJson(json).isPresent()) {
            LoadGameResponse loadGameResponse = LoadGameResponse.fromJson(json).get();
            clientNotificationListener.notifyLoadGameResponse(loadGameResponse.result, loadGameResponse.error);
        } else if (Ping.fromJson(json).isPresent()) {
            Ping ping = Ping.fromJson(json).get();
            clientNotificationListener.notifyPing();
        } else if (PlayerPoints.fromJson(json).isPresent()) {
            PlayerPoints playerPoints = PlayerPoints.fromJson(json).get();
            clientNotificationListener.notifyPoints(playerPoints.player, playerPoints.points);
        } else if (Reconnected.fromJson(json).isPresent()) {
            Reconnected reconnected = Reconnected.fromJson(json).get();
            clientNotificationListener.notifyReconnection(reconnected.toString());
        } else if (SavedGames.fromJson(json).isPresent()) {
            SavedGames savedGames = SavedGames.fromJson(json).get();
            clientNotificationListener.notifySavedGames(savedGames.names);
        }  else if (TurnNotify.fromJson(json).isPresent()) {
            TurnNotify turnNotify = TurnNotify.fromJson(json).get();
            clientNotificationListener.notifyTurn(turnNotify.player);
        } else if (Winner.fromJson(json).isPresent()) {
            Winner winner = Winner.fromJson(json).get();
            clientNotificationListener.notifyWinner(winner.player);
        } else {
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }
}