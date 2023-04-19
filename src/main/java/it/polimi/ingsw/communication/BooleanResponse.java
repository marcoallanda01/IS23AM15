package it.polimi.ingsw.communication;

import com.google.gson.Gson;

public class BooleanResponse extends Msg{
    public boolean result;

    public BooleanResponse(boolean result) {
        super("BooleanResponse");
        this.result = result;
    }
    public String toJson() {
        return toMsgJson("{\"result\":" + result + "}");
    }

    public BooleanResponse fromJson(String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, BooleanResponse.class);
    }
}
