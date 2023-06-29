package it.polimi.ingsw.server.model.turn;

import it.polimi.ingsw.server.model.Tile;

import java.util.List;

/**
 * Class that represents the end state of a turn
 */
public class EndState extends State {
    /**
     * Creates a new EndState for the given turn
     *
     * @param turn the turn where the state is used
     */
    public EndState(Turn turn) {
        super(turn);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean pickTiles(List<Tile> tiles) {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean putTiles(List<Tile> tiles, int column) {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "EndState";
    }
}
