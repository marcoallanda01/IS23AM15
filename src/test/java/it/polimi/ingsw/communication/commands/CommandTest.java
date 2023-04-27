package it.polimi.ingsw.communication.commands;

import it.polimi.ingsw.communication.JsonString;
import it.polimi.ingsw.communication.responses.BooleanResponse;
import it.polimi.ingsw.communication.responses.Disconnection;
import it.polimi.ingsw.communication.responses.Hello;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class CommandTest {

    @Test
    void toJson() throws Exception {
        Disconnect d = new Disconnect("test");
        JsonString json1 = new JsonString("{\"name\":\"Disconnect\", \"id\":\"test\"}");
        assertEquals(json1, new JsonString(d.toJson()));
    }

    @Test
    void getName() {
        String id = "test";
        Disconnect d = new Disconnect(id);
        assertEquals("Disconnect", d.getName());
    }

    void getId() {
        String id = "test";
        Disconnect d = new Disconnect(id);
        assertEquals("test", d.getId());
    }

    @Test
    void nameFromJson() {
        assertEquals("test", Command.nameFromJson("{\"name\":\"test\"}").get());
        assertEquals("test", Command.nameFromJson("{\"name\":\"test\", \"num\":1}").get());
        assertNotEquals("test", Command.nameFromJson("{\"name\":\"boh\", \"num\":1}").get());
        assertEquals(Optional.empty(), Command.nameFromJson("{\"num\":1}"));
        assertEquals(Optional.empty(), Command.nameFromJson("{wqeeq"));
    }
}