package it.polimi.ingsw.communication;

import com.google.gson.*;
import org.jetbrains.annotations.NotNull;

public class JsonString {
    protected String json;


    /**
     * Json string container used in tests
     * @param json json string
     * @throws Exception JsonSyntaxException
     */
    public @Deprecated JsonString(@NotNull String json) throws Exception{
        try {
            JsonParser.parseString(json);
        }catch (JsonSyntaxException e){
            throw new Exception("JsonSyntaxException was thrown");
        }

        this.json = json;
    }


    /**
     * Get json string
     * @return json string
     */
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
        JsonElement thatJE;
        JsonElement thisJE;
        thatJE = parser.parse(((JsonString) o).json);
        thisJE = parser.parse(this.json);
        return thisJE.equals(thatJE);
    }
}
