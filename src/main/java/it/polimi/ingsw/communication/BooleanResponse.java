package it.polimi.ingsw.communication;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;

public class BooleanResponse extends Msg {
    public boolean result;

    public BooleanResponse(boolean result) {
        super("BooleanResponse");
        this.result = result;
    }

    public String toJson() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    public static BooleanResponse fromJson(String json) {
        BooleanResponse br;
        try{
            Gson gson = new Gson();
            br = gson.fromJson(json, BooleanResponse.class);
        }
        catch (JsonSyntaxException e){
            return null;
        }
        if(br.name == null || !br.name.equals("BooleanResponse")){
            br = null;
        }
        return br;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BooleanResponse that = (BooleanResponse) o;
        return result == that.result;
    }
}
