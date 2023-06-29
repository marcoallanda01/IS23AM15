package it.polimi.ingsw.server.model.turn;

import it.polimi.ingsw.server.model.Tile;

import java.util.List;

/**
 * Abstract class that represents a state of the turn
 */
public abstract class State {
    /**
     * The turn to reference
     */
    public transient Turn turn;

    /**
     * Default constructor
     *
     * @param turn the turn to reference
     */
    public State(Turn turn) {
        this.turn = turn;
    }

    /**
     * Picks the given tiles from the board
     *
     * @param tiles the tiles to pick
     * @return true if the tiles were picked, false otherwise
     */
    abstract public boolean pickTiles(List<Tile> tiles);

    /**
     * Puts the given tiles in the given column
     *
     * @param tiles  the tiles to put
     * @param column the column where to put the tiles
     * @return true if the tiles were put, false otherwise
     */
    abstract public boolean putTiles(List<Tile> tiles, int column);
}
