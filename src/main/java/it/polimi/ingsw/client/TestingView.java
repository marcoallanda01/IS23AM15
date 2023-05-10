package it.polimi.ingsw.client;

import it.polimi.ingsw.client.communication.ClientCommunication;
import it.polimi.ingsw.client.communication.ClientNotificationListener;
import it.polimi.ingsw.communication.responses.GameSetUp;
import it.polimi.ingsw.server.model.Tile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
                    clientCommunication.joinNewAsFirst("player1", 1, myId);

            }
        }
    }
    @Override
    public void notifyGame(GameSetUp gameSetUp) {

    }

    @Override
    public void notifyWinner(String nickname) {

    }

    @Override
    public void notifyBoard(Set<Tile> tiles) {

    }

    @Override
    public void notifyBookshelf(String nickname, Set<Tile> tiles) {

    }

    @Override
    public void notifyPoints(String nickname, int points) {

    }

    @Override
    public void notifyTurn(String nickname) {

    }

    @Override
    public void notifyPersonalGoalCard(String nickname, String card) {

    }

    @Override
    public void notifyCommonGoalCards(Map<String, List<Integer>> cardsToTokens) {

    }

    @Override
    public void notifyCommonGoals(Set<String> goals) {

    }

    @Override
    public void notifyChatMessage(String nickname, String message, String date) {

    }

    @Override
    public void notifyDisconnection(String nickname) {

    }

    @Override
    public void notifyGameSaved(String game) {

    }

    @Override
    public void notifyPing() {

    }

    @Override
    public void notifyReconnection(String nickname) {

    }

    @Override
    public void notifyFirstJoinResponse(boolean result) {
        System.out.println("received first join response: " + String.valueOf(result));
    }

    @Override
    public void notifyLoadedGamePlayers(Set<String> nicknames) {

    }

    @Override
    public void notifyHello(boolean lobbyReady, String firstPlayerId, boolean loadedGame) {
        this.myId = firstPlayerId;
        System.out.println("received hello: " + lobbyReady + " " + firstPlayerId + " " + loadedGame);

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

    @Override
    public void notifyError(String message) {

    }
}
