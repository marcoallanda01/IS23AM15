package it.polimi.ingsw.model;

import org.jetbrains.annotations.NotNull;

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
    public Map<String, Stack<Token>> getCommonGoalCardsToTokens() {
        return goalManager.getCommonCardsToTokens().entrySet().stream().collect(Collectors.toMap(entry -> entry.getKey().getName(), Map.Entry::getValue));
    }
    public Set<String> getUnfulfilledCommonGoalCards(String nickname) throws PlayerNotFoundException {
        Player player = this.getPlayerFromNickname(nickname);
        return goalManager.getFulfilledCommonCards(player).stream().map(Card::getName).collect(Collectors.toSet());
    }
    public Set<String> getFulfilledCommonGoalCards(String nickname) throws PlayerNotFoundException {
        Player player = this.getPlayerFromNickname(nickname);
        return goalManager.getFulfilledCommonCards(player).stream().map(Card::getName).collect(Collectors.toSet());
    }
    public String getPersonalGoalCard(String nickname) throws PlayerNotFoundException {
        Player player = this.getPlayerFromNickname(nickname);
        return goalManager.getPersonalCard(player).getName();
    }
    public Set<Token> getTokens(String nickname) throws PlayerNotFoundException{
        Player player = this.getPlayerFromNickname(nickname);
        return goalManager.getTokens(player);
    }
    public Set<String> getEndGameGoals() {
        return goalManager.getEndGameGoals().stream().map(Pattern::getName).collect(Collectors.toSet());
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

    public void sendMessage(String sender, String receiver, String message) throws PlayerNotFoundException{
        Message m = new Message(this.getPlayerFromNickname(sender), this.getPlayerFromNickname(receiver), message);
        try {
            this.chat.addMessage(m);
        } catch (PlayerNotFoundException e) {
            e.printStackTrace();
        }
    }
    public void sendMessage(String sender, String message) throws PlayerNotFoundException{
        Message m = new Message(this.getPlayerFromNickname(sender), message);
        try {
            this.chat.addMessage(m);
        } catch (PlayerNotFoundException e) {
            e.printStackTrace();
        }
    }
}
