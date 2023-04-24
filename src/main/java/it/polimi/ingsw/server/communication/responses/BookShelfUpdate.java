package it.polimi.ingsw.server.communication.responses;

import it.polimi.ingsw.server.model.Tile;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class BookShelfUpdate extends Msg{
    public String player;
    public List<Tile> tiles;

    public BookShelfUpdate(@NotNull String player, @NotNull List<Tile> tiles) {
        super("BookShelfUpdate");
        this.player = player;
        this.tiles = tiles;
    }
}
