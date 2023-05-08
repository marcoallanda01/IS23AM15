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
    public abstract void notifyGame(GameSetUp gameSetUp);
    public abstract void notifyWinner(String nickname);
    public abstract void notifyPlayers(Set<String> nicknames);
    public abstract void notifyBoard(Set<Tile> tiles);
    public abstract void notifyBookshelf(String nickname, Set<Tile> tiles);
    public abstract void notifyPoints(String nickname, int points);
    public abstract void notifyTurn(String nickname);
    public abstract void notifyPersonalGoalCard(String nickname, String card);
    public abstract void notifyCommonGoalCards(Map<String, List<Integer>> cardsToTokens);
    public abstract void notifyCommonGoals(Set<String> goals);
}
