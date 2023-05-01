package it.polimi.ingsw.communication.commands;

import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class SaveGameTest {

    @Test
    void fromJson() {

        String json = "{\"name\":\"SaveGame\", \"id\":\"test\"}";
        System.out.println(json);
        System.out.println(new SaveGame("test").toJson());
        assertEquals(Optional.of(new SaveGame("test")), SaveGame.fromJson(json));
        assertNotEquals(Optional.of(new SaveGame("boh")), SaveGame.fromJson(json));

        assertFalse(new SaveGame("test").equals(3));

        assertEquals(Optional.empty(),GetSavedGames.fromJson("kjdsvaskd"));
        assertEquals(Optional.empty(),GetSavedGames.fromJson("{\"name\":\"SaveGame\"}"));
        assertEquals(Optional.empty(),GetSavedGames.fromJson("{\"name\":\"BooleanResponse\", \"result\":true}"));
    }
}