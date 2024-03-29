package it.polimi.ingsw.server.model.turn;

import it.polimi.ingsw.server.model.Tile;

import java.util.List;
import java.util.Optional;

/**
 * State where the player picks tiles from the board
 */
public class PickTilesState extends State {
    /**
     * Default constructor
     *
     * @param turn the turn to reference
     */
    public PickTilesState(Turn turn) {
        super(turn);
    }

    /**
     * Checks if the given tiles can be picked
     *
     * @param tiles the tiles to pick
     * @return the tiles if they can be picked, Optional.empty() otherwise
     */
    private Optional<List<Tile>> checkPick(List<Tile> tiles) {
        //return tiles.size() <= this.turn.getCurrentPlayer().getBookShelf().getMaxColumnSpace() && this.turn.getBoard().checkPick(tiles);
        Optional<List<Tile>> result = Optional.empty();
        if (tiles.size() <= this.turn.getCurrentPlayer().getBookShelf().getMaxColumnSpace() && this.turn.getBoard().checkPick(tiles)) {
            result = Optional.of(tiles);
        }
        return result;
    }

    /**
     * Picks the given tiles from the board
     *
     * @param tiles the tiles to pick
     * @return true if the tiles were picked, false otherwise
     */
    @Override
    public boolean pickTiles(List<Tile> tiles) {
        if (this.checkPick(tiles).isPresent()) {
            this.turn.setPickedTiles(tiles);
            this.turn.getBoard().removeFromBoard(tiles);
            return true;
        }
        return false;
    }

    @Override
    public boolean putTiles(List<Tile> tiles, int column) {
        return false;
    }

    @Override
    public String toString() {
        return "PickTilesState";
    }
}
