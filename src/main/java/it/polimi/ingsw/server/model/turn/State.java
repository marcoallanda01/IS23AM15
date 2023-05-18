package it.polimi.ingsw.server.model.turn;
import it.polimi.ingsw.server.model.Tile;
import it.polimi.ingsw.server.model.turn.Turn;

import java.util.List;

public abstract class State {
    public transient Turn turn;

    public State(Turn turn) {
        this.turn = turn;
    }

    abstract public boolean pickTiles(List<Tile> tiles);

    abstract public  boolean putTiles(List<Tile> tiles, int column);
}
