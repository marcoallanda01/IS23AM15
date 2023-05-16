package it.polimi.ingsw.communication.commands;

import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class SaveGameTest {

    @Test
    void fromJson() {

        String json = "{\"name\":\"SaveGame\", \"id\":\"test\", \"game\":\"gioco\"}";
        System.out.println(json);
        System.out.println(new SaveGame("test", "gioco").toJson());
        assertEquals(Optional.of(new SaveGame("test", "gioco")), SaveGame.fromJson(json));
        assertNotEquals(Optional.of(new SaveGame("boh", "gioco")), SaveGame.fromJson(json));
        assertNotEquals(Optional.of(new SaveGame("test", "boh")), SaveGame.fromJson(json));

        assertFalse(new SaveGame("test", "gioco").equals(3));

        assertEquals(Optional.empty(),SaveGame.fromJson("kjdsvaskd"));
        assertEquals(Optional.empty(),SaveGame.fromJson("{\"name\":\"SaveGame\"}"));
        assertEquals(Optional.empty(),SaveGame.fromJson("{\"name\":\"SaveGame\", \"id\":\"test\"}"));
        assertEquals(Optional.empty(),SaveGame.fromJson("{\"name\":\"SaveGame\", \"game\":\"gioco\"}"));
        assertEquals(Optional.empty(),SaveGame.fromJson("{\"name\":\"BooleanResponse\", \"result\":true}"));
    }
}