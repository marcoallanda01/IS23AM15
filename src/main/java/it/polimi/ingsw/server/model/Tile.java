package it.polimi.ingsw.server.model;

import java.io.Serializable;

public class Tile implements Serializable {
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

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void removeFromBoard() {
        this.x = -1;
        this.y = -1;
    }

    public TileType getType() {
        return type;
    }

    @Override
    public String toString() {
        return "Tile{" + "x=" + x + ", y=" + y + ", type=" + type + '}';
    }

    public boolean equalsType(Tile t){
        return this.type == t.type;
    }

    public boolean equals(Tile t){
        return this.x == t.x && this.y == t.y && this.type == t.type;
    }
}
