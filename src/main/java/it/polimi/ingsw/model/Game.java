package it.polimi.ingsw.model;

import java.util.List;
import java.util.Optional;

public class Game{
    private final List<Player> players;
    private Player winner;
    private final boolean isFirstGame;
    private final LivingRoomBoard board;
    private Turn currentTurn;
    private final Chat chat;
    private GoalManager goalManager;

    public Game(List<Player> players, boolean isFirstGame) {
        this.players = players;
        int numberOfPlayers = players.size();
        this.isFirstGame = isFirstGame;
        this.board = new LivingRoomBoard(numberOfPlayers);
        this.chat = new Chat(players);
        this.currentTurn = new Turn(players.get(0), board);
        String goalPath = isFirstGame ? "data/goalsFirstGame.json" : "data/goals.json";
        //this.goalManager = new GoalManager(players, goalPath);
    }

    public Integer getPlayerPoints(String nickname) throws PlayerNotFoundException {
        Player player = this.getPlayerFromNickname(nickname);
        return goalManager.getPlayerPoints(player) + (player.isFirstToFinish() ? 1 : 0);
    }

    private Player getPlayerFromNickname(String nickname) throws PlayerNotFoundException {
        try {
            return players.stream().filter(player -> player.getUserName().equals(nickname)).findAny().get();
        } catch (Exception e) {
            throw new PlayerNotFoundException();
        }
    }
}
