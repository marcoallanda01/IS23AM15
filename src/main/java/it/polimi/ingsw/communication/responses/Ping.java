package it.polimi.ingsw.communication.responses;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.util.Optional;

public class Ping extends Msg {

    public Ping(){
        super("Ping");
    }

    /**
     * Generator of Ping from a json string
     * @param json json string from which generate object
     * @return Optional of Ping, empty if json string was not coherent
     */
    public static Optional<Ping> fromJson(String json) {
        Ping p;
        try{
            Gson gson = new Gson();
            p = gson.fromJson(json, Ping.class);
        }
        catch (JsonSyntaxException e){
            return Optional.empty();
        }
        if(!"Ping".equals(p.name)){
            return Optional.empty();
        }
        return Optional.of(p);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        return o != null && getClass() == o.getClass();
    }
}
