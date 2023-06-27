package it.polimi.ingsw.server.model;

import java.io.Serializable;
import java.util.Objects;

/**
 * Class that represents a tile
 */
public class Tile implements Serializable {
    private final TileType type;
    private int x;
    private int y;

    /**
     * Default constructor
     *
     * @param x    x coordinate
     * @param y    y coordinate
     * @param type type of the tile
     */
    public Tile(int x, int y, TileType type) {
        this.x = x;
        this.y = y;
        this.type = type;
    }

    /**
     * Constructor that sets x and y to -1
     *
     * @param type type of the tile
     */
    public Tile(TileType type) {
        this(-1, -1, type);
    }

    /**
     * Gets the x coordinate of the tile
     *
     * @return x coordinate
     */
    public int getX() {
        return x;
    }

    /**
     * Sets the x coordinate of the tile
     *
     * @param x x coordinate
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * Gets the y coordinate of the tile
     *
     * @return y coordinate
     */
    public int getY() {
        return y;
    }

    /**
     * Sets the y coordinate of the tile
     *
     * @param y y coordinate
     */
    public void setY(int y) {
        this.y = y;
    }

    /**
     * Gets the type of the tile
     *
     * @return type of the tile
     */
    public TileType getType() {
        return type;
    }

    @Override
    public String toString() {
        return "Tile{" + "x=" + x + ", y=" + y + ", type=" + type + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tile tile = (Tile) o;
        return x == tile.x && y == tile.y && type == tile.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, type);
    }

}
