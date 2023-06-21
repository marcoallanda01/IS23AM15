package it.polimi.ingsw.server.model;

import java.io.Serializable;
import java.util.Objects;

public class Tile implements  Serializable{
    private int x;
    private int y;
    private final TileType type;

    public Tile(int x, int y, TileType type) {
        this.x = x;
        this.y = y;
        this.type = type;
    }

    public Tile(TileType type) {
        this(-1, -1, type);
    }

    /*
        Gets the x coordinate of the tile
     */
    public int getX() {
        return x;
    }

    /*
        Gets the y coordinate of the tile
     */
    public int getY() {
        return y;
    }

    /*
        Sets the x coordinate of the tile
     */
    public void setX(int x) {
        this.x = x;
    }

    /*
        Sets the y coordinate of the tile
     */
    public void setY(int y) {
        this.y = y;
    }

    /*
        Gets the type of the tile
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
