package it.polimi.ingsw.server.communication;

import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class DisconnectionTest {

    @Test
    void fromJson() {
        assertEquals(Optional.of(new Disconnection("test")), Disconnection.fromJson("{\"name\":\"Disconnection\", \"player\":\"test\"}"));

        assertEquals(Optional.empty(), Disconnection.fromJson("{\"name\":\"Disconnection\"}"));
        assertEquals(Optional.empty(),Disconnection.fromJson("kjdsvaskd"));
        assertEquals(Optional.empty(),Disconnection.fromJson("{\"answer\":true}"));
        assertEquals(Optional.empty(),Disconnection.fromJson("{\"player\":\"test\"}"));
        assertEquals(Optional.empty(),Disconnection.fromJson("{\"name\":\"BooleanResponse\", \"result\":true}"));
    }
}