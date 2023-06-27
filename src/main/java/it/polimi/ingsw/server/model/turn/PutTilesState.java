package it.polimi.ingsw.server.model.turn;

import it.polimi.ingsw.server.model.Tile;
import it.polimi.ingsw.server.model.TileType;

import java.util.ArrayList;
import java.util.List;

/**
 * State where the player puts tiles in their bookshelf
 */
public class PutTilesState extends State {
    /**
     * Default constructor
     *
     * @param turn the turn to reference
     */
    public PutTilesState(Turn turn) {
        super(turn);
    }

    @Override
    public boolean pickTiles(List<Tile> tiles) {
        return false;
    }

    /**
     * Puts the given tiles in the given column of the player's bookshelf
     *
     * @param tiles  the tiles to put
     * @param column the column where to put the tiles
     * @return true if the tiles were put, false otherwise
     */
    @Override
    public boolean putTiles(List<Tile> tiles, int column) {
        List<TileType> pickedTileTypes = this.turn.getPickedTiles().stream().map(Tile::getType).toList();
        List<TileType> putTileTypes = tiles.stream().map(Tile::getType).toList();
        for (TileType tt : TileType.values()) {
            if (pickedTileTypes.stream().filter(tt::equals).count()
                    !=
                    putTileTypes.stream().filter(tt::equals).count()
            ) {
                return false;
            }
        }
        if (this.turn.getCurrentPlayer().insertTiles(tiles, column)) {
            this.turn.setPickedTiles(new ArrayList<>());
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return "PutTilesState";
    }
}
