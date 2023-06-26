package it.polimi.ingsw.server.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Class that represents the bookshelf of a player
 */
public class BookShelf{
    /**
     * The list of tiles in the bookshelf
     */
    private final List<List<Optional<Tile>>> currentTiles;

    /**
     * Default constructor
     * @param numRows the number of rows in the bookshelf
     * @param numColumns the number of columns in the bookshelf
     */
    public BookShelf(int numRows, int numColumns) {
        currentTiles = new ArrayList<>();
        for (int i = 0; i < numColumns; i++) {
            List<Optional<Tile>> column = new ArrayList<>();
            for (int j = 0; j < numRows; j++) {
                column.add(Optional.empty());
            }
            currentTiles.add(column);
        }
    }

    /**
     * Constructor with default values
     */
    public BookShelf() {
        this(6, 5);
    }

    /**
     * Inserts a tile in the first empty space in the specified column
     *
     * @param tile   the tile to insert
     * @param column the column in which to insert the tile
     * @return true if the tile was inserted, false otherwise
     */
    private boolean insertTile(Tile tile, int column) {
        if (column < 0 || column >= currentTiles.size()) return false;
        List<Optional<Tile>> currentColumn = currentTiles.get(column);
        for (Optional<Tile> t : currentColumn) {
            if (t.isEmpty()) {
                tile.setX(column);
                tile.setY(currentColumn.indexOf(t));
                currentColumn.set(currentColumn.indexOf(t), Optional.of(tile));
                return true;
            }
        }
        return false;
    }

    /**
     * Inserts a list of tiles in the first empty space in the specified column
     *
     * @param tiles  the tiles to insert
     * @param column the column in which to insert the tiles
     * @return list of tiles with coordinates if the tiles were inserted, null otherwise
     */
    public List<Tile> insertTiles(List<Tile> tiles, int column) {
        if (column < 0 || column >= currentTiles.size()) return null;
        int count = 0;
        for (Optional<Tile> tile : currentTiles.get(column)) {
            if (tile.isEmpty()) count++;
        }
        if (count < tiles.size()) return null;
        for (Tile tile : tiles) {
            if (!insertTile(tile, column)) return null;
        }
        return getAllTiles();
    }


    /**
     * Get the bookshelf as list of tiles
     * @return tiles in board
     */
    public List<Tile> getAllTiles(){
        return this.currentTiles.stream()
                .flatMap((Collection::stream))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    /**
     * Returns the tile at the specified position
     *
     * @param column the column of the tile
     * @param row    the row of the tile
     * @return the tile at the specified position
     */
    public Tile getTile(int column, int row) {
        if (currentTiles.get(column).get(row).isEmpty()) return null;
        return currentTiles.get(column).get(row).get();
    }

    /**
     * Clears the bookshelf
     */
    public void clearBookShelf() {
        for (List<Optional<Tile>> columns : currentTiles) {
            columns.replaceAll(ignored -> Optional.empty());
        }
    }

    /**
     * Gets the number of spaces free in the column with the most free spaces
     *
     * @return the number of spaces free in the column with the most free spaces
     */
    public int getMaxColumnSpace() {
        int maxEmptyTileCount = 0;
        for (List<Optional<Tile>> column : currentTiles) {
            int emptyTileCount = 0;
            for (Optional<Tile> tile : column) {
                if (tile.isEmpty()) emptyTileCount++;
            }
            maxEmptyTileCount = Math.max(maxEmptyTileCount, emptyTileCount);
        }
        return maxEmptyTileCount;
    }

    /**
     * Gets the state of the bookshelf
     *
     * @return the clone of the state of the bookshelf
     */
    public List<List<Optional<Tile>>> getState() {
        List<List<Optional<Tile>>> returnList = new ArrayList<>();
        for (List<Optional<Tile>> sublist : currentTiles) {
            returnList.add(new ArrayList<>(sublist));
        }
        return returnList;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("BookShelf:\n");
        for (int i = currentTiles.get(0).size() - 1; i >= 0; i--) {
            for (List<Optional<Tile>> currentTile : currentTiles) {
                Optional<Tile> tile = currentTile.get(i);
                sb.append(tile.isEmpty() ? "\uD83D\uDD33" : tile.get().getType().getSymbol());
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}
