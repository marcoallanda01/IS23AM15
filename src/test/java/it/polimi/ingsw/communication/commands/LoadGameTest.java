package it.polimi.ingsw.communication.commands;

import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class LoadGameTest {

    @Test
    void fromJson() {
        String idFirstPlayer = "test";

        String json = "{\"name\":\"LoadGame\",\"idFirstPlayer\":\""+idFirstPlayer+"\", \"game\":\"gioco\"}";
        System.out.println(json);
        System.out.println(new LoadGame(idFirstPlayer, "gioco").toJson());
        assertEquals(Optional.of(new LoadGame(idFirstPlayer, "gioco")), LoadGame.fromJson(json));
        json = "{\"name\":\"LoadGame\",\"idFirstPlayer\":\""+idFirstPlayer+"\"}";
        assertNotEquals(Optional.of(new LoadGame("boh", "gioco")), LoadGame.fromJson(json));
        assertNotEquals(Optional.of(new LoadGame("test", "boh")), LoadGame.fromJson(json));

        assertEquals(Optional.empty(),LoadGame.fromJson("kjdsvaskd"));
        assertEquals(Optional.empty(),LoadGame.fromJson("{\"answer\":true}"));
        assertEquals(Optional.empty(),LoadGame.fromJson("{\"idFirstPlayer\":\"test\"}"));
        assertEquals(Optional.empty(),LoadGame.fromJson("{\"name\":\"LoadGame\"}"));
        assertEquals(Optional.empty(),LoadGame.fromJson("{\"name\":\"LoadGame\", " +
                "\"idFirstPlayer\":\"test\"}"));
        assertEquals(Optional.empty(),LoadGame.fromJson("{\"name\":\"LoadGame\", " +
                "\"game\":\"gioco\"}"));
        assertEquals(Optional.empty(),LoadGame.fromJson("{\"name\":\"BooleanResponse\", \"result\":true}"));
    }
}