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
     * If in game, function notifies the disconnection of a player to all the others
     * @param playerName player that disconnect
     */
    public void notifyDisconnection(String playerName);
    /**
     * Notify to all clients that a player reconnected
     * @param playerName name of the player who reconnected
     */
    public void notifyReconnection(String playerName);
    /**
     * Send a message to all players
     * @param sender sender's name
     * @param date date of message creation
     * @param message actual message to be sent
     */
    public void notifyMessage(String sender, String date, String message);
    /**
     * Send a message to all players
     * @param sender sender's name
     * @param date date of message creation
     * @param message actual message to be sent
     * @param receiver receiver's name
     */
    public void notifyMessage(String sender, String date, String message, String receiver);
    /**
     * Notify change in the board to all clients in game
     * @param tiles board
     */
    public void notifyChangeBoard(List<Tile> tiles);
    /**
     * Notify to all clients change in player's bookshelf
     * @param playerName player's name
     * @param tiles bookshelf
     */
    public void notifyChangeBookShelf(String playerName, List<Tile> tiles);
    /**
     * Notify change in point of a player to all clients
     * @param playerName player's name
     * @param points new points
     */
    public void updatePlayerPoints(String playerName, int points);
    /**
     * Notify to all player whom turn is
     * @param playerName current player
     */
    public void notifyTurn(String playerName);

    //Non serve, inviati in GameSeUp perché sono unici e non cambiano
    //public void sendCommonGoals(List<String> commonGoals);
    /**
     * Notify to all clients a change in common goals cards and tokens
     * @param cardsAndTokens cards with associated tokens
     */
    public void sendCommonGoalsCards(Map<String, List<Integer>> cardsAndTokens);

    /**
     * Send notification of the winner to all players
     * @param playerName name of the winner
     */
    public void notifyWinner(String playerName);

    /**
     * Handle the disconnection of the last player terminating the game
     */
    public void handleLastPlayerDisconnection();
}
