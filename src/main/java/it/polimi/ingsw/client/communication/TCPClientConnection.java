package it.polimi.ingsw.client.communication;

import it.polimi.ingsw.client.View;
import it.polimi.ingsw.communication.responses.*;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.*;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * this class handles the TCP connection:
 * opens it, closes it, responds to notifications
 * receives and sends messages from and to the server
 */
public class TCPClientConnection implements Connection {
    private View view;
    private String hostname;
    private int port;
    private Socket socket;
    private Object readLock, writeLock;
    private ExecutorService executorService;
    private Future<Void> notificationListener;
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
        try {
            // Create a socket to connect to the server
            socket = new Socket(hostname, port);
            notificationListener = executorService.submit(() -> listenForNotifications());
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
     * this method handles the received message by calling the appropriate view method
     * @param json the notification string received from the server
     */
    // TODO: handle notifications properly once view interface is definitive
    private void handleNotification(String json) {
        if (BoardUpdate.fromJson(json).isPresent()) {
            BoardUpdate boardUpdate = BoardUpdate.fromJson(json).get();
            view.showBoard(boardUpdate.tiles);
        } else if (BookShelfUpdate.fromJson(json).isPresent()) {
            BookShelfUpdate bookShelfUpdate = BookShelfUpdate.fromJson(json).get();
            view.showBookshelf(bookShelfUpdate.player, bookShelfUpdate.tiles);
        } else if (ChatMessage.fromJson(json).isPresent()) {
            ChatMessage chatMessage = ChatMessage.fromJson(json).get();
        } else if (CommonCards.fromJson(json).isPresent()) {
            CommonCards commonCards = CommonCards.fromJson(json).get();
            view.showCommonGoalCards(commonCards.cardsAndTokens);
        } else if (CommonGoals.fromJson(json).isPresent()) {

        } else if (Disconnection.fromJson(json).isPresent()) {

        } else if (GameSaved.fromJson(json).isPresent()) {

        } else if (GameSetUp.fromJson(json).isPresent()) {

        } else if (Ping.fromJson(json).isPresent()) {

        } else if (PlayerPoints.fromJson(json).isPresent()) {

        } else if (Reconnected.fromJson(json).isPresent()) {

        } else if (TurnNotify.fromJson(json).isPresent()) {

        } else if (Winner.fromJson(json).isPresent()) {

        } else {
            throw new ClientConnectionException("Unrecognized server message");
        }
    }
    /**
     * this method listen for notifications all the time, unless interrupted
     * it submits handleNotification to the executorService, it locks the readLock
     */
    private Void listenForNotifications() {
        synchronized (readLock) {
            try {
                while (!Thread.currentThread().isInterrupted()) { // check interrupt flag
                    // Create output stream for communication with the server
                    Scanner in = new Scanner(socket.getInputStream());
                    String json = in.nextLine();
                    // handling the notification in a separate thread, allowing for multiple notifications
                    executorService.submit(() -> handleNotification(json));
                }
                return null;
            } catch (IOException e) {
                throw new ClientConnectionException();
            }
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
                PrintWriter out = new PrintWriter(socket.getOutputStream());
                out.println(json);
            } catch (IOException e) {
                e.printStackTrace();
            }
            writeLock.notifyAll();
        }
    }
    /**
     * this method submits receiveStringFromServer to an executor service
     * @return a Future<String> that can be used from other classes to get the result (locking their thread)
     */
    public Future<String> receiveFromServer(Predicate<String> isExpectedResponse) {
        return executorService.submit(() -> receiveStringFromServer(isExpectedResponse));
    }
    /**
     * this method waits for a specific message to be received
     * @param isExpectedResponse a predicate that determines if the message is the right one
     * @return a String that is the response
     */
    private String receiveStringFromServer(Predicate<String> isExpectedResponse) {
        notificationListener.cancel(Boolean.TRUE);
        synchronized (readLock) {
            try {
                // Create output stream for communication with the server
                Scanner in = new Scanner(socket.getInputStream());
                String json = in.nextLine();
                while(!isExpectedResponse.test(json)) {
                    // handling the notification in a separate thread, allowing for multiple notifications
                    executorService.submit(() -> handleNotification(json));
                }
                notificationListener = executorService.submit(() -> listenForNotifications());
                readLock.notifyAll();
                return json;
            } catch (IOException e) {
                throw new ClientConnectionException();
            }
        }
    }
}