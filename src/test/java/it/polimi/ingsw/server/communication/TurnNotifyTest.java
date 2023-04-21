package it.polimi.ingsw.server.communication;

import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class TurnNotifyTest {

    @Test
    void fromJson() {

        assertEquals(Optional.of(new TurnNotify("test")), TurnNotify.fromJson("{\"name\":\"TurnNotify\", \"player\":\"test\"}"));

        assertEquals(Optional.empty(), TurnNotify.fromJson("{\"name\":\"TurnNotify\"}"));
        assertEquals(Optional.empty(),TurnNotify.fromJson("kjdsvaskd"));
        assertEquals(Optional.empty(),TurnNotify.fromJson("{\"answer\":true}"));
        assertEquals(Optional.empty(),TurnNotify.fromJson("{\"player\":\"test\"}"));
        assertEquals(Optional.empty(),TurnNotify.fromJson("{\"name\":\"BooleanResponse\", \"result\":true}"));
    }
}