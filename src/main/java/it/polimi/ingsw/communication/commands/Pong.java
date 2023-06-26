package it.polimi.ingsw.communication.commands;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.util.Objects;
import java.util.Optional;

/**
 * Pong command, used in response to a Ping message
 */
public class Pong extends GameCommand {

    /**
     * Pong constructor
     * @param id player's id
     */
    public Pong(String id){
        super("Pong", id);
    }

    /**
     * Generator of Pong from a json string
     * @param json json string from which generate Pong
     * @return Optional of Pong, empty if json string was not coherent
     */
    public static Optional<Pong> fromJson(String json) {
        Pong p;
        try{
            Gson gson = new Gson();
            p = gson.fromJson(json, Pong.class);
        }
        catch (JsonSyntaxException e){
            return Optional.empty();
        }
        if(!"Pong".equals(p.name) || p.id == null){
            return Optional.empty();
        }
        return Optional.of(p);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pong that = (Pong) o;
        return Objects.equals(this.id, that.id);
    }
}
