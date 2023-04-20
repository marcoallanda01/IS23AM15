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
     * @return name type of the message
     */
    public String getName(){
        return this.name;
    }
}
