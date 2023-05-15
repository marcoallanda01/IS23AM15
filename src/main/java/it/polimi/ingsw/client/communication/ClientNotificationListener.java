package it.polimi.ingsw.client.communication;

import it.polimi.ingsw.communication.responses.GameSetUp;
import it.polimi.ingsw.server.model.Tile;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * this class should be implemented from the client class that handles the notification
 * being it a controller or a directly the view
 * the client MUST use only one instance of this class
 */
public interface ClientNotificationListener {
    void notifyGame(GameSetUp gameSetUp);
    void notifyWinner(String nickname);
    void notifyBoard(Set<Tile> tiles);
    void notifyBookshelf(String nickname, Set<Tile> tiles);
    void notifyPoints(String nickname, int points);
    void notifyTurn(String nickname);
    void notifyPersonalGoalCard(String nickname, String card);
    void notifyCommonCards(Map<String, List<Integer>> cardsToTokens);
    void notifyCommonGoals(Set<String> goals);
    void notifyChatMessage(String nickname, String message, String date);
    void notifyDisconnection(String nickname);
    void notifyGameSaved(String game);
    void notifyPing();
    void notifyReconnection(String nickname);
    void notifyFirstJoinResponse(boolean result);
    void notifyLoadedGamePlayers(Set<String> nicknames);
    void notifyHello(boolean lobbyReady, String firstPlayerId, boolean loadedGame);
    void notifySavedGames(Set<String> games);
    void notifyJoinResponse(boolean result, String error, String id);
    void notifyLoadGameResponse(boolean result, String error);
    void notifyError(String message);
}
