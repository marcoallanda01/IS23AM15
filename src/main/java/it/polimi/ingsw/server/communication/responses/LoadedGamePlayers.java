package it.polimi.ingsw.server.communication.responses;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;

public class LoadedGamePlayers extends Msg {
    public Set<String> names;

    public LoadedGamePlayers(@NotNull Set<String> names) {
        super("LoadedGamePlayers");
        this.names = names;
    }

    /**
     * Generator of LoadedGamePlayers from a json string
     * @param json json string from which generate returned object
     * @return Optional of LoadedGamePlayers, empty if json string was not coherent
     */
    public static Optional<LoadedGamePlayers> fromJson(String json) {
        LoadedGamePlayers sg;
        try {
            Gson gson = new Gson();
            sg = gson.fromJson(json, LoadedGamePlayers.class);
        }catch (JsonSyntaxException e){
            return Optional.empty();
        }
        if(!"LoadedGamePlayers".equals(sg.name) || sg.names == null){
            return Optional.empty();
        }
        return Optional.of(sg);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LoadedGamePlayers that = (LoadedGamePlayers) o;
        return Objects.equals(names, that.names);
    }
}
