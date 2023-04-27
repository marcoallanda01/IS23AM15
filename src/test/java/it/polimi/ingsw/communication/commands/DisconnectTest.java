package it.polimi.ingsw.communication.commands;

import it.polimi.ingsw.communication.JsonString;
import it.polimi.ingsw.communication.responses.Hello;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class DisconnectTest {

    @Test
    void fromJson() {
        Disconnect d = new Disconnect("test");
        assertEquals(d, Disconnect.fromJson("{\"name\":\"Disconnect\", \"id\":\"test\"}").get());
        assertNotEquals(d, Disconnect.fromJson("{\"name\":\"Disconnect\", \"id\":\"boh\"}").get());

        assertEquals(Optional.empty(), Disconnect.fromJson("kjdsvaskd"));
        assertEquals(Optional.empty(),Disconnect.fromJson("{\"answer\":true}"));
        assertEquals(Optional.empty(),Disconnect.fromJson("{\"idFirstPlayer\":\"test\"}"));
        assertEquals(Optional.empty(),Disconnect.fromJson("{\"name\":\"PickTilesCommand\"}"));
    }
}