package it.polimi.ingsw.client;

import it.polimi.ingsw.client.communication.ClientNotificationListener;
import it.polimi.ingsw.communication.responses.GameSetUp;
import it.polimi.ingsw.server.model.Tile;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class ClientController implements ClientNotificationListener {
    private final View view;

    public ClientController() {
        view = Client.getInstance().getView();
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

    @Override
    public void notifyError(String message) {

    }

    public void login(String nickname) {
        view.setNickname(nickname);
    }

    public static void logout() {

    }
    public static void createLobby(int numOfPlayers) {

    }
    public static void createLobby(int numOfPlayers, boolean easyRules) {

    }
    public static void joinLobby() {

    }
    public static void startGame() {

    }
    public static void pickTiles(List<List<Integer>> coordinates) {

    }
    public static void putTiles(Integer column) {

    }
    public static void sendChatMessage(String message) {

    }
    public static void sendChatMessage(String receiver, String message) {

    }
}
