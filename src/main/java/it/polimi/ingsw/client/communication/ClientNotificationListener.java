package it.polimi.ingsw.client.communication;

import it.polimi.ingsw.communication.responses.GameSetUp;
import it.polimi.ingsw.server.model.Tile;

import java.time.LocalDateTime;
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
    void notifyBoard(Set<Tile> tiles, boolean added);
    void notifyBookshelf(String nickname, Set<Tile> tiles);
    void notifyPoints(String nickname, int points);
    void notifyTurn(String nickname);
    void notifyPersonalGoalCard(String nickname, String card);
    void notifyCommonGoalCards(Map<String, List<Integer>> cardsToTokens);
    void notifyCommonGoals(Set<String> goals);
    void notifyChatMessage(String nickname, String message, String date);
    void notifyDisconnection(String nickname);
    void notifyGameSaved(String game);
    void notifyPing(String id);
    void notifyReconnection(String nickname);

}
