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

    abstract public boolean pickTiles(List<Tile> tiles);

    abstract public boolean putTiles(List<Tile> tiles, int column);
}
