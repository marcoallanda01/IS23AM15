package it.polimi.ingsw.communication;

import com.google.gson.Gson;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class LoadGameResponse extends Msg{
    public boolean result;
    public Optional<String> error;


    public LoadGameResponse(boolean result, @NotNull String error) {
        super("LoadGameResponse");
        this.result = result;
        this.error = Optional.of(error);
    }
    public LoadGameResponse(boolean result) {
        super("LoadGameResponse");
        this.result = result;
        this.error = Optional.empty();
    }

    public LoadGameResponse fromJson(String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, LoadGameResponse.class);
    }

    public String toJson() {
        String json = "{\"result\": " + result;
        if (error.isPresent()) {
            json += ", \"error\": \"" + error.get() + "\"";
        }
        json += "}";
        return toMsgJson(json);
    }
}
