package it.polimi.ingsw.server.communication.responses;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.Optional;

public class Disconnection extends Msg{
    public String player; // player name

    /**
     * Constructor
     * @param player name of the player who disconnected
     */
    public Disconnection(@NotNull String player) {
        super("Disconnection");
        this.player = player;
    }

    /**
     * Generator of Disconnection from a json string
     * @param json json string from which generate returned object
     * @return Optional of Disconnection, empty if json string was not coherent
     */
    public static Optional<Disconnection> fromJson(String json){
        Disconnection d;
        try {
            Gson gson = new Gson();
            d = gson.fromJson(json, Disconnection.class);
        }catch (JsonSyntaxException e){
            return Optional.empty();
        }
        if(!"Disconnection".equals(d.name) || d.player == null){
            return Optional.empty();
        }
        return Optional.of(d);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Disconnection that = (Disconnection) o;
        return Objects.equals(player, that.player);
    }
}
