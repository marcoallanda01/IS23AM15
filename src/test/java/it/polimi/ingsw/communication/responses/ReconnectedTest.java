package it.polimi.ingsw.communication.responses;

import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class ReconnectedTest {

    @Test
    void fromJson() {
        assertEquals(Optional.of(new Reconnected("test")), Reconnected.fromJson("{\"name\":\"Reconnected\", \"player\":\"test\"}"));

        assertEquals(Optional.empty(), Reconnected.fromJson("{\"name\":\"Reconnected\"}"));
        assertEquals(Optional.empty(),Reconnected.fromJson("kjdsvaskd"));
        assertEquals(Optional.empty(),Reconnected.fromJson("{\"answer\":true}"));
        assertEquals(Optional.empty(),Reconnected.fromJson("{\"player\":\"test\"}"));
        assertEquals(Optional.empty(),Reconnected.fromJson("{\"name\":\"BooleanResponse\", \"result\":true}"));
    }
}