package it.polimi.ingsw.communication.commands;

import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class LoadGameTest {

    @Test
    void fromJson() {
        String idFirstPlayer = "test";

        String json = "{\"name\":\"LoadGame\",\"idFirstPlayer\":\""+idFirstPlayer+"\"}";
        System.out.println(json);
        System.out.println(new LoadGame(idFirstPlayer).toJson());
        assertEquals(Optional.of(new LoadGame(idFirstPlayer)), LoadGame.fromJson(json));
        json = "{\"name\":\"GetSavedGames\",\"idFirstPlayer\":\""+idFirstPlayer+"\"}";
        assertNotEquals(Optional.of(new LoadGame("boh")), LoadGame.fromJson(json));


        assertEquals(Optional.empty(),LoadGame.fromJson("kjdsvaskd"));
        assertEquals(Optional.empty(),LoadGame.fromJson("{\"answer\":true}"));
        assertEquals(Optional.empty(),LoadGame.fromJson("{\"idFirstPlayer\":\"test\"}"));
        assertEquals(Optional.empty(),LoadGame.fromJson("{\"name\":\"GetSavedGames\"}"));
        assertEquals(Optional.empty(),LoadGame.fromJson("{\"name\":\"BooleanResponse\", \"result\":true}"));
    }
}