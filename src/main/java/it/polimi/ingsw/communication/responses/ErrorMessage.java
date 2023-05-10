package it.polimi.ingsw.communication.responses;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.Optional;

public class ErrorMessage extends Msg{
    public String message;

    public ErrorMessage(@NotNull String message){
        super("ErrorMessage");
        this.message = message;
    }

    /**
     * Generator of ErrorMessage from a json string
     * @param json json string from which generate object
     * @return Optional of ErrorMessage, empty if json string was not coherent
     */
    public static Optional<ErrorMessage> fromJson(String json) {
        ErrorMessage em;
        try{
            Gson gson = new Gson();
            em = gson.fromJson(json, ErrorMessage.class);
        }
        catch (JsonSyntaxException e){
            return Optional.empty();
        }
        if(!"ErrorMessage".equals(em.name) || em.message == null){
            return Optional.empty();
        }
        return Optional.of(em);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ErrorMessage errorMessage = (ErrorMessage) o;
        return Objects.equals(message, errorMessage.message);
    }
}
