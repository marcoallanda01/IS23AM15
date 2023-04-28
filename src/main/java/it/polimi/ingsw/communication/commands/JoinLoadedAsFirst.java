package it.polimi.ingsw.communication.commands;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.util.Objects;
import java.util.Optional;

public class JoinLoadedAsFirst extends Command{
    public String player;
    public String idFirstPlayer;

    public JoinLoadedAsFirst(String player, String idFirstPlayer) {
        super("JoinLoadedAsFirst");
        this.player = new String(player);
        this.idFirstPlayer = new String(idFirstPlayer);
    }

    public static Optional<JoinLoadedAsFirst> fromJson(String json) {
        JoinLoadedAsFirst jlfp;
        try{
            Gson gson = new Gson();
            jlfp = gson.fromJson(json, JoinLoadedAsFirst.class);
        }
        catch (JsonSyntaxException e){
            return Optional.empty();
        }
        if(!"JoinLoadedAsFirst".equals(jlfp.name) || jlfp.player == null || jlfp.idFirstPlayer == null){
            return Optional.empty();
        }
        return Optional.of(jlfp);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JoinLoadedAsFirst that = (JoinLoadedAsFirst) o;
        return Objects.equals(player, that.player) && Objects.equals(idFirstPlayer, that.idFirstPlayer);
    }
}
