package it.polimi.ingsw.communication.commands;

import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class GetLoadedPlayersTest {

    @Test
    void fromJson() {
        GetLoadedPlayers glp = new GetLoadedPlayers();
        assertEquals(glp, GetLoadedPlayers.fromJson("{\"name\":\"GetLoadedPlayers\"}").get());
        assertFalse(glp.equals(Disconnect.fromJson("{\"name\":\"Disconnect\", \"id\":\"test\"}").get()));

        assertEquals(Optional.empty(), GetLoadedPlayers.fromJson("kjdsvaskd"));
        assertEquals(Optional.empty(), GetLoadedPlayers.fromJson("{\"name\":\"PickTilesCommand\"}"));
    }
}