package it.polimi.ingsw.server.communication.responses;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.util.Objects;
import java.util.Optional;

public class PlayerPoints extends Msg{
    public String player;
    public int points;

    public PlayerPoints(String player, int points) {
        super("PlayerPoints");
        this.player = player;
        this.points = points;
    }

    /**
     * Generator of PlayerPoints from a json string
     * @param json json string from which generate returned object
     * @return Optional of PlayerPoints, empty if json string was not coherent
     */
    public static Optional<PlayerPoints> fromJson(String json){
        PlayerPoints pp;
        try {
            Gson gson = new Gson();
            pp = gson.fromJson(json, PlayerPoints.class);
        }catch (JsonSyntaxException e){
            return Optional.empty();
        }
        if(!"PlayerPoints".equals(pp.name) || pp.player == null || pp.points < 0){
            return Optional.empty();
        }
        return Optional.of(pp);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlayerPoints that = (PlayerPoints) o;
        return points == that.points && Objects.equals(player, that.player);
    }
}