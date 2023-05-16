package it.polimi.ingsw.client;

import it.polimi.ingsw.client.communication.ClientCommunication;
import it.polimi.ingsw.client.communication.ClientNotificationListener;
import it.polimi.ingsw.communication.responses.GameSetUp;
import it.polimi.ingsw.server.model.Tile;
import it.polimi.ingsw.server.model.TileType;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class TestingView implements ClientNotificationListener {
    private String myId = null;
    ClientCommunication clientCommunication;
    public TestingView() {
    }
    public void setClientCommunication(ClientCommunication clientCommunication) {
        this.clientCommunication = clientCommunication;
    }
    public void start() {
        while (true) {
            System.out.println("Waiting command...");
            // Enter data using BufferReader
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

            // Reading data using readLine
            String command = null;
            try {
                command = reader.readLine();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            // Printing the read line
            System.out.println("Just read: " + command);

            switch (command) {
                case "hello":
                    clientCommunication.hello();
                    break;
                case "join new as first":
                    clientCommunication.joinNewAsFirst("player1", 2, myId);
                    break;
                case "jnaf 3":
                    clientCommunication.joinNewAsFirst("player1", 3, myId);
                    break;
                case "join new as first easy rules":
                    clientCommunication.joinNewAsFirst("player1", 2, myId, true);
                    break;
                case "join 1":
                    clientCommunication.join("player1");
                    break;
                case "join 2":
                    clientCommunication.join("player2");
                    break;
                case "join 3":
                    clientCommunication.join("player3");
                    break;
                case "join 4":
                    clientCommunication.join("player4");
                    break;
                case "get saved games":
                    clientCommunication.getSavedGames();
                    break;
                case "load game":
                    clientCommunication.loadGame("0", myId);
                    break;
                case "get loaded game players":
                    clientCommunication.getLoadedGamePlayers();
                    break;
                case "join loaded as first":
                    clientCommunication.joinLoadedAsFirst("player1", myId);
                    break;
                case "disconnect":
                    clientCommunication.disconnect(myId);
                    break;
                case "reconnect":
                    clientCommunication.reconnect(myId);
                    break;
                case "pick tiles":
                    clientCommunication.pickTiles(myId, new HashSet<>(Set.of(new Tile(1, 1, TileType.CAT))));
                    break;
                case "put tiles":
                    clientCommunication.putTiles(myId, new ArrayList<>(List.of(TileType.BOOK)), 1);
                    break;
                case "send message":
                    clientCommunication.sendMessage(myId, "Hello, how are you?", "player2");
                    break;
                case "pong":
                    clientCommunication.pong(myId);
                    break;
                default:
                    System.out.println("Invalid command!");
            }
        }
    }
    @Override
    public void notifyGame(GameSetUp gameSetUp) {
        System.out.println("Game setup received: " + gameSetUp);
    }

    @Override
    public void notifyWinner(String nickname) {
        System.out.println("Winner notified: " + nickname);
    }

    @Override
    public void notifyBoard(Set<Tile> tiles) {
        System.out.println("Board tiles notified: " + tiles);
    }

    @Override
    public void notifyBookshelf(String nickname, Set<Tile> tiles) {
        System.out.println("Bookshelf tiles notified for " + nickname + ": " + tiles);
    }

    @Override
    public void notifyPoints(String nickname, int points) {
        System.out.println("Points notified for " + nickname + ": " + points);
    }

    @Override
    public void notifyTurn(String nickname) {
        System.out.println("Turn notified for " + nickname);
    }

    @Override
    public void notifyPersonalGoalCard(String nickname, String card) {
        System.out.println("Personal goal card notified for " + nickname + ": " + card);
    }

    @Override
    public void notifyCommonCards(Map<String, List<Integer>> cardsToTokens) {
        System.out.println("Common goal cards notified: " + cardsToTokens);
    }

    @Override
    public void notifyCommonGoals(Set<String> goals) {
        System.out.println("Common goals notified: " + goals);
    }

    @Override
    public void notifyChatMessage(String nickname, String message, String date) {
        System.out.println("Chat message notified from " + nickname + " at " + date + ": " + message);
    }

    @Override
    public void notifyDisconnection(String nickname) {
        System.out.println("Player disconnected: " + nickname);
    }

    @Override
    public void notifyGameSaved(String game) {
        System.out.println("Game saved: " + game);
    }

    @Override
    public void notifyPing() {
        System.out.println("Ping received");
        clientCommunication.pong(myId);
    }

    @Override
    public void notifyReconnection(String nickname) {
        System.out.println("Player reconnected: " + nickname);
    }

    @Override
    public void notifyFirstJoinResponse(boolean result) {
        System.out.println("First join response received: " + result);
    }

    @Override
    public void notifyLoadedGamePlayers(Set<String> nicknames) {
        System.out.println("Loaded game players notified: " + nicknames);
    }

    @Override
    public void notifyHello(boolean lobbyReady, String firstPlayerId, boolean loadedGame) {
        this.myId = firstPlayerId;
        System.out.println("Hello received: " + lobbyReady + ", First player ID: " + firstPlayerId + ", Loaded game: " + loadedGame);
    }

    @Override
    public void notifySavedGames(Set<String> games) {
        System.out.println("Saved games notified: " + games);
    }

    @Override
    public void notifyJoinResponse(boolean result, String error, String id) {
        System.out.println("Join response received: " + result + ", Error: " + error + ", ID: " + id);
    }

    @Override
    public void notifyLoadGameResponse(boolean result, String error) {
        System.out.println("Load game response received: " + result + ", Error: " + error);
    }

    @Override
    public void notifyError(String message) {
        System.out.println("Error received: " + message);
    }
}
