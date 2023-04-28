package it.polimi.ingsw.communication.commands;

import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class ReconnectTest {

    @Test
    void fromJson() {
        Reconnect r = new Reconnect("test");
        assertEquals(r, Reconnect.fromJson("{\"name\":\"Reconnect\", \"id\":\"test\"}").get());
        assertNotEquals(r, Reconnect.fromJson("{\"name\":\"Reconnect\", \"id\":\"boh\"}").get());

        assertEquals(Optional.empty(), Reconnect.fromJson("kjdsvaskd"));
        assertEquals(Optional.empty(),Reconnect.fromJson("{\"answer\":true}"));
        assertEquals(Optional.empty(),Reconnect.fromJson("{\"idFirstPlayer\":\"test\"}"));
        assertEquals(Optional.empty(),Reconnect.fromJson("{\"name\":\"PickTilesCommand\"}"));
    }
}