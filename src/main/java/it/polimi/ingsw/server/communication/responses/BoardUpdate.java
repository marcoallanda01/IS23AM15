package it.polimi.ingsw.server.communication.responses;

import it.polimi.ingsw.server.model.Tile;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class BoardUpdate extends Msg{
    public List<Tile> tiles;

    public BoardUpdate(@NotNull List<Tile> tiles) {
        super("BoardUpdate");
        this.tiles = tiles;
    }
}
