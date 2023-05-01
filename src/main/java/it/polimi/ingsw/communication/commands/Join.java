package it.polimi.ingsw.communication.commands;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.Optional;

public class Join extends Command{
    public String player;

    public Join(@NotNull String name){
        super("Join");
        this.player = new String(name);
    }

    /**
     * Generator of Join from a json string
     * @param json json string from which generate object
     * @return Optional of Join, empty if json string was not coherent
     */
    public static Optional<Join> fromJson(String json) {
        Join j;
        try{
            Gson gson = new Gson();
            j = gson.fromJson(json, Join.class);
        }
        catch (JsonSyntaxException e){
            return Optional.empty();
        }
        if(!"Join".equals(j.name) || j.player == null){
            return Optional.empty();
        }
        return Optional.of(j);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if( ! (o != null && getClass() == o.getClass()) ) return false;
        Join that = (Join) o;
        return Objects.equals(this.player, that.player);
    }
}
