package it.polimi.ingsw.server.communication.responses;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.util.Optional;

public class BooleanResponse extends Msg {
    public boolean result;

    public BooleanResponse(boolean result) {
        super("BooleanResponse");
        this.result = result;
    }

    /**
     * Generator of BooleanResponse from a json string
     * @param json json string from which generate BooleanResponse
     * @return Optional of BooleanResponse, empty if json string was not coherent
     */
    public static Optional<BooleanResponse> fromJson(String json) {
        BooleanResponse br;
        try{
            Gson gson = new Gson();
            br = gson.fromJson(json, BooleanResponse.class);
        }
        catch (JsonSyntaxException e){
            return Optional.empty();
        }
        if(!"BooleanResponse".equals(br.name)){
            return Optional.empty();
        }
        return Optional.of(br);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BooleanResponse that = (BooleanResponse) o;
        return result == that.result;
    }
}
