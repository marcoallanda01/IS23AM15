package it.polimi.ingsw.communication;

import com.google.gson.*;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class JsonString {
    protected String json;

    public JsonString(@NotNull String json){
        /*try {
            /*this.json = JsonParser.parseString(json).j;
        }*/

        this.json = json;
    }



    public String getJson() {
        return json;
    }

    @Override
    public String toString() {
        return this.json;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JsonParser parser = new JsonParser();
        JsonElement thatJE = parser.parse(((JsonString) o).json);
        JsonElement thisJE = parser.parse(this.json);
        return thisJE.equals(thatJE);
    }
}
