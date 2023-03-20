package it.polimi.ingsw.modules;

public class Tile {
    private int boardX;
    private int boardY;
    private final TileType type;

    public Tile(int boardX, int boardY, TileType type) {
        this.boardX = boardX;
        this.boardY = boardY;
        this.type = type;
    }

    public int getBoardX() {
        return boardX;
    }

    public int getBoardY() {
        return boardY;
    }

    public void removeFromBoard() {
        this.boardX = -1;
        this.boardY = -1;
    }

    public TileType getType() {
        return type;
    }

    @Override
    public String toString() {
        return "Tile{" + "boardX=" + boardX + ", boardY=" + boardY + ", type=" + type + '}';
    }

    public boolean equalsType(Tile t){
        return this.type == t.type;
    }

    public boolean equals(Tile t){
        return this.boardX == t.boardX && this.boardY == t.boardY && this.type == t.type;
    }
}
