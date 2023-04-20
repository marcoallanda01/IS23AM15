package it.polimi.ingsw.communication;

import com.google.gson.Gson;

import javax.swing.*;

public abstract class Msg{
    protected String name;

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
     * Method used to wrap payload of message with the name of the message
     * @param jsonObj payload
     * @return wrapped json mgs string to send = "{"name":"ExampleObj", "data":{...}}"
     */
    /*
    protected String toMsgJson(String jsonObj){

        return "{" +
                "\"name\":" +
                this.name +
                "," +
                "\"data\":" +
                jsonObj +
                "}";
    }*/

    // public abstract Msg fromJson(String json);

    /**
     * @return name type of the message
     */
    public String getName(){
        return this.name;
    }
}
