package it.polimi.ingsw.comunication;

import it.polimi.ingsw.server.communication.GameSetUp;
import it.polimi.ingsw.server.model.Tile;

import java.util.List;

public class TCPClientController implements ClientController{
    public TCPClientController(TCPServer tcpServer, NotificationHandler notificationHandler) {
    }

    @Override
    public void gameSetUp(GameSetUp gameSetUp) {

    }

    @Override
    public void notifyWinner(String nickname) {

    }

    @Override
    public void notifyChangePlayers(List<String> nicknames) {

    }

    @Override
    public void notifyChangeBoard(List<Tile> tiles) {

    }

    @Override
    public void notifyChangeBookShelf(String nickname, List<Tile> tiles) {

    }

    @Override
    public void updatePlayerPoints(String nickname, int points) {

    }

    @Override
    public void notifyTurn(String nickname) {

    }
}
