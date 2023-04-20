package it.polimi.ingsw.communication;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BooleanResponseTest {

    @Test
    void toJson() {

        BooleanResponse br = new BooleanResponse(true);
        JsonString json1 = new JsonString("{\"name\":\"BooleanResponse\", \"result\":true}");
        assertEquals(json1, new JsonString(br.toJson()));
        br = new BooleanResponse(false);
        JsonString json2 = new JsonString("{\"name\":\"BooleanResponse\", \"result\":false}");
        assertEquals(json2, new JsonString(br.toJson()));
    }

    @Test
    void fromJson() {
        JsonString json1 = new JsonString("{\"name\":\"BooleanResponse\", \"result\":true}");
        JsonString json2 = new JsonString("{\"name\":\"BooleanResponse\", \"result\":false}");
        assertEquals(new BooleanResponse(true), BooleanResponse.fromJson(json1.getJson()));
        assertEquals(new BooleanResponse(false), BooleanResponse.fromJson(json2.getJson()));
        assertNull(BooleanResponse.fromJson("kjdsvaskd"));
        assertNull(BooleanResponse.fromJson("{\"answer\":true}"));
        assertNull(BooleanResponse.fromJson("{\"result\":true}"));
        //System.out.println(BooleanResponse.fromJson("{\"name\":\"BooleanResponse\", \"answer\":true}").toJson());
        assertNull(BooleanResponse.fromJson("{\"name\":\"Response\", \"answer\":true}"));
    }
}