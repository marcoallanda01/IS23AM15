package it.polimi.ingsw.client.communication;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.communication.responses.*;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.*;

/**
 * Handles the TCP connection:
 * opens it, closes it, handles notifications
 * receives and sends messages from and to the server
 */
public class TCPClientConnection implements ClientConnection {
    private final ClientNotificationListener clientNotificationListener;
    private final String hostname;
    private final int port;
    private Socket socket;
    private Object readLock, writeLock;
    private ExecutorService executorService, notificationExecutor;
    private Future<Void> notificationListener;

    /**
     * @param clientNotificationListener the clientNotificationListener
     */
    public TCPClientConnection(String hostname, int port, ClientNotificationListener clientNotificationListener) {
        this.clientNotificationListener = clientNotificationListener;
        this.hostname = hostname;
        this.port = port;
    }

    /**
     * Opens the TCP connection, initializes the executorService, and starts listening to notifications.
     *
     * @throws ClientConnectionException if an error occurs while opening the TCP client connection.
     */
    public void openConnection() throws ClientConnectionException {
        Client.getInstance().getLogger().log("Opening TCP client connection...");
        readLock = new Object();
        writeLock = new Object();
        executorService = Executors.newCachedThreadPool();
        notificationExecutor = Executors.newSingleThreadExecutor();
        try {
            Client.getInstance().getLogger().log("Opening socket...");
            socket = new Socket(hostname, port);
            Client.getInstance().getLogger().log("Starting notification handler...");
            notificationListener = executorService.submit(() -> startNotificationHandler());
        } catch (IOException e) {
            Client.getInstance().getLogger().log(e);
            throw new ClientConnectionException("Error while opening TCP client connection.");
        }
        Client.getInstance().getLogger().log("TCP client connection open.");
    }

    /**
     * Closes the TCP connection, closes the executorService, and stops listening to notifications.
     *
     * @throws ClientConnectionException if an error occurs while closing the TCP client connection.
     */
    public void closeConnection() throws ClientConnectionException {
        try {
            executorService.close();
            notificationExecutor.close();
            notificationListener.cancel(Boolean.TRUE);
        } catch (SecurityException e) {
            Client.getInstance().getLogger().log(new ClientConnectionException("Error while closing TCP related executors."));
        }
        try {
            if (socket != null) {
                socket.close();
                Client.getInstance().getLogger().log("TCP client connection closed.");
            }
        } catch (IOException e) {
            Client.getInstance().getLogger().log(e);
            Client.getInstance().getLogger().log(new ClientConnectionException("Error while closing TCP client connection."));
        }
    }

    /**
     * Calls sendStringToServer
     */
    public void sendToServer(String json) {
        sendStringToServer(json);
    }
    /**
     * Sends a message to the server, it locks the writeLock
     *
     * @param json the message to be sent to the server
     */
    private void sendStringToServer(String json) {
        synchronized (writeLock) {
            try {
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                out.println(json);
                Client.getInstance().getLogger().log("Sent to server: " + json);
            } catch (Exception e) {
                Client.getInstance().getLogger().log(e);
            }
        }
    }

    /**
     * Listens for messages and dispatches them, unless interrupted
     * this process is done synchronously, so that messages received from the server hare handled in order of arrival
     */
    private Void startNotificationHandler() {
        synchronized (readLock) {
            try {
                Scanner in = new Scanner(socket.getInputStream());
                Client.getInstance().onConnectionReady();
                while (!Thread.currentThread().isInterrupted()) {
                    String json = in.nextLine();
                    Client.getInstance().getLogger().log("Received from server: " + json);
                    dispatchNotification(json);
                }
                return null;
            } catch (Exception e) {
                Client.getInstance().getLogger().log(e);
                return null;
            }
        }
    }

    /**
     * Handles the received message by calling the appropriate view method,
     * notifications related to the game are called synchronously and in order (queued in a single thread pool executor)
     * ping is called asynchronously, to prevent notification handler from causing excessive ping delay
     *
     * @param json the notification string received from the server
     * @return true if the response has been handled correctly by the view, false otherwise
     */
    private Boolean dispatchNotification(String json) {
        if (BoardUpdate.fromJson(json).isPresent()) {
            BoardUpdate boardUpdate = BoardUpdate.fromJson(json).get();
            notificationExecutor.submit(() -> clientNotificationListener.notifyBoard(boardUpdate.tiles));
        } else if (TilesPicked.fromJson(json).isPresent()) {
            TilesPicked tilesPicked = TilesPicked.fromJson(json).get();
            notificationExecutor.submit(() -> clientNotificationListener.notifyPickedTiles(tilesPicked.player, tilesPicked.tiles));
        } else if (BookShelfUpdate.fromJson(json).isPresent()) {
            BookShelfUpdate bookShelfUpdate = BookShelfUpdate.fromJson(json).get();
            notificationExecutor.submit(() -> clientNotificationListener.notifyBookshelf(bookShelfUpdate.player, bookShelfUpdate.tiles));
        } else if (ChatMessage.fromJson(json).isPresent()) {
            ChatMessage chatMessage = ChatMessage.fromJson(json).get();
            notificationExecutor.submit(() -> clientNotificationListener.notifyChatMessage(chatMessage.sender, chatMessage.message, chatMessage.date));
        } else if (CommonCards.fromJson(json).isPresent()) {
            CommonCards commonCards = CommonCards.fromJson(json).get();
            notificationExecutor.submit(() -> clientNotificationListener.notifyCommonCards(commonCards.cardsAndTokens));
        } else if (CommonGoals.fromJson(json).isPresent()) {
            CommonGoals commonGoals = CommonGoals.fromJson(json).get();
            notificationExecutor.submit(() -> clientNotificationListener.notifyCommonGoals(commonGoals.goals));
        } else if (Disconnection.fromJson(json).isPresent()) {
            Disconnection disconnection = Disconnection.fromJson(json).get();
            notificationExecutor.submit(() -> clientNotificationListener.notifyDisconnection(disconnection.player));
        } else if (ErrorMessage.fromJson(json).isPresent()) {
            ErrorMessage errorMessage = ErrorMessage.fromJson(json).get();
            notificationExecutor.submit(() -> clientNotificationListener.notifyError(errorMessage.message));
        } else if (FirstJoinResponse.fromJson(json).isPresent()) {
            FirstJoinResponse firstJoinResponse = FirstJoinResponse.fromJson(json).get();
            notificationExecutor.submit(() -> clientNotificationListener.notifyFirstJoinResponse(firstJoinResponse.result));
        } else if (GameSaved.fromJson(json).isPresent()) {
            GameSaved gameSaved = GameSaved.fromJson(json).get();
            notificationExecutor.submit(() -> clientNotificationListener.notifyGameSaved(gameSaved.game));
        } else if (GameSetUp.fromJson(json).isPresent()) {
            GameSetUp gameSetUp = GameSetUp.fromJson(json).get();
            notificationExecutor.submit(() -> clientNotificationListener.notifyGame(gameSetUp));
        } else if (Hello.fromJson(json).isPresent()) {
            Hello hello = Hello.fromJson(json).get();
            notificationExecutor.submit(() -> clientNotificationListener.notifyHello(hello.lobbyReady, hello.firstPlayerId, hello.loadedGame));
        } else if (JoinResponse.fromJson(json).isPresent()) {
            JoinResponse joinResponse = JoinResponse.fromJson(json).get();
            notificationExecutor.submit(() -> clientNotificationListener.notifyJoinResponse(joinResponse.result, joinResponse.error, joinResponse.id));
        } else if (LoadedGamePlayers.fromJson(json).isPresent()) {
            LoadedGamePlayers loadedGamePlayers = LoadedGamePlayers.fromJson(json).get();
            notificationExecutor.submit(() -> clientNotificationListener.notifyLoadedGamePlayers(loadedGamePlayers.names));
        } else if (LoadGameResponse.fromJson(json).isPresent()) {
            LoadGameResponse loadGameResponse = LoadGameResponse.fromJson(json).get();
            notificationExecutor.submit(() -> clientNotificationListener.notifyLoadGameResponse(loadGameResponse.result, loadGameResponse.error));
        } else if (ChatMessage.fromJson(json).isPresent()) {
            ChatMessage chatMessage = ChatMessage.fromJson(json).get();
            notificationExecutor.submit(() -> clientNotificationListener.notifyChatMessage(chatMessage.sender, chatMessage.message, chatMessage.date));
        } else if (Ping.fromJson(json).isPresent()) {
            Ping ping = Ping.fromJson(json).get();
            executorService.submit(() -> clientNotificationListener.notifyPing());
        } else if (PlayerPoints.fromJson(json).isPresent()) {
            PlayerPoints playerPoints = PlayerPoints.fromJson(json).get();
            notificationExecutor.submit(() -> clientNotificationListener.notifyPoints(playerPoints.player, playerPoints.points));
        } else if (Reconnected.fromJson(json).isPresent()) {
            Reconnected reconnected = Reconnected.fromJson(json).get();
            notificationExecutor.submit(() -> clientNotificationListener.notifyReconnection(reconnected.player));
        } else if (SavedGames.fromJson(json).isPresent()) {
            SavedGames savedGames = SavedGames.fromJson(json).get();
            notificationExecutor.submit(() -> clientNotificationListener.notifySavedGames(savedGames.names));
        } else if (TilesPicked.fromJson(json).isPresent()) {
            TilesPicked tilesPicked = TilesPicked.fromJson(json).get();
            notificationExecutor.submit(() -> clientNotificationListener.notifyPickedTiles(tilesPicked.player, tilesPicked.tiles));
        }  else if (TurnNotify.fromJson(json).isPresent()) {
            TurnNotify turnNotify = TurnNotify.fromJson(json).get();
            notificationExecutor.submit(() -> clientNotificationListener.notifyTurn(turnNotify.player));
        } else if (Winner.fromJson(json).isPresent()) {
            Winner winner = Winner.fromJson(json).get();
            notificationExecutor.submit(() -> clientNotificationListener.notifyWinner(winner.player));
        } else {
            Client.getInstance().getLogger().log("Notification: " + json + " not recognized");
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }
}