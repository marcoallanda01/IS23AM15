package it.polimi.ingsw.client;

import it.polimi.ingsw.server.communication.ClientCommunication;
import it.polimi.ingsw.communication.responses.GameSetUp;
import it.polimi.ingsw.server.model.Tile;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class CLI extends View {

    public CLI(ClientCommunication clientCommunication) {
        super(clientCommunication);
    }

    @Override
    public void showGame(GameSetUp gameSetUp) {

    }

    @Override
    public void showWinner(String nickname) {

    }

    @Override
    public void showPlayers(Set<String> nicknames) {

    }

    @Override
    public void showBoard(Set<Tile> tiles) {

    }

    @Override
    public void showBookshelf(String nickname, Set<Tile> tiles) {

    }

    @Override
    public void showPoints(String nickname, int points) {

    }

    @Override
    public void showTurn(String nickname) {

    }

    @Override
    public void showPersonalGoalCard(String nickname, String card) {

    }

    @Override
    public void showCommonGoalCards(Map<String, List<Integer>> cardsToTokens) {

    }

    @Override
    public void showCommonGoals(Set<String> goals) {

    }
}
