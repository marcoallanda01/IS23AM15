package it.polimi.ingsw.server.communication;

import it.polimi.ingsw.server.communication.responses.JsonString;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JsonStringTest {

    @Test
    void JsonStringConstruction(){
        assertThrows(Exception.class , ()->{new JsonString("{}sda");});
    }

    @Test
    void getJsonTest() throws Exception {
        assertEquals("{\"casa\":true}", new JsonString("{\"casa\":true}").getJson());
    }

    @Test
    void toStringTest() throws Exception {
        assertEquals("{\"casa\":true}",""+new JsonString("{\"casa\":true}")+"");
    }

    @Test
    void equalsTest() throws Exception {
        assertFalse(new JsonString("{}").equals("ciao"));
        assertTrue(new JsonString("{\"ciao\":false}").equals(new JsonString("{\"ciao\": false}")));
    }

}