package it.polimi.ingsw.communication.responses;


import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import it.polimi.ingsw.server.controller.exceptions.NicknameException;
import it.polimi.ingsw.server.controller.exceptions.NicknameTakenException;
import it.polimi.ingsw.server.controller.exceptions.FullGameException;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.Optional;

public class JoinResponse extends Msg {
    public boolean result;
    public String id;
    public String error;

    public JoinResponse(@NotNull FullGameException error) {
        super("JoinResponse");
        this.result = false;
        this.error = error.getClass().getSimpleName();
        this.id = null;
    }

    public JoinResponse(@NotNull NicknameTakenException error) {
        super("JoinResponse");
        this.result = false;
        this.error = error.getClass().getSimpleName();
        this.id = null;
    }

    public JoinResponse(@NotNull NicknameException error) {
        super("JoinResponse");
        this.result = false;
        this.error = error.getClass().getSimpleName();
        this.id = null;
    }

    public JoinResponse(@NotNull String id) {
        super("JoinResponse");
        this.result = true;
        this.id = id;
        this.error = null;
    }

    /**
     * Generator of JoinResponse from a json string
     * @param json json string from which generate returned object
     * @return Optional of JoinResponse, empty if json string was not coherent
     */
    public static Optional<JoinResponse> fromJson(String json) {
        JoinResponse jr;
        try {
            Gson gson = new Gson();
            jr = gson.fromJson(json, JoinResponse.class);
        }catch (JsonSyntaxException e){
            return Optional.empty();
        }
        if(!"JoinResponse".equals(jr.name) || (jr.result && (jr.id == null || jr.error != null))
                || (!jr.result && (jr.error == null || jr.id != null))){
            return Optional.empty();
        }
        return Optional.of(jr);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JoinResponse that = (JoinResponse) o;
        return result == that.result && Objects.equals(id, that.id) && Objects.equals(error, that.error);
    }
}
