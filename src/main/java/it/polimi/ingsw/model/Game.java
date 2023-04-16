package it.polimi.ingsw.model;

import java.util.*;
import java.util.stream.Collectors;

public class Game{
    private final List<Player> players;
    private Player winner;
    private final boolean isFirstGame;
    private final LivingRoomBoard board;
    private Turn currentTurn;
    private final Chat chat;
    private GoalManager goalManager;

    public Game(List<String> players, boolean isFirstGame) {
        this.players = players.stream().map(Player::new).collect(Collectors.toList());
        int numberOfPlayers = players.size();
        this.isFirstGame = isFirstGame;
        this.board = new LivingRoomBoard(numberOfPlayers);
        this.chat = new Chat(this.players);
        this.currentTurn = new Turn(this.players.get((int) Math.floor(Math.random() *(this.players.size() + 1))), board);
        String goalPath = isFirstGame ? "data/goalsFirstGame.json" : "data/goals.json";
        //this.goalManager = new GoalManager(players, goalPath);
    }

    public Integer getPoints(String nickname) throws PlayerNotFoundException {
        Player player = this.getPlayerFromNickname(nickname);
        return goalManager.getPoints(player) + (player.isFirstToFinish() ? 1 : 0);
    }
    // IMPORTANT: cards with the same name are lost!, maybe resort back to having unique names!
    public Map<String, Stack<Token>> getCommonGoalCardsToTokens() {
        return goalManager.getCommonCardsToTokens().entrySet().stream().collect(Collectors.toMap(entry -> entry.getKey().getName(), Map.Entry::getValue));
    }
    // returning List<String> because there can be more than 1 card with the same name and pattern
    // IMPORTANT: cards with the same name can cause confusion on the view side, maybe resort back to set!
    public List<String> getUnfulfilledCommonGoalCards(String nickname) throws PlayerNotFoundException {
        Player player = this.getPlayerFromNickname(nickname);
        return goalManager.getFulfilledCommonCards(player).stream().map(Card::getName).collect(Collectors.toList());
    }
    public List<String> getFulfilledCommonGoalCards(String nickname) throws PlayerNotFoundException {
        Player player = this.getPlayerFromNickname(nickname);
        return goalManager.getFulfilledCommonCards(player).stream().map(Card::getName).collect(Collectors.toList());
    }
    public String getPersonalGoalCard(String nickname) throws PlayerNotFoundException {
        Player player = this.getPlayerFromNickname(nickname);
        return goalManager.getPersonalCard(player).getName();
    }
    public Set<Token> getTokens(String nickname) throws PlayerNotFoundException{
        Player player = this.getPlayerFromNickname(nickname);
        return goalManager.getTokens(player);
    }
    public List<String> getEndGameGoals() {
        return goalManager.getEndGameGoals().stream().map(Pattern::getName).collect(Collectors.toList());
    }
    private Player getPlayerFromNickname(String nickname) throws PlayerNotFoundException {
        try {
            return players.stream().filter(player -> player.getUserName().equals(nickname)).findAny().get();
        } catch (Exception e) {
            throw new PlayerNotFoundException();
        }
    }

    private boolean nextTurn(){
        if(this.currentTurn.getState() instanceof  EndState){ //TODO: è giusto?
            this.currentTurn = new Turn(
                    this.players.get(
                            this.players.indexOf(
                                    this.currentTurn.getCurrentPlayer()
                            ) + 1
                    ), this.board
            );
            return true;
        }
        return false;
    }

    public Set<String> getPlayers() {
        return players.stream().map(Player::getUserName).collect(Collectors.toSet());
    }

}
