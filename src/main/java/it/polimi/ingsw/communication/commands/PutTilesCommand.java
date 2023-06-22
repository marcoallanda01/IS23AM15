package it.polimi.ingsw.communication.commands;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import it.polimi.ingsw.server.model.TileType;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class PutTilesCommand extends GameCommand {

    public List<TileType> tiles;
    public int column;

    /**
     * Put tiles command constructor
     * @param id player's id
     * @param tiles list of tiles to put
     * @param column bookshelf colum where to put the tiles
     */
    public PutTilesCommand(@NotNull String id, @NotNull List<TileType> tiles, int column) {
        super("PutTilesCommand", id);
        this.tiles = new ArrayList<>();
        this.tiles.addAll(tiles);
        this.column = column;
    }

    /**
     * Generator of PutTilesCommand from a json string
     * @param json json string from which generate PickTilesCommand
     * @return Optional of PutTilesCommand, empty if json string was not coherent
     */
    public static Optional<PutTilesCommand> fromJson(String json) {
        PutTilesCommand ptc;
        try{
            Gson gson = new Gson();
            ptc = gson.fromJson(json, PutTilesCommand.class);
        }
        catch (JsonSyntaxException e){
            return Optional.empty();
        }
        if(!"PutTilesCommand".equals(ptc.name) || ptc.tiles == null || ptc.column < 0 || ptc.id == null){
            return Optional.empty();
        }
        return Optional.of(ptc);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PutTilesCommand that = (PutTilesCommand) o;
        if(!Objects.equals(this.id, that.id)) return false;
        if(this.tiles == null || that.tiles == null) return false;
        if(this.column != that.column) return false;
        return Objects.equals(tiles, that.tiles);
    }


}
