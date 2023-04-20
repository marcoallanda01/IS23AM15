package it.polimi.ingsw.server.communication;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JsonStringTest {

    @Test
    void JsonStringConstruction(){
        assertThrows(Exception.class , ()->{new it.polimi.ingsw.server.communication.JsonString("{}sda");});
    }

    @Test
    void getJsonTest() throws Exception {
        assertEquals("{\"casa\":true}", new it.polimi.ingsw.server.communication.JsonString("{\"casa\":true}").getJson());
    }

    @Test
    void toStringTest() throws Exception {
        assertEquals("{\"casa\":true}",""+new it.polimi.ingsw.server.communication.JsonString("{\"casa\":true}")+"");
    }

    @Test
    void equalsTest() throws Exception {
        assertFalse(new it.polimi.ingsw.server.communication.JsonString("{}").equals("ciao"));
        assertTrue(new it.polimi.ingsw.server.communication.JsonString("{\"ciao\":false}").equals(new it.polimi.ingsw.server.communication.JsonString("{\"ciao\": false}")));
    }

}