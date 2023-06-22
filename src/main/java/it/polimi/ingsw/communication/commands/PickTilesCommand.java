package it.polimi.ingsw.communication.commands;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import it.polimi.ingsw.server.model.Tile;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

public class PickTilesCommand extends GameCommand {

    public Set<Tile> tiles;

    /**
     * PickTilesCommand constructor
     * @param id player's id
     * @param tiles set of tiles picked
     */
    public PickTilesCommand(@NotNull String id, @NotNull Set<Tile> tiles) {
        super("PickTilesCommand", id);
        this.tiles = new HashSet<>();
        this.tiles.addAll(tiles);
    }

    /**
     * Generator of PickTilesCommand from a json string
     * @param json json string from which generate PickTilesCommand
     * @return Optional of PickTilesCommand, empty if json string was not coherent
     */
    public static Optional<PickTilesCommand> fromJson(String json) {
        PickTilesCommand ptc;
        try{
            Gson gson = new Gson();
            ptc = gson.fromJson(json, PickTilesCommand.class);
        }
        catch (JsonSyntaxException e){
            return Optional.empty();
        }
        if(!"PickTilesCommand".equals(ptc.name) || ptc.tiles == null || ptc.id == null){
            return Optional.empty();
        }
        return Optional.of(ptc);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PickTilesCommand that = (PickTilesCommand) o;
        if(!Objects.equals(this.id, that.id)) return false;
        if(this.tiles == null || that.tiles == null) return false;
        return Objects.equals(tiles, that.tiles);
    }
}
