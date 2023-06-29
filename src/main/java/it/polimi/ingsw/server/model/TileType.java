package it.polimi.ingsw.server.model;

import java.io.Serializable;

/**
 * Enum that represents the type of the tiles
 */
public enum TileType implements Serializable {
    /**
     * Cat tile type
     */
    CAT,
    /**
     * Book tile type
     */
    BOOK,
    /**
     * Game tile type
     */
    GAME,
    /**
     * Frame tile type
     */
    FRAME,
    /**
     * Trophy tile type
     */
    TROPHY,
    /**
     * Plant tile type
     */
    PLANT;

    /**
     * Gets a random tile type
     *
     * @return a random tile type
     */
    public static TileType getRandomTileType() {
        return values()[(int) (Math.random() * values().length)];
    }

    /**
     * Gets a tile type from its name
     * @param name name of the tile type you want to create
     * @return null if there is not a tile type with that name, else a tile type with that name
     */
    public static TileType tileTypeFromName(String name) {
        for (TileType tt : TileType.values()) {
            if (tt.name().equals(name)) {
                return tt;
            }
        }
        return null;
    }

    /**
     * Gets the number of tiles per type
     *
     * @return the number of tiles per type
     */
    public int getNumberOfTilesPerType() {
        return 22;
    }

    /**
     * Gets the symbol of the tile type
     *
     * @return the symbol of the tile type
     */
    public String getSymbol() {
        return switch (this) {
            case CAT -> "🐱";
            case BOOK -> "📚";
            case GAME -> "🎲";
            case FRAME -> "🖼️";
            case TROPHY -> "🏆";
            case PLANT -> "🌱";
        };
    }
}
