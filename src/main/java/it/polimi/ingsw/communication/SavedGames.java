package it.polimi.ingsw.communication;

import com.google.gson.Gson;

import java.util.Set;

public class SavedGames extends Msg {
    public Set<String> names;

    public SavedGames(Set<String> names) {
        super("SavedGames");
        this.names = names;
    }

    public String toJson() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        sb.append("\"names\": [");

        for (String name : names) {
            sb.append("\"").append(name).append("\",");
        }

        if (!names.isEmpty()) {
            sb.deleteCharAt(sb.length() - 1); // Remove the last comma
        }

        sb.append("]");
        sb.append("}");
        return sb.toString();
    }

    public SavedGames fromJson(String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, SavedGames.class);
    }
}
