package it.polimi.ingsw.communication.responses;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.util.Optional;
import java.util.Set;

public class SavedGames extends Msg {
    public Set<String> names;

    public SavedGames(Set<String> names) {
        super("SavedGames");
        this.names = names;
    }

    public String toJson() {
        StringBuilder sb = new StringBuilder();
        sb.append("{ \"name\": \"SavedGames\", ");
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

    /**
     * Generator of SavedGames from a json string
     * @param json json string from which generate returned object
     * @return Optional of SavedGames, empty if json string was not coherent
     */
    public static Optional<SavedGames> fromJson(String json) {
        SavedGames sg;
        try {
            Gson gson = new Gson();
            sg = gson.fromJson(json, SavedGames.class);
        }catch (JsonSyntaxException e){
            return Optional.empty();
        }
        if(!"SavedGames".equals(sg.name) || sg.names == null){
            return Optional.empty();
        }
        return Optional.of(sg);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SavedGames that = (SavedGames) o;
        return names.equals(that.names);
    }

}
