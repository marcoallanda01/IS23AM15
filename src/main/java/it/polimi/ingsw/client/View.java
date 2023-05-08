package it.polimi.ingsw.client;

import it.polimi.ingsw.communication.responses.GameSetUp;
import it.polimi.ingsw.client.communication.ClientCommunication;
import it.polimi.ingsw.server.model.Tile;

import java.util.List;
import java.util.Map;
import java.util.Set;

public abstract class View {
    protected ClientCommunication clientCommunication;
    public View(ClientCommunication clientCommunication) {
        this.clientCommunication = clientCommunication;
    }
    public abstract void showGame(GameSetUp gameSetUp);
    public abstract void showWinner(String nickname);
    public abstract void showPlayers(Set<String> nicknames);
    public abstract void showBoard(Set<Tile> tiles);
    public abstract void showBookshelf(String nickname, Set<Tile> tiles);
    public abstract void showPoints(String nickname, int points);
    public abstract void showTurn(String nickname);
    public abstract void showPersonalGoalCard(String nickname, String card);
    public abstract void showCommonGoalCards(Map<String, List<Integer>> cardsToTokens);
    public abstract void showCommonGoals(Set<String> goals);
}
