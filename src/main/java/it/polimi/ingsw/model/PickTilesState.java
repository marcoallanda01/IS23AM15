package it.polimi.ingsw.model;

import java.util.List;

public class PickTilesState extends State{
    public PickTilesState(Turn turn) {
        super(turn);
    }

    private boolean checkPick(List<Tile> tiles) {
        return tiles.size() <= this.turn.getCurrentPlayer().getBookShelf().getMaxColumnSpace() && this.turn.getBoard().checkPick(tiles);
    }

    /**
     * Picks the given tiles from the board
     * @param tiles the tiles to pick
     * @return true if the tiles were picked, false otherwise
     */
    @Override
    public boolean pickTiles(List<Tile> tiles) {
        if (this.checkPick(tiles)) {
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
}
