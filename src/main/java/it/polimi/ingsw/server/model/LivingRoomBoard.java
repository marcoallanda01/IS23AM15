package it.polimi.ingsw.server.model;

import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Function;
import java.util.stream.Collectors;

public class LivingRoomBoard {
    /**
     * The number of players
     */
    private final int numberOfPlayers;
    /**
     * The board is a list of lists of tiles.
     * <p>
     * The first list represents the rows, the second list represents the columns
     */
    private final Map<Integer, Map<Integer, Tile>> board;
    /**
     * The mask is a list of lists of TileRules
     * <p>
     * The first list represents the rows, the second list represents the columns
     */
    private Map<Integer, Map<Integer, TileRule>> mask;
    /**
     * The bag is a map of tile types and the number of remaining tiles of that type
     */
    private final Map<TileType, Integer> bag;

    public LivingRoomBoard(int numberOfPlayers) {
        this.numberOfPlayers = numberOfPlayers;
        this.board = new HashMap<>();
        this.bag = Arrays.stream(TileType.values()).collect(Collectors.toMap(Function.identity(), TileType::getNumberOfTilesPerType));
        //String maskJSON = Files.readString(Paths.get(getClass().getClassLoader().getResource("data/mask.json").toURI()));
        this.mask = new HashMap<>();
        mask.put(0, Map.of(0, TileRule.BLOCK, 1, TileRule.BLOCK, 2, TileRule.BLOCK, 3, TileRule.THREE, 4, TileRule.FOUR, 5, TileRule.BLOCK, 6, TileRule.BLOCK, 7, TileRule.BLOCK, 8,
                TileRule.BLOCK));
        mask.put(1, Map.of(0, TileRule.BLOCK, 1, TileRule.BLOCK, 2, TileRule.BLOCK, 3, TileRule.TWO, 4, TileRule.TWO, 5, TileRule.FOUR, 6, TileRule.BLOCK, 7, TileRule.BLOCK, 8,
                TileRule.BLOCK));
        mask.put(2, Map.of(0, TileRule.BLOCK, 1, TileRule.BLOCK, 2, TileRule.THREE, 3, TileRule.TWO, 4, TileRule.TWO, 5, TileRule.TWO, 6, TileRule.THREE, 7, TileRule.BLOCK, 8,
                TileRule.BLOCK));
        mask.put(3, Map.of(0, TileRule.BLOCK, 1, TileRule.FOUR, 2, TileRule.TWO, 3, TileRule.TWO, 4, TileRule.TWO, 5, TileRule.TWO, 6, TileRule.TWO, 7, TileRule.TWO, 8,
                TileRule.THREE));
        mask.put(4,
                Map.of(0, TileRule.FOUR, 1, TileRule.TWO, 2, TileRule.TWO, 3, TileRule.TWO, 4, TileRule.TWO, 5, TileRule.TWO, 6, TileRule.TWO, 7, TileRule.TWO, 8, TileRule.FOUR));
        mask.put(5,
                Map.of(0, TileRule.THREE, 1, TileRule.TWO, 2, TileRule.TWO, 3, TileRule.TWO, 4, TileRule.TWO, 5, TileRule.TWO, 6, TileRule.TWO, 7, TileRule.TWO, 8, TileRule.FOUR));
        mask.put(6, Map.of(0, TileRule.BLOCK, 1, TileRule.BLOCK, 2, TileRule.THREE, 3, TileRule.TWO, 4, TileRule.TWO, 5, TileRule.TWO, 6, TileRule.THREE, 7, TileRule.BLOCK, 8,
                TileRule.BLOCK));
        mask.put(7, Map.of(0, TileRule.BLOCK, 1, TileRule.BLOCK, 2, TileRule.BLOCK, 3, TileRule.FOUR, 4, TileRule.TWO, 5, TileRule.TWO, 6, TileRule.BLOCK, 7, TileRule.BLOCK, 8,
                TileRule.BLOCK));
        mask.put(8, Map.of(0, TileRule.BLOCK, 1, TileRule.BLOCK, 2, TileRule.BLOCK, 3, TileRule.FOUR, 4, TileRule.THREE, 5, TileRule.BLOCK, 6, TileRule.BLOCK, 7, TileRule.BLOCK, 8,
                TileRule.BLOCK));
    }

    public LivingRoomBoard(int numberOfPlayers, Map<Integer, Map<Integer, TileRule>> mask) {
        this(numberOfPlayers);
        this.mask = mask;
    }

    /**
     * Returns a copy of the board
     * @return a copy of the board
     */
    public Map<Integer, Map<Integer, Tile>> getBoard() {
        return Map.copyOf(board);
    }

    /**
     * Checks if there are only sparse tiles on the board and returns true if the board should be filled
     *
     * @return true if the board should be filled
     */
    public boolean isToFill() {
        AtomicBoolean toFill = new AtomicBoolean(true);
        board.forEach((row, map) -> map.forEach((col, tile) -> {
            if (tile != null) {
                List<Tile> adjacentTiles = getAdjacentTiles(tile);
                if (adjacentTiles.stream().anyMatch(Objects::nonNull)) {
                    toFill.set(false);
                }
            }
        }));
        return toFill.get();
    }

    /**
     * Fills the board with tiles according to the mask (based on the number of players)
     */
    public void fillBoard() {
        board.forEach((row, map) -> map.forEach((col, tile) -> {
            if(tile != null) {
                bag.merge(tile.getType(), 1, Integer::sum); // add the tile to the bag
            }
        }));
        mask.forEach((row, map) -> {
            board.put(row, new HashMap<>()); // initialize the row
            map.forEach((col, tileRule) -> {
                if (tileRule == TileRule.BLOCK) { // if the rule is BLOCK, set the tile to null
                    board.get(row).put(col, null);
                } else {
                    if (tileRule.ordinal() + 1 <= numberOfPlayers) { // if the rule is <= numberOfPlayers, pick a tile from the bag
                        TileType type = pickTileTypeFromBag();
                        if (type == null) {
                            throw new ArrestGameException("ArrestGameException: The bag is empty");
                        }
                        Tile tile = new Tile(row, col, type);
                        board.get(row).put(col, tile); // add the tile to the board
                    } else {
                        board.get(row).put(col, null); // if the rule is > numberOfPlayers, set the tile to null
                    }
                }
            });
        });
    }

    /**
     * Removes the tiles from the board
     *
     * @param tiles the tiles to remove
     */
    public void removeFromBoard(List<Tile> tiles) {
        for (Tile tile : tiles) {
            board.get(tile.getX()).put(tile.getY(), null);
        }
    }

    /**
     * Checks if the tiles can be picked based on 3 rules:
     * <p>
     * 1. The tiles must be present on the board
     * 2. The tiles must be adjacent and form a straight line
     * <p>
     * 3. The tiles must have at least one side free (not adjacent to another tile)
     *
     * @param tiles the tiles to pick
     * @return true if the tiles can be picked
     */
    public boolean checkPick(List<Tile> tiles) {
        if (tiles.isEmpty() || tiles.size() > 3) {
            return false;
        }
        for (Tile tile : tiles) {
            // check if the tiles are present on the board
            if (board.get(tile.getX()).get(tile.getY()) != tile) {
                return false;
            }
            // check if the tiles do not have free sides
            List<Tile> adjacentTiles = getAdjacentTiles(tile);
            if (!adjacentTiles.contains(null)) {
                return false;
            }

            for (Tile otherTile : tiles) { // check if the tiles are adjacent and form a straight line
                if (tile != otherTile) {
                    if (tile.getX() != otherTile.getX() && tile.getY() != otherTile.getY()) { // check if the tiles are not on the same row or column
                        return false;
                    }
                    if (Math.abs(tile.getX() - otherTile.getX()) > 1 || Math.abs(tile.getY() - otherTile.getY()) > 1) { // check if the tiles are not adjacent
                        if (tiles.size() == 3) { // if there are 3 tiles, check if the tiles are not at a distance of 2
                            if (Math.abs(tile.getX() - otherTile.getX()) > 2 || Math.abs(tile.getY() - otherTile.getY()) > 2) {
                                return false;
                            }
                        } else {
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    /**
     * Picks a random tile type from the bag and removes it from the bag
     * if the bag is empty, returns null
     *
     * @return a tile type
     */
    private @Nullable TileType pickTileTypeFromBag() {
        if (bag.isEmpty() || bag.values().stream().mapToInt(Integer::intValue).sum() == 0) {
            return null;
        }
        TileType type = TileType.getRandomTileType();
        if (bag.get(type) > 0) {
            bag.put(type, bag.get(type) - 1);
            return type;
        } else {
            return pickTileTypeFromBag();
        }
    }

    /**
     * Returns a list of the adjacent tiles of the given tile
     *
     * @param tile the tile to get the adjacent tiles of
     * @return a list of the adjacent tiles
     */
    private List<Tile> getAdjacentTiles(Tile tile) {
        Tile up = board.get(tile.getX() - 1) == null ? null : board.get(tile.getX() - 1).get(tile.getY());
        Tile down = board.get(tile.getX() + 1) == null ? null : board.get(tile.getX() + 1).get(tile.getY());
        Tile left = board.get(tile.getX()) == null ? null : board.get(tile.getX()).get(tile.getY() - 1);
        Tile right = board.get(tile.getX()) == null ? null : board.get(tile.getX()).get(tile.getY() + 1);
        return Arrays.asList(up, down, left, right);
    }


    @Override
    public String toString() {
        // print the board
        StringBuilder sb = new StringBuilder();
        sb.append("Board:\n");
        if(board.isEmpty()) {
            sb.append("Empty");
            return sb.toString();
        }
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                Tile tile = board.get(i).get(j);
                if (tile == null) {
                    sb.append("\uD83D\uDD33");
                } else {
                    sb.append(tile.getType().getSymbol());
                }
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}
