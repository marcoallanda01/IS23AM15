package it.polimi.ingsw.communication.responses;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import it.polimi.ingsw.server.controller.GameLoadException;
import it.polimi.ingsw.server.controller.GameNameException;
import it.polimi.ingsw.server.controller.IllegalLobbyException;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.Optional;

public class LoadGameResponse extends Msg {
    public boolean result;
    public String error;

    public LoadGameResponse(@NotNull GameLoadException error) {
        super("LoadGameResponse");
        this.result = false;
        this.error = error.getClass().getSimpleName();
    }
    public LoadGameResponse(@NotNull GameNameException error) {
        super("LoadGameResponse");
        this.result = false;
        this.error = error.getClass().getSimpleName();
    }
    public LoadGameResponse(@NotNull IllegalLobbyException error) {
        super("LoadGameResponse");
        this.result = false;
        this.error = error.getClass().getSimpleName();
    }
    public LoadGameResponse() {
        super("LoadGameResponse");
        this.result = true;
        this.error = null;
    }

    public static Optional<LoadGameResponse> fromJson(String json) {
        LoadGameResponse jgr;
        try {
            Gson gson = new Gson();
            jgr = gson.fromJson(json, LoadGameResponse.class);
        }catch (JsonSyntaxException e){
            return Optional.empty();
        }
        if(!"LoadGameResponse".equals(jgr.name) || (jgr.result && jgr.error != null)
                || (!jgr.result && jgr.error == null)){
            return Optional.empty();
        }
        return Optional.of(jgr);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LoadGameResponse that = (LoadGameResponse) o;
        return result == that.result && Objects.equals(error, that.error);
    }
}
