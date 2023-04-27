package it.polimi.ingsw.communication.responses;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.Optional;

public class Reconnected extends Msg{
    public String player; // player name

    /**
     * Constructor
     * @param player name of the player who reconnected
     */
    public Reconnected(@NotNull String player) {
        super("Reconnected");
        this.player = player;
    }

    /**
     * Generator of Reconnected from a json string
     * @param json json string from which generate returned object
     * @return Optional of Reconnected, empty if json string was not coherent
     */
    public static Optional<Reconnected> fromJson(String json){
        Reconnected r;
        try {
            Gson gson = new Gson();
            r = gson.fromJson(json, Reconnected.class);
        }catch (JsonSyntaxException e){
            return Optional.empty();
        }
        if(!"Reconnected".equals(r.name) || r.player == null){
            return Optional.empty();
        }
        return Optional.of(r);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Reconnected that = (Reconnected) o;
        return Objects.equals(player, that.player);
    }
}
