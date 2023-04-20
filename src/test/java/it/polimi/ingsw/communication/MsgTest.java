package it.polimi.ingsw.communication;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MsgTest {

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
    void getName() {
        Hello h = new Hello("test");
        assertEquals("Hello", h.name);
    }
}