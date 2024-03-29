package it.polimi.ingsw.communication.responses;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.util.Optional;

/**
 * Response of a first join command
 */
public class FirstJoinResponse extends Msg {
    /**
     * result of the first join
     */
    public boolean result;

    /**
     * FirstJoinResponse constructor
     * @param result true if join was successful, false otherwise
     */
    public FirstJoinResponse(boolean result) {
        super("FirstJoinResponse");
        this.result = result;
    }

    /**
     * Generator of FirstJoinResponse from a json string
     * @param json json string from which generate FirstJoinResponse
     * @return Optional of FirstJoinResponse, empty if json string was not coherent
     */
    public static Optional<FirstJoinResponse> fromJson(String json) {
        FirstJoinResponse br;
        try{
            Gson gson = new Gson();
            br = gson.fromJson(json, FirstJoinResponse.class);
        }
        catch (JsonSyntaxException e){
            return Optional.empty();
        }
        if(!"FirstJoinResponse".equals(br.name)){
            return Optional.empty();
        }
        return Optional.of(br);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FirstJoinResponse that = (FirstJoinResponse) o;
        return result == that.result;
    }
}
