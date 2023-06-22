package it.polimi.ingsw.communication.commands;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.util.Objects;
import java.util.Optional;

public class Reconnect extends GameCommand {

    /**
     * Reconnect command costructor
     * @param id player's id
     */
    public Reconnect(String id) {
        super("Reconnect", id);
    }

    /**
     * Generator of Reconnect from a json string
     * @param json json string from which generate object
     * @return Optional of Reconnect, empty if json string was not coherent
     */
    public static Optional<Reconnect> fromJson(String json) {
        Reconnect r;
        try{
            Gson gson = new Gson();
            r = gson.fromJson(json, Reconnect.class);
        }
        catch (JsonSyntaxException e){
            return Optional.empty();
        }
        if(!"Reconnect".equals(r.name) || r.id == null){
            return Optional.empty();
        }
        return Optional.of(r);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Reconnect that = (Reconnect) o;
        return Objects.equals(this.id, that.id);
    }
}
