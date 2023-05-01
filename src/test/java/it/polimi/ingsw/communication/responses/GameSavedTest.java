package it.polimi.ingsw.communication.responses;

import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class GameSavedTest {
    @Test
    void fromJson() {
        GameSaved gs = new GameSaved();
        assertEquals(gs, GameSaved.fromJson("{\"name\":\"GameSaved\"}").get());
        assertFalse(gs.equals(BooleanResponse.fromJson("{\"name\":\"BooleanResponse\"}").get()));

        assertEquals(Optional.empty(), GameSaved.fromJson("kjdsvaskd"));
        assertEquals(Optional.empty(), GameSaved.fromJson("{\"name\":\"PickTilesCommand\"}"));
    }
}