package it.polimi.ingsw.communication.responses;

import it.polimi.ingsw.communication.commands.Pong;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class PingTest {

    @Test
    void fromJson() {
        Ping p = new Ping();
        assertEquals(p, Ping.fromJson("{\"name\":\"Ping\"}").get());
        assertFalse(p.equals(BooleanResponse.fromJson("{\"name\":\"BooleanResponse\"}").get()));

        assertEquals(Optional.empty(), Ping.fromJson("kjdsvaskd"));
        assertEquals(Optional.empty(), Ping.fromJson("{\"name\":\"PickTilesCommand\"}"));
    }
}