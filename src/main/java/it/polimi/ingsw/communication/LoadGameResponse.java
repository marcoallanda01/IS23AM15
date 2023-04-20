package it.polimi.ingsw.communication;

import com.google.gson.Gson;
import it.polimi.ingsw.controller.GameLoadException;
import it.polimi.ingsw.controller.GameNameException;
import it.polimi.ingsw.controller.IllegalLobbyException;
import org.jetbrains.annotations.NotNull;

public class LoadGameResponse extends Msg{
    public boolean result;
    public String error;

    // throws GameLoadException, GameNameException, IllegalLobbyException;
    public LoadGameResponse(@NotNull GameLoadException error) {
        super("LoadGameResponse");
        this.result = false;
        this.error = error.getClass().getName();
    }
    public LoadGameResponse(@NotNull GameNameException error) {
        super("LoadGameResponse");
        this.result = false;
        this.error = error.getClass().getName();
    }
    public LoadGameResponse(@NotNull IllegalLobbyException error) {
        super("LoadGameResponse");
        this.result = false;
        this.error = error.getClass().getName();
    }
    public LoadGameResponse() {
        super("LoadGameResponse");
        this.result = true;
        this.error = null;
    }

    public LoadGameResponse fromJson(String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, LoadGameResponse.class);
    }
}
