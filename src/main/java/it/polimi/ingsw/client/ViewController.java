package it.polimi.ingsw.client;

import it.polimi.ingsw.server.communication.GameSetUp;
import it.polimi.ingsw.server.model.Tile;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class ViewController {
   // private ServerController serverController;
    private View view;

    private String lastShowed;
    //public ViewController(View view, ServerController serverController) {
    //    this.serverController = serverController;
    //}

    public void showGame(GameSetUp gameSetUp) {
        this.lastShowed = "game is: " + gameSetUp;
    }
    public void showWinner(String nickname) {
        this.lastShowed = "winner is: " + nickname;
    }
    public void showPlayers(List<String> nicknames) {
        this.lastShowed = "players are: " + nicknames;
    }

    public void showBoard(List<Tile> tiles) {
        this.lastShowed = "board tiles are: " + tiles;
    }

    public void showBookshelf(String nickname, List<Tile> tiles) {
        this.lastShowed = nickname + "'s bookshelf tiles are: " + tiles;
    }

    public void showPoints(String nickname, int points) {
        this.lastShowed = nickname + "'s points are: " + points;
    }

    public void showTurn(String nickname) {
        this.lastShowed = nickname + "'s turn started";
    }

    public void showPersonalGoalCard(String nickname, String card) {
        this.lastShowed = nickname + "'s card has changed: " + card;
    }

    public void showCommonGoalCards(Map<String, List<Integer>> cardsToTokens) {
        this.lastShowed = "common goal cards have changed: " + cardsToTokens;
    }

    public void showCommonGoals(Set<String> goals) {
        this.lastShowed = "common goals have changed: " + goals;
    }

    public String getLastShowed() {
        return this.lastShowed;
    }
}
