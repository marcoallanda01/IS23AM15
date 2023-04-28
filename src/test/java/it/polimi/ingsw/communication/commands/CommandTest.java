package it.polimi.ingsw.communication.commands;

import it.polimi.ingsw.communication.JsonString;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class CommandTest {

    @Test
    void toJson() throws Exception {
        GetLoadedPlayers glp = new GetLoadedPlayers();
        JsonString json1 = new JsonString("{\"name\":\"GetLoadedPlayers\"}");
        System.out.println(glp.toJson());
        assertEquals(json1, new JsonString(glp.toJson()));
    }

    @Test
    void getName() {
        String id = "test";
        GetLoadedPlayers glp = new GetLoadedPlayers();
        assertEquals("GetLoadedPlayers", glp.getName());
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