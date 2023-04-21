package it.polimi.ingsw.server.communication;

import it.polimi.ingsw.server.model.Tile;

import java.util.List;

public interface ServerCommunication {

    // send one GameSetUp object to every player
    public void gameSetUp();


    // notifications methods

    /**
     * Send notification of the winner to all players
     * @return true if there is a winner and the notification is sent, false otherwise
     */
    public boolean sendWinner();

    /**
     * If in game, function notifies the disconnection of a player to all the others
     * @param playerId player that disconnecter
     */
    public void notifyDisconnection(String playerId);

    public void notifyChangeBoard(List<Tile> tiles);
    public void notifyChangeBookShelf(String playerName, List<Tile> tiles);
    public void updatePlayerPoints(String playerName, int points);

    public void notifyTurn(String playerName);

}
