package it.polimi.ingsw.communication.responses;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import java.io.Serializable;
import java.util.Optional;

public abstract class Msg implements Serializable {
    protected String name;

    /**
     * Message constructor
     * @param name name of the message
     */
    public Msg(String name){
        this.name = name;
    }

    /**
     * Transform the object in a json string
     * @return json string representing the object
     */
    public String toJson() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    /**
     * Get the name from a json string
     * @param json json string
     * @return name of the message
     */
    public static Optional<String> nameFromJson(String json){
        JsonObject jo;
        Optional<String> name;
        try{
            Gson gson = new Gson();
            jo = gson.fromJson(json, JsonObject.class);
        }
        catch (JsonSyntaxException e){
            return Optional.empty();
        }
        if(jo.has("name")){
            name = Optional.of(jo.get("name").getAsString());
        }else{
            name = Optional.empty();
        }
        return name;
    }

    /**
     * @return name type of the message
     */
    public String getName(){
        return this.name;
    }
}
