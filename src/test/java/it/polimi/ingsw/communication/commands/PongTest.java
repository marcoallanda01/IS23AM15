package it.polimi.ingsw.communication.commands;

import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class PongTest {

    @Test
    void fromJson() {
        Pong p = new Pong("test");
        assertEquals(p, Pong.fromJson("{\"name\":\"Pong\", \"id\":\"test\"}").get());
        assertNotEquals(p, Pong.fromJson("{\"name\":\"Pong\", \"id\":\"boh\"}").get());

        assertEquals(Optional.empty(), Pong.fromJson("kjdsvaskd"));
        assertEquals(Optional.empty(), Pong.fromJson("{\"answer\":true}"));
        assertEquals(Optional.empty(), Pong.fromJson("{\"idFirstPlayer\":\"test\"}"));
        assertEquals(Optional.empty(), Pong.fromJson("{\"name\":\"PickTilesCommand\"}"));
    }
}