package it.polimi.ingsw.server.model.turn;

import it.polimi.ingsw.server.model.Tile;

import java.util.List;

public class PutTilesState extends State {
    public PutTilesState(Turn turn) {
        super(turn);
    }

    @Override
    public boolean pickTiles(List<Tile> tiles) {
        return false;
    }

    /**
     * Puts the given tiles in the given column of the player's bookshelf
     * @param tiles the tiles to put
     * @param column the column where to put the tiles
     * @return true if the tiles were put, false otherwise
     */
    @Override
    public boolean putTiles(List<Tile> tiles, int column) {
        if(!this.turn.getPickedTiles().equals(tiles))
            return false;
        return this.turn.getCurrentPlayer().insertTiles(tiles, column);
    }

    @Override
    public String toString() {
        return "PutTilesState";
    }
}