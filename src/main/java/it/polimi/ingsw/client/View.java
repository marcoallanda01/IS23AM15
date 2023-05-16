package it.polimi.ingsw.client;

import it.polimi.ingsw.server.model.Tile;
import it.polimi.ingsw.server.model.TileType;

import java.util.*;

public abstract class View {
    private String nickname;
    private List<String> players = new ArrayList<>();
    private List<String> lobbyPlayers = new ArrayList<>();
    private Map<String,Set<Tile>> bookShelves = new HashMap<>();
    private Set<Tile> livingRoomBoard = new HashSet<>();
    private List<TileType> pickedTiles = new ArrayList<>();
    private int numberOfPlayers;
    private boolean easyRules;
    private Map<String,Map<String,String>> chat = new HashMap<>();
    private Map<String,Integer> points = new HashMap<>();
    private String currentTurnPlayer;
    private String personalGoal;
    private Map<String,List<Integer>> commonCards = new HashMap<>();
    private List<String> commonGoals = new ArrayList<>();
    private String game;
    private List<String> savedGames = new ArrayList<>();
    private String winner;
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

    public List<String> getLobbyPlayers() {
        return lobbyPlayers;
    }

    public void setLobbyPlayers(List<String> lobbyPlayers) {
        this.lobbyPlayers = lobbyPlayers;
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

    public List<TileType> getPickedTiles() {
        return pickedTiles;
    }

    public void setPickedTiles(List<TileType> pickedTiles) {
        this.pickedTiles = pickedTiles;
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

    public Map<String, List<Integer>> getCommonCards() {
        return commonCards;
    }

    public void setCommonCards(Map<String, List<Integer>> commonCards) {
        this.commonCards = commonCards;
    }

    public List<String> getCommonGoals() {
        return commonGoals;
    }

    public void setCommonGoals(List<String> commonGoals) {
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

    public String getWinner() {
        return winner;
    }

    public void setWinner(String winner) {
        this.winner = winner;
    }
}
