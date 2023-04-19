package it.polimi.ingsw.communication;

import com.google.gson.Gson;

import java.util.Set;

public class LoadedGamePlayers extends Msg{
    public Set<String> names;

    public LoadedGamePlayers(Set<String> names) {
        super("LoadedGamePlayers");
        this.names = names;
    }

    @Override
    public String toJson() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        sb.append("\"names\":[");
        for (String name : this.names) {
            sb.append("\"").append(name).append("\",");
        }
        if (!this.names.isEmpty()) {
            sb.deleteCharAt(sb.length() - 1);
        }
        sb.append("]");
        sb.append("}");
        return toMsgJson(sb.toString());
    }

    public LoadedGamePlayers fromJson(String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, LoadedGamePlayers.class);
    }
}
