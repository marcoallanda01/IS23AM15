package it.polimi.ingsw.client;

import it.polimi.ingsw.client.communication.ClientCommunication;
import it.polimi.ingsw.client.communication.ClientNotificationListener;
import it.polimi.ingsw.communication.responses.GameSetUp;
import it.polimi.ingsw.server.model.Tile;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ClientController implements ClientNotificationListener {
    private final View view;
    private final ClientCommunication communication;

    public ClientController() {
        view = Client.getInstance().getView();
        communication = Client.getInstance().getClientCommunication();
    }

    @Override
    public void notifyGame(GameSetUp gameSetUp) {

    }

    @Override
    public void notifyWinner(String nickname) {
        Client.getInstance().setClientState(ClientStates.END_GAME);
        view.setWinner(nickname);

        view.render();
    }

    @Override
    public void notifyBoard(Set<Tile> tiles) {
        view.setLivingRoomBoard(tiles);

        view.render();
    }

    @Override
    public void notifyBookshelf(String nickname, Set<Tile> tiles) {
        Map<String, Set<Tile>> bookShelves = view.getBookShelves();
        bookShelves.put(nickname, tiles);
        view.setBookShelves(bookShelves);

        view.render();
    }

    @Override
    public void notifyPoints(String nickname, int points) {
        Map<String, Integer> pointsMap = view.getPoints();
        pointsMap.put(nickname, points);
        view.setPoints(pointsMap);

        view.render();
    }

    @Override
    public void notifyTurn(String nickname) {
        view.setCurrentTurnPlayer(nickname);

        view.render();
    }

    @Override
    public void notifyPersonalGoalCard(String nickname, String card) {
        view.setPersonalGoal(card);

        view.render();
    }

    @Override
    public void notifyCommonGoalCards(Map<String, List<Integer>> cardsToTokens) {
        view.setCommonGoalsCards(cardsToTokens);

        view.render();
    }

    @Override
    public void notifyCommonGoals(Set<String> goals) {
        view.setCommonGoals(goals);

        view.render();
    }

    @Override
    public void notifyChatMessage(String nickname, String message, String date) {
        Map<String, Map<String, String>> chat = view.getChat();
        chat.put(date, Map.of("nickname", nickname, "message", message));
        view.setChat(chat);

        view.render();
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
        List<String> players = new ArrayList<>(nicknames);
        view.setPlayers(players);

        view.render();
    }

    @Override
    public void notifyHello(boolean lobbyReady, String firstPlayerId, boolean loadedGame) {
        if (!lobbyReady) {
            if (!firstPlayerId.equals("NoFirst")) {
                Client.getInstance().setId(firstPlayerId);
                Client.getInstance().setClientState(ClientStates.CREATE_LOBBY);
                view.render();
            }
        } else {
            if (loadedGame) {
                communication.join(Client.getInstance().getView().getNickname());
            }
        }
    }

    @Override
    public void notifySavedGames(Set<String> games) {
        List<String> savedGames = new ArrayList<>(games);
        view.setSavedGames(savedGames);

        view.render();
    }

    @Override
    public void notifyJoinResponse(boolean result, String error, String id) {

    }

    @Override
    public void notifyLoadGameResponse(boolean result, String error) {

    }

    @Override
    public void notifyError(String message) {
        view.showError(message);
    }

    public void login(String nickname) {
        view.setNickname(nickname);
        Client.getInstance().getClientCommunication().hello();
    }

    public void logout() {

    }

    public void createLobby(boolean loadGame) {

    }

    public void createGame(int numOfPlayers, boolean easyRules) {

    }

    public void getSavedGames() {
        Client.getInstance().getClientCommunication().getSavedGames();
    }
    public static void joinLobby() {

    }

    public void startGame() {

    }

    public void pickTiles(List<List<Integer>> coordinates) {

    }

    public void putTiles(Integer column) {

    }

    public void sendChatMessage(String message) {

    }

    public void sendChatMessage(String receiver, String message) {

    }
}
