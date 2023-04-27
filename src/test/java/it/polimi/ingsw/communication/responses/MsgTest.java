package it.polimi.ingsw.communication.responses;

import it.polimi.ingsw.communication.commands.Command;
import it.polimi.ingsw.communication.responses.BooleanResponse;
import it.polimi.ingsw.communication.responses.Hello;
import it.polimi.ingsw.communication.JsonString;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class MsgTest {

    @Test
    void toJson() throws Exception {
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
        assertEquals("Hello", h.getName());
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