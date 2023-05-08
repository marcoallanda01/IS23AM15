package it.polimi.ingsw.client.communication;

import it.polimi.ingsw.client.View;
import it.polimi.ingsw.communication.responses.*;

import java.io.*;
import java.net.Socket;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;
import java.util.concurrent.*;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * this class handles the TCP connection:
 * opens it, closes it, handles notifications
 * receives and sends messages from and to the server
 */
public class TCPClientConnection implements Connection {
    private View view;
    private String hostname;
    private int port;
    private Socket socket;
    private Object readLock, writeLock;
    private ExecutorService executorService;
    private TCPClientResponseBuffer receivedResponsesAndNotifications;
    private Future<Void> bufferWriter;
    private Future<Void> notificationListener;

    private Integer waitingResponses;
    /**
     * @param view the view
     */
    public TCPClientConnection(String hostname, int port, View view) {
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
        executorService = new ForkJoinPool();
        receivedResponsesAndNotifications = new TCPClientResponseBuffer();
        waitingResponses = 0;
        try {
            // Create a socket to connect to the server
            socket = new Socket(hostname, port);
            bufferWriter = executorService.submit(() -> startBufferWriter());
            notificationListener = executorService.submit(() -> startNotificationListener());
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
                bufferWriter.cancel(Boolean.TRUE);
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
     * this method submits receiveStringFromServer to an executor service
     * @return a Future<String> that can be used from other classes to get the result (locking their thread)
     */
    public Future<String> receiveFromServer(Predicate<String> isExpectedResponse) {
        return executorService.submit(() -> receiveResponseFromServer(isExpectedResponse));
    }
    /**
     * this method sends a message to the server, it locks the writeLock
     * @param json the message to be sent to the server
     */
    private void sendStringToServer(String json) {
        synchronized (writeLock) {
            try {
                // Create output stream for communication with the server
                PrintWriter out = new PrintWriter(socket.getOutputStream());
                out.println(json);
            } catch (IOException e) {
                e.printStackTrace();
            }
            writeLock.notifyAll();
        }
    }
    /**
     * this method waits for a specific message to be received
     * @param isExpectedResponse a predicate that determines if the message is the right one
     * @return a String that is the response
     */
    private String receiveResponseFromServer(Predicate<String> isExpectedResponse) {
        Optional<String> response;
        do {
            response = receivedResponsesAndNotifications.popByPredicate(isExpectedResponse);
        }
        while (response.isEmpty());
        return response.get();
    }
    /**
     * this method listen for messages and writes them in the buffer all the time, unless interrupted
     */
    private Void startBufferWriter() {
        synchronized (readLock) {
            try {
                while (!Thread.currentThread().isInterrupted()) { // check interrupt flag
                    // Create output stream for communication with the server
                    Scanner in = new Scanner(socket.getInputStream());
                    String json = in.nextLine();
                    receivedResponsesAndNotifications.add(json);
                }
                return null;
            } catch (IOException e) {
                throw new ClientConnectionException();
            }
        }
    }
    /**
     * this method listens for notifications all the time, unless interrupted
     */
    private Void startNotificationListener() {
        while (!Thread.currentThread().isInterrupted()) { // check interrupt flag
            receiveNotificationFromServer();
        }
        return null;
    }
    /**
     * this method waits for any message, it also
     * removes it from the buffer if it is a
     * notification and has been handled correctly
     */
    private void receiveNotificationFromServer() {
        Optional<String> response;
        // scan for response
        do {
            response = receivedResponsesAndNotifications.getByPredicate((any) -> Boolean.TRUE);
        }
        while (response.isEmpty());
        // handle response
        if (handleNotification(response.get())) {
            // remove notification if response has been handled correctly
            receivedResponsesAndNotifications.remove(response.get());
        }
    }
    /**
     * this method handles the received message by calling the appropriate view method
     * @param json the notification string received from the server
     * @return true if the response has been handled correctly by the view, false otherwise
     */
    // TODO: handle notifications properly once view interface is definitive
    private Boolean handleNotification(String json) {
        if (BoardUpdate.fromJson(json).isPresent()) {
            BoardUpdate boardUpdate = BoardUpdate.fromJson(json).get();
            view.showBoard(boardUpdate.tiles);
        } else if (BookShelfUpdate.fromJson(json).isPresent()) {
            BookShelfUpdate bookShelfUpdate = BookShelfUpdate.fromJson(json).get();
            view.showBookshelf(bookShelfUpdate.player, bookShelfUpdate.tiles);
        } else if (ChatMessage.fromJson(json).isPresent()) {
            ChatMessage chatMessage = ChatMessage.fromJson(json).get();
            // do something with the chat message
        } else if (CommonCards.fromJson(json).isPresent()) {
            CommonCards commonCards = CommonCards.fromJson(json).get();
            view.showCommonGoalCards(commonCards.cardsAndTokens);
        } else if (CommonGoals.fromJson(json).isPresent()) {
            CommonGoals commonGoals = CommonGoals.fromJson(json).get();
            view.showCommonGoals(commonGoals.goals);
        } else if (Disconnection.fromJson(json).isPresent()) {
            Disconnection disconnection = Disconnection.fromJson(json).get();
            // do something with the disconnection
        } else if (GameSaved.fromJson(json).isPresent()) {
            GameSaved gameSaved = GameSaved.fromJson(json).get();
            // do something with the game saved
        } else if (GameSetUp.fromJson(json).isPresent()) {
            GameSetUp gameSetUp = GameSetUp.fromJson(json).get();
            view.showGame(gameSetUp);
        } else if (Ping.fromJson(json).isPresent()) {
            // do something with the ping
        } else if (PlayerPoints.fromJson(json).isPresent()) {
            PlayerPoints playerPoints = PlayerPoints.fromJson(json).get();
            view.showPoints(playerPoints.player, playerPoints.points);
        } else if (Reconnected.fromJson(json).isPresent()) {
            Reconnected reconnected = Reconnected.fromJson(json).get();
            // do something with the reconnection
        } else if (TurnNotify.fromJson(json).isPresent()) {
            TurnNotify turnNotify = TurnNotify.fromJson(json).get();
            view.showTurn(turnNotify.player);
        } else if (Winner.fromJson(json).isPresent()) {
            Winner winner = Winner.fromJson(json).get();
            view.showWinner(winner.player);
        } else {
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }
}