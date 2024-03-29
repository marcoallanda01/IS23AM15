package it.polimi.ingsw.communication.commands;

import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class JoinTest {

    @Test
    void fromJson() {
        Join hc = new Join("test");
        assertEquals(hc, Join.fromJson("{\"name\":\"Join\", \"player\":\"test\"}").get());
        assertFalse(hc.equals(Disconnect.fromJson("{\"name\":\"Disconnect\", \"id\":\"test\"}").get()));

        assertEquals(Optional.empty(), Join.fromJson("{\"name\":\"Join\"}"));
        assertEquals(Optional.empty(), Join.fromJson("{\"player\":\"test\"}"));
        assertEquals(Optional.empty(), Join.fromJson("kjdsvaskd"));
        assertEquals(Optional.empty(), Join.fromJson("{\"name\":\"PickTilesCommand\"}"));
    }
}