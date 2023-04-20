package it.polimi.ingsw.communication;


import com.google.gson.Gson;
import it.polimi.ingsw.controller.FullGameException;
import it.polimi.ingsw.controller.NicknameException;
import it.polimi.ingsw.controller.NicknameTakenException;
import org.jetbrains.annotations.NotNull;

public class JoinResponse extends Msg{
    public boolean result;
    public String id;
    public String error;

    public JoinResponse(@NotNull FullGameException error) {
        super("JoinResponse");
        this.result = false;
        this.error = error.getClass().getName();
        this.id = null;
    }
    public JoinResponse(@NotNull NicknameTakenException error) {
        super("JoinResponse");
        this.result = false;
        this.error = error.getClass().getName();
        this.id = null;
    }
    public JoinResponse(@NotNull NicknameException error) {
        super("JoinResponse");
        this.result = false;
        this.error = error.getClass().getName();
        this.id = null;
    }
    public JoinResponse(@NotNull String id) {
        super("JoinResponse");
        this.result = true;
        this.id = id;
        this.error = null;
    }

    public JoinResponse fromJson(String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, JoinResponse.class);
    }
}
