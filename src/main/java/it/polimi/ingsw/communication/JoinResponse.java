package it.polimi.ingsw.communication;


import com.google.gson.Gson;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

// TODO: question - è giusto che ho JoinResponse e LoadGameResponse separate? Devo fare un enum per i tipi di errori possibili?
public class JoinResponse extends Msg{
    public boolean result;
    public Optional<String> error;


    public JoinResponse(boolean result, @NotNull String error) {
        super("JoinResponse");
        this.result = result;
        this.error = Optional.of(error);
    }
    public JoinResponse(boolean result) {
        super("JoinResponse");
        this.result = result;
        this.error = Optional.empty();
    }

    public String toJson() {
        String json = "{\"result\": " + result;
        if (error.isPresent()) {
            json += ", \"error\": \"" + error.get() + "\"";
        }
        json += "}";
        return toMsgJson(json);
    }

    public JoinResponse fromJson(String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, JoinResponse.class);
    }
}
