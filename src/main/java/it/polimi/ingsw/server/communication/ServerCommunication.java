package it.polimi.ingsw.server.communication;

import it.polimi.ingsw.server.model.Tile;

import java.util.List;
import java.util.Map;

public interface ServerCommunication {

    /**
     * Send one GameSetUp object to every player
     */
    public void gameSetUp();


    // notifications methods

    /**
     * Send notification of the winner to all players
     * @return true if there is a winner and the notification is sent, false otherwise
     */
    public boolean sendWinner();

    /**
     * If in game, function notifies the disconnection of a player to all the others
     * @param playerName player that disconnect
     */
    public void notifyDisconnection(String playerName);
    public void notifyReconnection(String playerName);

    public void notifyChangeBoard(List<Tile> tiles, boolean added);
    public void notifyChangeBookShelf(String playerName, List<Tile> tiles);
    public void updatePlayerPoints(String playerName, int points);
    public void notifyTurn(String playerName);

    // TODO here
    //Non serve, inviati in GameSeUp perché sono unici e non cambiano
    //public void sendCommonGoals(List<String> commonGoals);
    public void sendCommonGoalsCards(Map<String, List<Integer>> cardsAndTokens);
}
