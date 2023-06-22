package it.polimi.ingsw.communication.responses;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.util.Objects;
import java.util.Optional;

public class TurnNotify extends Msg{
    public String player;

    /**
     * Turn change notification constructor
     * @param name current player's name
     */
    public TurnNotify(String name){
        super("TurnNotify");
        this.player = name;
    }

    /**
     * Generator of TurnNotify from a json string
     * @param json json string from which generate returned object
     * @return Optional of TurnNotify, empty if json string was not coherent
     */
    public static Optional<TurnNotify> fromJson(String json){
        TurnNotify tn;
        try {
            Gson gson = new Gson();
            tn = gson.fromJson(json, TurnNotify.class);
        }catch (JsonSyntaxException e){
            return Optional.empty();
        }
        if(!"TurnNotify".equals(tn.name) || tn.player == null){
            return Optional.empty();
        }
        return Optional.of(tn);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TurnNotify that = (TurnNotify) o;
        return Objects.equals(player, that.player);
    }
}
