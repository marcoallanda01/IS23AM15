package it.polimi.ingsw.modules;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BookShelf {
    /**
     * The list of tiles in the bookshelf
     */
    private List<List<Optional<Tile>>> currentTiles;

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
     * @return true if the tiles were inserted, false otherwise
     */
    public boolean insertTiles(List<Tile> tiles, int column) {
        if (column < 0 || column >= currentTiles.size()) return false;
        int count = 0;
        for(Optional<Tile> tile: currentTiles.get(column)) {
            if(tile.isEmpty()) count++;
        }
        if(count < tiles.size()) return false;
        for (Tile tile : tiles) {
            if (!insertTile(tile, column)) return false;
        }
        return true;
    }

    /**
     * Returns the tile at the specified position
     *
     * @param column the column of the tile
     * @param row    the row of the tile
     * @return the tile at the specified position
     */
    public Tile getTile(int column, int row) {
        if(currentTiles.get(column).get(row).isEmpty()) return null;
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
        int max = 0;
        for (List<Optional<Tile>> columns : currentTiles) {
            int count = 0;
            for (Optional<Tile> tile : columns) {
                if (tile.isEmpty()) count++;
            }
            if (count > max) max = count;
        }
        return max;
    }

    /**
     * Gets the state of the bookshelf
     *
     * @return the state of the bookshelf
     */
    public List<List<Optional<Tile>>> getState() {
        return currentTiles;
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
