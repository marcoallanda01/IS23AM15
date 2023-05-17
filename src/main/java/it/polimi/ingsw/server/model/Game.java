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
import it.polimi.ingsw.server.model.turn.PickTilesState;
import it.polimi.ingsw.server.model.turn.PutTilesState;
import it.polimi.ingsw.server.model.turn.Turn;
import org.jetbrains.annotations.NotNull;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.*;
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
    private static final String PROPERTY_NAME = "currentTurn";

    private transient PushNotificationController pushNotificationController;

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
        String goalPath = isFirstGame ? "data/goalsFirstGame.json" : "data/goals.json";

        addPropertyChangeListener(new GameListener(pushNotificationController));
        notifyListeners();
        //FirstFill
        this.board.fillBoard();
        this.goalManager = new GoalManager(this.players, goalPath);
        //Necessary for first trigger of points notification and bookshelf
        this.players.forEach(Player::notifyListeners);

    }

    /**
     * Setting up of the game from another game. Must set first pushNotificationController.
     * Notification are sent to the clients.
     * @param game another game
     */
    public void setGame(Game game) {

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
        //Notify picked tiles if turn is in put tiles (state next pickTiles)
        if(this.currentTurn.getState().getClass() == PutTilesState.class && this.currentTurn.getPickedTiles() != null){
            GameChangeSupport.firePropertyChange("pickedTiles",
                    null,
                    new Turn(currentTurn.getPickedTiles(), currentTurn.getCurrentPlayer(), currentTurn.getBoard()));
        }

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
     */
    public @Deprecated Game(@NotNull List<String> players, boolean isFirstGame) throws ArrestGameException {
        this.players = players.stream().map(Player::new).collect(Collectors.toList());
        int numberOfPlayers = players.size();
        this.isFirstGame = isFirstGame;
        this.board = new LivingRoomBoard(numberOfPlayers);

        //choosing first player
        Collections.shuffle(this.players);
        this.currentTurn = new Turn(this.players.get(0), board);

        this.chat = new Chat(this.players);
        String goalPath = isFirstGame ? "data/goalsFirstGame.json" : "data/goals.json";
        this.goalManager = new GoalManager(this.players, goalPath);

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
            this.GameChangeSupport.firePropertyChange("gameStarted", null,
                    this);
            this.GameChangeSupport.firePropertyChange(PROPERTY_NAME, null,
                    this.currentTurn.getCurrentPlayer().getUserName());
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

    public boolean pickTiles(List<Tile> tiles) {
        if (currentTurn.pickTiles(tiles)) {
            GameChangeSupport.firePropertyChange("pickedTiles",
                    null,
                    new Turn(currentTurn.getPickedTiles(), currentTurn.getCurrentPlayer(), currentTurn.getBoard()));
            currentTurn.changeState(new PutTilesState(currentTurn));
            return true;
        } else {
            return false;
        }
    }

    public boolean putTiles(List<Tile> tiles, int column) {
        if (currentTurn.putTiles(tiles, column)) {
            if (currentTurn.checkBoardRefill()) {
                currentTurn.refillBoard();
            }
            Player player = currentTurn.getCurrentPlayer();
            goalManager.updatePointsTurn(player);
            if (player.getBookShelf().getMaxColumnSpace() == 0) {
                if (isLastRound) {
                    if (this.players.indexOf(player) == this.players.size() - 1) {
                        this.winner = goalManager.getWinner(this.players);
                        return true;
                    }
                } else {
                    player.setFirstToFinish(true);
                    isLastRound = true;
                }
            }
            currentTurn.changeState(new EndState(currentTurn));
            nextTurn(player);
            notifyListeners();
            return true;
        } else {
            return false;
        }
    }

    public Integer getPoints(String nickname) throws PlayerNotFoundException {
        Player player = this.getPlayerFromNickname(nickname);
        return goalManager.getPoints(player);
    }

    public Map<String, List<Integer>> getCommonGoalCardsToTokens() {
        return goalManager.getCommonCardsToTokens().entrySet().stream().collect(Collectors.toMap(entry -> entry.getKey().getName(), Map.Entry::getValue));
    }

    public Set<String> getUnfulfilledCommonGoalCards(String nickname) throws PlayerNotFoundException {
        Player player = this.getPlayerFromNickname(nickname);
        return goalManager.getFulfilledCommonCards(player).stream().map(Pattern::getName).collect(Collectors.toSet());
    }

    public Set<String> getFulfilledCommonGoalCards(String nickname) throws PlayerNotFoundException {
        Player player = this.getPlayerFromNickname(nickname);
        return goalManager.getFulfilledCommonCards(player).stream().map(Pattern::getName).collect(Collectors.toSet());
    }

    public String getPersonalGoalCard(String nickname) throws PlayerNotFoundException {
        Player player = this.getPlayerFromNickname(nickname);
        return goalManager.getPersonalCard(player).getName();
    }

    public List<Integer> getTokens(String nickname) throws PlayerNotFoundException {
        Player player = this.getPlayerFromNickname(nickname);
        return goalManager.getTokens(player);
    }

    public Set<String> getEndGameGoals() {
        return goalManager.getCommonGoals().stream().map(Pattern::getName).collect(Collectors.toSet());
    }

    public Player getPlayerFromNickname(String nickname) throws PlayerNotFoundException {
        try {
            return players.stream().filter(player -> player.getUserName().equals(nickname)).findAny().get();
        } catch (Exception e) {
            throw new PlayerNotFoundException();
        }
    }

    /**
     * Advances the turn to the next player
     *
     * @param currentPlayer the player who just finished his turn
     * @return true if the turn has been advanced, false if not
     */
    private boolean nextTurn(Player currentPlayer) {
        Player nextPlayer = this.players.get((this.players.indexOf(currentPlayer) + 1) % this.players.size());
        int playingPlayers = this.players.stream().mapToInt(player -> {return player.isPlaying() ? 1 : 0;}).sum();
        if (playingPlayers > 1){
            if (!nextPlayer.isPlaying()) {
                return nextTurn(nextPlayer);
            }
            if (this.currentTurn.getState() instanceof EndState) {
                this.currentTurn = new Turn(nextPlayer, this.board);

                notifyListeners();
                return true;
            }
        }
        return false;
    }

    /**
     * @return list of the players
     */
    public List<String> getPlayers() {
        return players.stream().map(Player::getUserName).collect(Collectors.toList());
    }

    public void sendMessage(String sender, String receiver, String message) throws PlayerNotFoundException {
        Message m = new Message(this.getPlayerFromNickname(sender), this.getPlayerFromNickname(receiver), message);
        try {
            this.chat.addMessage(m);
        } catch (PlayerNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(String sender, String message) throws PlayerNotFoundException {
        Message m = new Message(this.getPlayerFromNickname(sender), message);
        try {
            this.chat.addMessage(m);
        } catch (PlayerNotFoundException e) {
            e.printStackTrace();
        }
    }

    public List<Player> getPlayersList() {
        return this.players;
    }

    public String getWinner() {
        return winner;
    }

    public boolean isFirstGame() {
        return isFirstGame;
    }

    public LivingRoomBoard getBoard() {
        return board;
    }

    public Turn getCurrentTurn() {
        return currentTurn;
    }

    public Chat getChat() {
        return chat;
    }

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
        /*
            Not needed anymore --> games blocks until 1 player is playing
            if (players.stream().filter(p -> !p.isPlaying()).count() == players.size()-1) {
                // handle disconnection of last player
            }

         */
        // but now is needed(also before) a TODO if no one is playing
        if(players.stream().filter(p -> !p.isPlaying()).count() == players.size()){
            this.GameChangeSupport.firePropertyChange("lastPlayerDisconnected", null, player);
        }
        if (this.getCurrentPlayer().equals(player)) {
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
