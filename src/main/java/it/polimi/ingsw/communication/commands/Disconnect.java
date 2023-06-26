package it.polimi.ingsw.communication.commands;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.util.Objects;
import java.util.Optional;

/**
 * Command to disconnect
 */
public class Disconnect extends GameCommand {

    /**
     * Constructor
     * @param id player's id
     */
    public Disconnect(String id) {
        super("Disconnect", id);
    }

    /**
     * Generator of Disconnect from a json string
     * @param json json string from which generate Disconnect
     * @return Optional of Disconnect, empty if json string was not coherent
     */
    public static Optional<Disconnect> fromJson(String json) {
        Disconnect d;
        try{
            Gson gson = new Gson();
            d = gson.fromJson(json, Disconnect.class);
        }
        catch (JsonSyntaxException e){
            return Optional.empty();
        }
        if(!"Disconnect".equals(d.name) || d.id == null){
            return Optional.empty();
        }
        return Optional.of(d);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Disconnect that = (Disconnect) o;
        return Objects.equals(this.id, that.id);
    }
}
