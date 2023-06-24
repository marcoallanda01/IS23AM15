package it.polimi.ingsw.server.model;

import it.polimi.ingsw.server.controller.PushNotificationController;
import it.polimi.ingsw.server.listeners.GameListener;
import it.polimi.ingsw.server.model.chat.Chat;
import it.polimi.ingsw.server.model.chat.Message;
import it.polimi.ingsw.server.model.exceptions.ArrestGameException;
import it.polimi.ingsw.server.model.exceptions.PlayerNotFoundException;
import it.polimi.ingsw.server.model.managers.GoalManager;
import it.polimi.ingsw.server.model.managers.patterns.Pattern;
import it.polimi.ingsw.server.model.turn.EndState;
import it.polimi.ingsw.server.model.turn.PutTilesState;
import it.polimi.ingsw.server.model.turn.Turn;
import org.jetbrains.annotations.NotNull;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

public class Game{
    private List<Player> players = null;
    private String winner;
    private boolean isLastRound = false;
    private boolean isFirstGame;
    private LivingRoomBoard board;
    private Turn currentTurn;
    private Chat chat;
    private GoalManager goalManager;
    private PropertyChangeSupport GameChangeSupport;
    private static final String TURN_PROPRIETY_NAME = "currentTurn";
    private static final String PICKED_TILES_PROPRIETY_NAME = "pickedTiles";
    private static final String GAME_PROPRIETY_NAME = "gameStarted";
    private static final String WINNER_PROPRIETY_NAME = "gameWon";
    private static final String goalPath= "data/goals.json";
    private static final int WINNER_TIMEOUT = 20;
    private transient PushNotificationController pushNotificationController;
    private ScheduledFuture<?> winnerTimeout = null;

    /**
     * Only method used for instantiation of the game
     * @param pushNotificationController Push Notification Controller
     */
    public Game(PushNotificationController pushNotificationController){
        this.pushNotificationController = pushNotificationController;
        // Creation of the propriety support
        this.GameChangeSupport = new PropertyChangeSupport(this);
    }

    /**
     * Setting up of the game, notification are sent to the clients
     * @param players list of players' names
     * @param isFirstGame easy rules game rule
     * @throws ArrestGameException
     */
    public void setGame(@NotNull List<String> players, boolean isFirstGame) throws ArrestGameException {
        this.players = players.stream().map(Player::new).collect(Collectors.toList());
        this.players.forEach((p)->{p.setStandardListener(pushNotificationController);});
        int numberOfPlayers = players.size();
        this.isFirstGame = isFirstGame;
        this.board = new LivingRoomBoard(numberOfPlayers);
        this.board.setStandardListener(pushNotificationController);

        //choosing first player
        Collections.shuffle(this.players);
        this.currentTurn = new Turn(this.players.get(0), board);

        this.chat = new Chat(this.players);
        this.chat.setStandardListener(pushNotificationController);

        addPropertyChangeListener(new GameListener(pushNotificationController));
        notifyListeners();
        //FirstFill
        this.board.fillBoard();
        this.goalManager = new GoalManager(this.players, goalPath, isFirstGame);
        this.goalManager.getCommonCardsPointsManager().setStandardListener(pushNotificationController);
        this.goalManager.getCommonCardsPointsManager().notifyListeners();
        //Necessary for first trigger of points notification and bookshelf
        this.players.forEach(Player::notifyListeners);

    }

    /**
     * Setting up of the game from another game. Must set first pushNotificationController.
     * Notification are sent to the clients.
     * @param game another game
     */
    public void setGame(@NotNull Game game) {

        this.players = game.players;
        this.players.forEach((p)->{p.setStandardListener(pushNotificationController);});

        this.winner = game.winner;
        this.isFirstGame = game.isFirstGame;

        this.board = game.board;
        this.board.setStandardListener(pushNotificationController);


        this.currentTurn = game.currentTurn;
        this.chat = game.chat;
        this.chat.setStandardListener(pushNotificationController);
        this.goalManager = game.goalManager;
        this.goalManager.getCommonCardsPointsManager().setStandardListener(pushNotificationController);

        addPropertyChangeListener(new GameListener(pushNotificationController));
        notifyListeners();
        this.board.notifyListeners();
        //Force notifications
        this.goalManager.getCommonCardsPointsManager().notifyListeners();
        //Necessary for first trigger of points notification and bookshelf
        this.players.forEach(Player::notifyListeners);
    }

    /**
     * Help constructor used in tests that not requires listeners
     * @param players players
     * @param isFirstGame game rule easy game
     * @throws ArrestGameException
     */
    public @Deprecated Game(@NotNull List<String> players, boolean isFirstGame) throws ArrestGameException {
        this.GameChangeSupport = new PropertyChangeSupport(this);
        this.players = players.stream().map(Player::new).collect(Collectors.toList());
        int numberOfPlayers = players.size();
        this.isFirstGame = isFirstGame;
        this.board = new LivingRoomBoard(numberOfPlayers);

        //choosing first player
        Collections.shuffle(this.players);
        this.currentTurn = new Turn(this.players.get(0), board);

        this.chat = new Chat(this.players);
        this.goalManager = new GoalManager(this.players, goalPath, isFirstGame);

    }

    /**
     * Used for deserialization
     */
    public Game(List<Player> players, String winner, boolean isFirstGame, LivingRoomBoard board, Turn currentTurn, Chat chat, GoalManager goalManager) {
        this.players = players;
        this.winner = winner;
        this.isFirstGame = isFirstGame;
        this.board = board;
        this.currentTurn = currentTurn;
        this.chat = chat;
        this.goalManager = goalManager;
    }

    /**
     * Method to notify listeners about turn changes
     */
    private void notifyListeners(){
        if(this.GameChangeSupport != null) {
            this.GameChangeSupport.firePropertyChange(GAME_PROPRIETY_NAME, null,
                    this);
            this.GameChangeSupport.firePropertyChange(TURN_PROPRIETY_NAME, null,
                    this.currentTurn.getCurrentPlayer().getUserName());
            // When a game is loaded in the middle of a turn
            if(this.currentTurn.getState().getClass() == PutTilesState.class && this.currentTurn.getPickedTiles() != null){
                GameChangeSupport.firePropertyChange(PICKED_TILES_PROPRIETY_NAME,
                        null,
                        new Turn(currentTurn.getPickedTiles(), currentTurn.getCurrentPlayer(), currentTurn.getBoard()));
            }
        }
    }

    /**
     * Method to add listeners about turn changes
     * @param listener listener
     */
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        GameChangeSupport.addPropertyChangeListener(listener);
    }

    /**
     * Method to remove listeners about turn changes
     * @param listener listener
     */
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        GameChangeSupport.removePropertyChangeListener(listener);
    }

    /**
     * Current player pick tiles
     * Checks if the board needs refill and refills it if so
     * @param tiles tiles to pick
     * @return true if the pick was successful, false otherwise
     */
    public boolean pickTiles(List<Tile> tiles) {
        List<Tile> oldTiles = currentTurn.getPickedTiles();
        if (currentTurn.pickTiles(tiles)) {
            GameChangeSupport.firePropertyChange(PICKED_TILES_PROPRIETY_NAME,
                    new Turn(oldTiles, currentTurn.getCurrentPlayer(), currentTurn.getBoard()),
                    new Turn(currentTurn.getPickedTiles(), currentTurn.getCurrentPlayer(), currentTurn.getBoard()));
            currentTurn.changeState(new PutTilesState(currentTurn));
            // check board refill
            if (currentTurn.checkBoardRefill()) {
                currentTurn.refillBoard();
            }
            return true;
        } else {
            return false;
        }
    }

    /**
     * Put tiles action performed
     * @param tiles tiles to put
     * @param column column in witch put the tiles
     * @return True if action was performed correctly, false otherwise
     */
    public boolean putTiles(List<Tile> tiles, int column) {
        List<Tile> oldTiles = currentTurn.getPickedTiles();
        if (currentTurn.putTiles(tiles, column)) {
            GameChangeSupport.firePropertyChange(PICKED_TILES_PROPRIETY_NAME,
                    new Turn(oldTiles, currentTurn.getCurrentPlayer(), currentTurn.getBoard()),
                    new Turn(currentTurn.getPickedTiles(), currentTurn.getCurrentPlayer(), currentTurn.getBoard()));
            Player player = currentTurn.getCurrentPlayer();
            currentTurn.changeState(new EndState(currentTurn));
            nextTurn(player);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Get the points of the player
     * @param nickname
     * @return the points
     * @throws PlayerNotFoundException
     */
    public Integer getPoints(String nickname) throws PlayerNotFoundException {
        Player player = this.getPlayerFromNickname(nickname);
        return goalManager.getPoints(player);
    }

    /**
     * Get the common cards and their tokens
     * @return the common cards and their tokens
     */
    public Map<String, List<Integer>> getCommonCardsToTokens() {
        return goalManager.getCommonCardsToTokens().entrySet().stream().collect(Collectors.toMap(entry -> entry.getKey().getName(), Map.Entry::getValue));
    }

    /**
     * @param nickname the nickname of the player
     * Get the unfulfilled common cards
     * @return the common cards unfulfilled by the player
     */
    public Set<String> getUnfulfilledCommonCards(String nickname) throws PlayerNotFoundException {
        Player player = this.getPlayerFromNickname(nickname);
        return goalManager.getUnfulfilledCommonCards(player).stream().map(Pattern::getName).collect(Collectors.toSet());
    }

    /**
     * @param nickname the nickname of the player
     * Get the fulfilled common cards
     * @return the common cards fulfilled by the player
     */
    public Set<String> getFulfilledCommonCards(String nickname) throws PlayerNotFoundException {
        Player player = this.getPlayerFromNickname(nickname);
        return goalManager.getFulfilledCommonCards(player).stream().map(Pattern::getName).collect(Collectors.toSet());
    }

    /**
     * Get the personal goal card of the player
     * @return the personal goal card of the player
     */
    public String getPersonalGoalCard(String nickname) throws PlayerNotFoundException {
        Player player = this.getPlayerFromNickname(nickname);
        return goalManager.getPersonalCard(player).getName();
    }

    /**
     * Get the tokens of the player
     * @return the tokens of the player
     */
    public List<Integer> getTokens(String nickname) throws PlayerNotFoundException {
        Player player = this.getPlayerFromNickname(nickname);
        return goalManager.getTokens(player);
    }

    /**
     * Get the common goals
     * @return the common goals
     */
    public Set<String> getCommonGoals() {
        return goalManager.getCommonGoals().stream().map(Pattern::getName).collect(Collectors.toSet());
    }

    /**
     * given a nickname returns the player
     * @param nickname the nickname of the player
     * @return the player with that nickname
     * @throws PlayerNotFoundException
     */
    public Player getPlayerFromNickname(String nickname) throws PlayerNotFoundException {
        try {
            return players.stream().filter(player -> player.getUserName().equals(nickname)).findAny().get();
        } catch (Exception e) {
            throw new PlayerNotFoundException();
        }
    }

    /**
     * Performs end turn actions and advances the turn to the next player
     * Updates points
     * Checks last round
     * Checks winner
     * @param currentPlayer the player who just finished his turn
     * @return true if the turn has been advanced, false if not
     */
    private boolean nextTurn(Player currentPlayer) {
        // update points
        goalManager.updatePointsTurn(currentPlayer);
        // check last round
        if (currentPlayer.getBookShelf().getMaxColumnSpace() == 0) {
            currentPlayer.setFullBookShelf(true);
            if (!isLastRound) {
                currentPlayer.setFirstToFinish(true);
                isLastRound = true;
            }
        }
        // check winner
        if (isLastRound && (this.players.indexOf(currentPlayer) == (this.players.size() - 1))) {
            retrieveWinnerFromGoalManager();
            return false;
        }
        // take next player
        Player nextPlayer = this.players.get((this.players.indexOf(currentPlayer) + 1) % this.players.size());
        // check if there are any players playing
        int playingPlayers = this.players.stream().mapToInt(player -> player.isPlaying() ? 1 : 0).sum();
        if (playingPlayers < 2) {
            startWinnerTimeout();
            return false;
        }
        // check if the next player is playing
        if (!nextPlayer.isPlaying()) {
            return nextTurn(nextPlayer);
        }
        // check if changing turn is allowed
        if (this.currentTurn.getState() instanceof EndState || !currentPlayer.isPlaying()) {
            this.currentTurn = new Turn(nextPlayer, this.board);

            this.GameChangeSupport.firePropertyChange(TURN_PROPRIETY_NAME ,null, this.currentTurn.getCurrentPlayer().getUserName());
            return true;
        }
        return false;
    }

    /**
     * Starts the winner timeout
     */
    private void startWinnerTimeout() {
        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        winnerTimeout = executor.schedule(() -> {
            retrieveWinnerFromGoalManager();
        }, WINNER_TIMEOUT, TimeUnit.SECONDS);
    }

    /**
     * retrieves and notifies the winner
     */
    private void retrieveWinnerFromGoalManager () {
        this.winner = goalManager.getWinner(this.players);
        GameChangeSupport.firePropertyChange(WINNER_PROPRIETY_NAME, null, this.winner);
        System.out.println("GAME: THERE IS A WINNER!: "+this.winner);
    }
    /**
     * @return list of the players
     */
    public List<String> getPlayers() {
        return players.stream().map(Player::getUserName).collect(Collectors.toList());
    }
    /**
     * Sends a message to player
     * @param sender the sender of the message
     * @param receiver the receiver of the message
     * @param message the message
     * @throws PlayerNotFoundException
     */
    public void sendMessage(String sender, String receiver, String message) throws PlayerNotFoundException {
        Message m = new Message(this.getPlayerFromNickname(sender), this.getPlayerFromNickname(receiver), message);
        this.chat.addMessage(m);
    }

    /**
     * Sends a message to all
     * @param sender the sender of the message
     * @param message the message
     * @throws PlayerNotFoundException
     */
    public void sendMessage(String sender, String message) throws PlayerNotFoundException {
        Message m = new Message(this.getPlayerFromNickname(sender), message);
        this.chat.addMessage(m);
    }
    /**
     * Gets the messages of the player
     * @param player the player
     * @throws PlayerNotFoundException
     */
    public List<Message> getPlayerMessages(String player) throws PlayerNotFoundException  {
        return this.chat.getMessages(this.getPlayerFromNickname(player));
    }
    /**
     * Get the players in playing order
     * @return the players as a list
     */
    public List<Player> getPlayersList() {
        return this.players;
    }
    /**
     * Get the winner
     * @return the winner
     */
    public String getWinner() {
        return winner;
    }

    public boolean isFirstGame() {
        return isFirstGame;
    }
    /**
     * Get the board
     * @return the board
     */
    public LivingRoomBoard getBoard() {
        return board;
    }
    /**
     * Get the current turn
     * @return the current turn
     */
    public Turn getCurrentTurn() {
        return currentTurn;
    }

    /**
     * Get the chat
     * @return the chat
     */
    public Chat getChat() {
        return chat;
    }

    /**
     * Get the GoalManager
     * @return the goalManager
     */
    public GoalManager getGoalManager() {
        return goalManager;
    }

    /**
     * Get the player's name whom turn is
     *
     * @return name of current player
     */
    public String getCurrentPlayer() {
        return currentTurn.getCurrentPlayer().getUserName();
    }

    /**
     * Disconnect a player from the game
     *
     * @param player player to disconnect
     * @return true if the player is disconnected, false if the player is not found or is not playing
     */
    public boolean disconnectPlayer(String player){
        try {
            if (this.getPlayerFromNickname(player).isPlaying()) {
                this.getPlayerFromNickname(player).goToWc();
            } else {
                return false;
            }
        } catch (PlayerNotFoundException e) {
            e.printStackTrace();
            return false;
        }
        if(players.stream().filter(p -> !p.isPlaying()).count() == players.size()){
            this.GameChangeSupport.firePropertyChange("lastPlayerDisconnected", null, player);
        }
        if (this.getCurrentPlayer().equals(player)) {
            if(this.currentTurn.getState().getClass().equals(PutTilesState.class)){
                this.board.putBackInBag(this.currentTurn.getPickedTiles());
                if(this.board.isToFill()){
                    this.board.fillBoard();
                }
            }
            this.nextTurn(this.currentTurn.getCurrentPlayer());
        }
        return true;
    }

    /**
     * Reconnect a player to the game
     *
     * @param player player to reconnect
     * @return true if the player is reconnected, false if the player is not found or is already playing
     */
    public boolean reconnectPlayer(String player) {
        try {
            if (!this.getPlayerFromNickname(player).isPlaying()) {
                this.getPlayerFromNickname(player).backFromWc();

                // If there is just one player of if the player is in the end state then next turn is called
                nextTurn(this.currentTurn.getCurrentPlayer());
                if (winnerTimeout != null) {
                    winnerTimeout.cancel(true);
                    winnerTimeout = null;
                }
            } else {
                return false;
            }
        } catch (PlayerNotFoundException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
