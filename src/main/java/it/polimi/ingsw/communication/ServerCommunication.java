package it.polimi.ingsw.communication;

import it.polimi.ingsw.model.Tile;

import java.util.List;

public interface ServerCommunication {
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


}
