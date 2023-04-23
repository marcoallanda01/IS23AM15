package it.polimi.ingsw.comunication;

import it.polimi.ingsw.server.communication.GameSetUp;
import it.polimi.ingsw.server.model.Tile;

import java.rmi.Remote;
import java.util.List;

public interface RMIClient extends Remote {
    // send one GameSetUp object to every player
    public void gameSetUp(GameSetUp gameSetUp);
    // notifications methods
    public void notifyWinner(String nickname);
    public void notifyChangePlayers(List<String> nicknames);
    public void notifyChangeBoard(List<Tile> tiles);
    public void notifyChangeBookShelf(String nickname, List<Tile> tiles);
    public void updatePlayerPoints(String nickname, int points);
    public void notifyTurn(String nickname);
}
