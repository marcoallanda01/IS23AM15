package it.polimi.ingsw.communication;

import com.google.gson.Gson;

import java.util.Objects;
import java.util.Set;

public class SavedGames extends Msg {
    public Set<String> names;

    public SavedGames(Set<String> names) {
        super("SavedGames");
        this.names = names;
    }

    public String toJson() {
        StringBuilder sb = new StringBuilder();
        sb.append("{ \"name\": \"StringBuilder\", ");
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SavedGames that = (SavedGames) o;
        return names.equals(that.names);
    }

    @Override
    public int hashCode() {
        return Objects.hash(names);
    }
}
