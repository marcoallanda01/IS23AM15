package it.polimi.ingsw.communication.commands;

import it.polimi.ingsw.communication.JsonString;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class GameCommandTest {

    @Test
    void toJson() throws Exception {
        Disconnect d = new Disconnect("test");
        JsonString json1 = new JsonString("{\"name\":\"Disconnect\", \"id\":\"test\"}");
        assertEquals(json1, new JsonString(d.toJson()));
    }

    @Test
    void getId() {
        String id = "test";
        Disconnect d = new Disconnect(id);
        assertEquals("test", d.getId());
    }
}
