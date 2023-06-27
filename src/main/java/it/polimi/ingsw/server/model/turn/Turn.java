package it.polimi.ingsw.server.model.turn;

import it.polimi.ingsw.server.model.LivingRoomBoard;
import it.polimi.ingsw.server.model.Player;
import it.polimi.ingsw.server.model.Tile;

import java.util.ArrayList;
import java.util.List;

/**
 * Class that represents a turn
 */
public class Turn {
    private final Player currentPlayer;
    private final LivingRoomBoard board;
    private List<Tile> pickedTiles;
    private State state;


    /**
     * Creates a new turn for the given player and board and sets the initial state to PickTilesState
     *
     * @param player the player who is playing the turn
     * @param board  the board where the player is playing
     */
    public Turn(Player player, LivingRoomBoard board) {
        this.currentPlayer = player;
        this.board = board;
        this.state = new PickTilesState(this);
        this.pickedTiles = null;
    }

    /**
     * Used for deserialization
     *
     * @param pickedTiles   the tiles picked by the player
     * @param currentPlayer the player who is playing the turn
     * @param board         the board where the player is playing
     */
    public Turn(List<Tile> pickedTiles, Player currentPlayer, LivingRoomBoard board) {
        this.pickedTiles = pickedTiles;
        this.currentPlayer = currentPlayer;
        this.board = board;
    }

    /**
     * Changes the state of the turn
     *
     * @param state the new state of the turn
     */
    public void changeState(State state) {
        this.state = state;
    }

    /**
     * Picks the given tiles from the board
     *
     * @param tiles the tiles to pick
     * @return true if the tiles were picked, false otherwise
     */
    public boolean pickTiles(List<Tile> tiles) {
        return this.state.pickTiles(tiles);
    }

    /**
     * Puts the given tiles in the given column of the player's bookshelf
     *
     * @param tiles  the tiles to put
     * @param column the column where to put the tiles
     * @return true if the tiles were put, false otherwise
     */
    public boolean putTiles(List<Tile> tiles, int column) {
        return this.state.putTiles(tiles, column);
    }

    /**
     * Checks if the board needs to be refilled
     *
     * @return true if the board needs to be refilled, false otherwise
     */
    public boolean checkBoardRefill() {
        return this.board.isToFill();
    }

    /**
     * Refills the board
     */
    public void refillBoard() {
        this.board.fillBoard();
    }

    /**
     * Gets the current player
     *
     * @return the current player
     */
    public Player getCurrentPlayer() {
        return this.currentPlayer;
    }

    /**
     * Gets the board
     *
     * @return the board
     */
    public LivingRoomBoard getBoard() {
        return this.board;
    }

    /**
     * Gets the state of the turn
     *
     * @return the state of the turn
     */
    public State getState() {
        return state;
    }

    /**
     * Gets the tiles picked by the player
     *
     * @return the tiles picked by the player
     */
    public List<Tile> getPickedTiles() {
        return pickedTiles == null ? null : new ArrayList<>(pickedTiles);
    }

    /**
     * Sets the picked tiles
     *
     * @param pickedTiles the tiles picked by the player
     */
    public void setPickedTiles(List<Tile> pickedTiles) {
        this.pickedTiles = pickedTiles;
    }
}
