package it.polimi.ingsw.client;

import it.polimi.ingsw.server.model.Tile;

import java.util.List;
import java.util.Map;
import java.util.Set;

public abstract class View {
    private String nickname;
    private List<String> players;
    private Map<String,Set<Tile>> bookShelves;
    private Set<Tile> livingRoomBoard;
    private int numberOfPlayers;
    private boolean easyRules;
    private Map<String,Map<String,String>> chat;
    private List<String> goals;
    private Map<String,Integer> points;
    private String currentTurnPlayer;
    private String personalGoal;
    private Map<String,List<Integer>> commonGoalsCards;
    private Set<String> commonGoals;
    private String game;
    private List<String> savedGames;
    public void render(){}
    public void showError(String error){}
    public void showChat(){}

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public List<String> getPlayers() {
        return players;
    }

    public void setPlayers(List<String> players) {
        this.players = players;
    }

    public Map<String, Set<Tile>> getBookShelves() {
        return bookShelves;
    }

    public void setBookShelves(Map<String, Set<Tile>> bookShelves) {
        this.bookShelves = bookShelves;
    }

    public Set<Tile> getLivingRoomBoard() {
        return livingRoomBoard;
    }

    public void setLivingRoomBoard(Set<Tile> livingRoomBoard) {
        this.livingRoomBoard = livingRoomBoard;
    }

    public int getNumberOfPlayers() {
        return numberOfPlayers;
    }

    public void setNumberOfPlayers(int numberOfPlayers) {
        this.numberOfPlayers = numberOfPlayers;
    }

    public boolean isEasyRules() {
        return easyRules;
    }

    public void setEasyRules(boolean easyRules) {
        this.easyRules = easyRules;
    }

    public Map<String, Map<String, String>> getChat() {
        return chat;
    }

    public void setChat(Map<String, Map<String, String>> chat) {
        this.chat = chat;
    }

    public List<String> getGoals() {
        return goals;
    }

    public void setGoals(List<String> goals) {
        this.goals = goals;
    }

    public Map<String, Integer> getPoints() {
        return points;
    }

    public void setPoints(Map<String, Integer> points) {
        this.points = points;
    }

    public String getCurrentTurnPlayer() {
        return currentTurnPlayer;
    }

    public void setCurrentTurnPlayer(String currentTurnPlayer) {
        this.currentTurnPlayer = currentTurnPlayer;
    }

    public String getPersonalGoal() {
        return personalGoal;
    }

    public void setPersonalGoal(String personalGoal) {
        this.personalGoal = personalGoal;
    }

    public Map<String, List<Integer>> getCommonGoalsCards() {
        return commonGoalsCards;
    }

    public void setCommonGoalsCards(Map<String, List<Integer>> commonGoalsCards) {
        this.commonGoalsCards = commonGoalsCards;
    }

    public Set<String> getCommonGoals() {
        return commonGoals;
    }

    public void setCommonGoals(Set<String> commonGoals) {
        this.commonGoals = commonGoals;
    }

    public String getGame() {
        return game;
    }

    public void setGame(String game) {
        this.game = game;
    }

    public List<String> getSavedGames() {
        return savedGames;
    }

    public void setSavedGames(List<String> savedGames) {
        this.savedGames = savedGames;
    }
}
