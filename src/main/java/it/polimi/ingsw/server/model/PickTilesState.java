package it.polimi.ingsw.server.model;

import java.util.List;
import java.util.Optional;

public class PickTilesState extends State{
    public PickTilesState(Turn turn) {
        super(turn);
    }

    private Optional<List<Tile>> checkPick(List<Tile> tiles) {
        //return tiles.size() <= this.turn.getCurrentPlayer().getBookShelf().getMaxColumnSpace() && this.turn.getBoard().checkPick(tiles);
        Optional<List<Tile>> result = Optional.empty();
        if (tiles.size() <= this.turn.getCurrentPlayer().getBookShelf().getMaxColumnSpace() && this.turn.getBoard().checkPick(tiles)){
            result = Optional.of(tiles);
        }
        return result;
    }

    /**
     * Picks the given tiles from the board
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
