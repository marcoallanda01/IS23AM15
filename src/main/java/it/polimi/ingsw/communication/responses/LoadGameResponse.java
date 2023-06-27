package it.polimi.ingsw.communication.responses;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import it.polimi.ingsw.server.controller.exceptions.GameLoadException;
import it.polimi.ingsw.server.controller.exceptions.GameNameException;
import it.polimi.ingsw.server.controller.exceptions.IllegalLobbyException;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.Optional;

/**
 * Response message of a LoadGame command
 */
public class LoadGameResponse extends Msg {
    /**
     * Whether the game was loaded or not
     */
    public boolean result;
    /**
     * Error that occurred
     */
    public String error;

    /**
     * Unsuccessful LoadGameResponse constructor
     *
     * @param error error occurred
     */
    public LoadGameResponse(@NotNull GameLoadException error) {
        super("LoadGameResponse");
        this.result = false;
        this.error = error.getClass().getSimpleName();
    }

    /**
     * Unsuccessful LoadGameResponse constructor
     *
     * @param error error occurred
     */
    public LoadGameResponse(@NotNull GameNameException error) {
        super("LoadGameResponse");
        this.result = false;
        this.error = error.getClass().getSimpleName();
    }

    /**
     * Unsuccessful LoadGameResponse constructor
     *
     * @param error error occurred
     */
    public LoadGameResponse(@NotNull IllegalLobbyException error) {
        super("LoadGameResponse");
        this.result = false;
        this.error = error.getClass().getSimpleName();
    }

    /**
     * Successful LoadGameResponse constructor
     */
    public LoadGameResponse() {
        super("LoadGameResponse");
        this.result = true;
        this.error = null;
    }


    /**
     * Generator of LoadGameResponse from a json string
     *
     * @param json json string from which generate Hello
     * @return Optional of LoadGameResponse, empty if json string was not coherent
     */
    public static Optional<LoadGameResponse> fromJson(String json) {
        LoadGameResponse jgr;
        try {
            Gson gson = new Gson();
            jgr = gson.fromJson(json, LoadGameResponse.class);
        } catch (JsonSyntaxException e) {
            return Optional.empty();
        }
        if (!"LoadGameResponse".equals(jgr.name) || (jgr.result && jgr.error != null)
                || (!jgr.result && jgr.error == null)) {
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
