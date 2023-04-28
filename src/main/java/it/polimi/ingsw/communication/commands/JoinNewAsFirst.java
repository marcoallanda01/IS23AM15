package it.polimi.ingsw.communication.commands;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.Optional;

public class JoinNewAsFirst extends Command{
    public String player;
    public int numOfPlayers;
    public String idFirstPlayer;
    public boolean easyRules;



    public JoinNewAsFirst(@NotNull String name, int numPlayersGame, @NotNull String idFirstPlayer, boolean easyRules){
        super("JoinNewAsFirst");
        this.player = new String(name);
        this.numOfPlayers = numPlayersGame;
        this.idFirstPlayer = new String(idFirstPlayer);
        this.easyRules = easyRules;
    }

    public static Optional<JoinNewAsFirst> fromJson(String json) {
        JoinNewAsFirst jnfp;
        try{
            Gson gson = new Gson();
            jnfp = gson.fromJson(json, JoinNewAsFirst.class);
        }
        catch (JsonSyntaxException e){
            return Optional.empty();
        }
        if(!"JoinNewAsFirst".equals(jnfp.name) || jnfp.player == null || jnfp.idFirstPlayer == null){
            return Optional.empty();
        }
        return Optional.of(jnfp);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JoinNewAsFirst that = (JoinNewAsFirst) o;
        return numOfPlayers == that.numOfPlayers && easyRules == that.easyRules &&
                Objects.equals(player, that.player) && Objects.equals(idFirstPlayer, that.idFirstPlayer);
    }

}
