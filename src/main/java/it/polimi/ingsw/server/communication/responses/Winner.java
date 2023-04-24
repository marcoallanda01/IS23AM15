package it.polimi.ingsw.server.communication.responses;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.util.Objects;
import java.util.Optional;

public class Winner extends Msg{
    public String player; // player name

    public Winner(String player) {
        super("Winner");
        this.player = player;
    }

    /**
     * Generator of Winner from a json string
     * @param json json string from which generate returned object
     * @return Optional of Winner, empty if json string was not coherent
     */
    public static Optional<Winner> fromJson(String json){
        Winner w;
        try {
            Gson gson = new Gson();
            w = gson.fromJson(json, Winner.class);
        }catch (JsonSyntaxException e){
            return Optional.empty();
        }
        if(!"Winner".equals(w.name) || w.player == null){
            return Optional.empty();
        }
        return Optional.of(w);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Winner winner = (Winner) o;
        return Objects.equals(player, winner.player);
    }
}
