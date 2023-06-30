package it.polimi.ingsw.client;

import it.polimi.ingsw.server.model.Tile;
import it.polimi.ingsw.server.model.TileType;

import java.util.*;

/**
 * Abstract class that represents a view
 */
public abstract class View {
    private List<String> players = new ArrayList<>();
    private List<String> lobbyPlayers = new ArrayList<>();
    private Map<String, Set<Tile>> bookShelves = new HashMap<>();
    private Set<Tile> livingRoomBoard = new HashSet<>();
    private List<TileType> pickedTiles = new ArrayList<>();
    private Map<String, Map<String, String>> chat = new HashMap<>();
    private Map<String, Integer> points = new HashMap<>();
    private String currentTurnPlayer;
    private String personalGoal;
    private Map<String, List<Integer>> commonCards = new HashMap<>();
    private List<String> commonGoals = new ArrayList<>();
    private List<String> savedGames = new ArrayList<>();
    private String winner;
    protected Map<String, ClientGoalDetail> goalsToDetails;
    private boolean hasPutTiles = false;

    /**
     * Renders
     */
    public abstract void render();

    /**
     * Shows an error
     * @param error the error to show
     */
    public abstract void showError(String error);

    /**
     * Shows the chat
     */
    public abstract void showChat();

    /**
     * Shows help in terms of available actions
     */
    public abstract void showHelp();

    /**
     * Shows goal details
     */
    public abstract void showGoals();

    /**
     * Shows the chat notification
     */
    public abstract void showChatNotification();

    /**
     * stops the view showing a message
     * @param message the last message to show
     */
    public abstract void stop(String message);

    /**
     * Gets the players
     * @return the players
     */
    public List<String> getPlayers() {
        return new ArrayList<>(players);
    }

    /**
     * Sets the players
     * @param players the players
     */
    public void setPlayers(List<String> players) {
        this.players = players;
    }

    /**
     * Gets the lobby players
     * @return the lobby players
     */
    public List<String> getLobbyPlayers() {
        return lobbyPlayers;
    }

    /**
     * Sets the lobby players
     * @param lobbyPlayers the lobby players
     */
    public void setLobbyPlayers(List<String> lobbyPlayers) {
        this.lobbyPlayers = lobbyPlayers;
    }

    /**
     * Gets the bookshelves
     * @return the bookshelves
     */
    public Map<String, Set<Tile>> getBookShelves() {
        return bookShelves;
    }

    /**
     * Sets the bookshelves
     * @param bookShelves the bookshelves
     */
    public void setBookShelves(Map<String, Set<Tile>> bookShelves) {
        this.bookShelves = bookShelves;
    }

    /**
     * Gets the living room board
     * @return the living room board
     */
    public Set<Tile> getLivingRoomBoard() {
        return livingRoomBoard;
    }

    /**
     * Sets the living room board
     * @param livingRoomBoard the living room board
     */
    public void setLivingRoomBoard(Set<Tile> livingRoomBoard) {
        this.livingRoomBoard = livingRoomBoard;
    }

    /**
     * Gets the picked tiles
     * @return the picked tiles
     */
    public List<TileType> getPickedTiles() {
        return pickedTiles;
    }

    /**
     * Sets the picked tiles
     * @param pickedTiles the picked tiles
     */
    public void setPickedTiles(List<TileType> pickedTiles) {
        this.pickedTiles = pickedTiles;
    }

    /**
     * Gets the chat
     * @return the chat
     */
    public Map<String, Map<String, String>> getChat() {
        return chat;
    }

    /**
     * Sets the chat
     * @param chat the chat
     */
    public void setChat(Map<String, Map<String, String>> chat) {
        this.chat = chat;
    }

    /**
     * Gets the points
     * @return the points
     */
    public Map<String, Integer> getPoints() {
        return points;
    }

    /**
     * Sets the points
     * @param points the points
     */
    public void setPoints(Map<String, Integer> points) {
        this.points = points;
    }

    /**
     * Gets the current turn player
     * @return the current turn player
     */
    public String getCurrentTurnPlayer() {
        return currentTurnPlayer;
    }

    /**
     * Sets the current turn player
     * @param currentTurnPlayer the current turn player
     */
    public void setCurrentTurnPlayer(String currentTurnPlayer) {
        this.currentTurnPlayer = currentTurnPlayer;
    }

    /**
     * Gets the personal goal
     * @return the personal goal
     */
    public String getPersonalGoal() {
        return personalGoal;
    }

    /**
     * Sets the personal goal
     * @param personalGoal the personal goal
     */
    public void setPersonalGoal(String personalGoal) {
        this.personalGoal = personalGoal;
    }

    /**
     * Gets the common cards
     * @return the common cards
     */
    public Map<String, List<Integer>> getCommonCards() {
        return commonCards;
    }

    /**
     * Sets the common cards
     * @param commonCards the common cards
     */
    public void setCommonCards(Map<String, List<Integer>> commonCards) {
        this.commonCards = commonCards;
    }

    /**
     * Gets the common goals
     * @return the common goals
     */
    public List<String> getCommonGoals() {
        return commonGoals;
    }

    /**
     * Sets the common goals
     * @param commonGoals the common goals
     */
    public void setCommonGoals(List<String> commonGoals) {
        this.commonGoals = commonGoals;
    }

    /**
     * Gets the saved games
     * @return the saved games
     */
    public List<String> getSavedGames() {
        return savedGames;
    }

    /**
     * Sets the saved games
     * @param savedGames the saved games
     */
    public void setSavedGames(List<String> savedGames) {
        this.savedGames = savedGames;
    }

    /**
     * Gets the winner
     * @return the winner
     */
    public String getWinner() {
        return winner;
    }

    /**
     * Sets the winner
     * @param winner the winner
     */
    public void setWinner(String winner) {
        this.winner = winner;
    }

    /**
     * Gets the details of a goal
     * @return the details of a goal
     */
    public Map<String, ClientGoalDetail> getGoalsToDetails() {
        return goalsToDetails;
    }

    /**
     * Sets the details of a goal
     * @param goalsToDetails the details of a goal
     */
    public void setGoalsToDetail(Map<String, ClientGoalDetail> goalsToDetails) {
        this.goalsToDetails = goalsToDetails;
    }

    /**
     * Gets if the player has put tiles
     * @return if the player has put tiles
     */
    public boolean getHasPutTiles() {
        return hasPutTiles;
    }

    /**
     * Sets if the player has put tiles
     * @param hasPutTiles if the player has put tiles
     */
    public void setHasPutTiles(boolean hasPutTiles) {
        this.hasPutTiles = hasPutTiles;
    }
}

