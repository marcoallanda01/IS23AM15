package it.polimi.ingsw.communication;

import com.google.gson.Gson;

import java.util.Set;

public class LoadedGamePlayers extends Msg{
    public Set<String> names;

    public LoadedGamePlayers(Set<String> names) {
        super("LoadedGamePlayers");
        this.names = names;
    }

    public LoadedGamePlayers fromJson(String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, LoadedGamePlayers.class);
    }
}
