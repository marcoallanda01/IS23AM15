package it.polimi.ingsw.communication.commands;

import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class HelloCommandTest {

    @Test
    void fromJson() {
        HelloCommand hc = new HelloCommand();
        assertEquals(hc, HelloCommand.fromJson("{\"name\":\"HelloCommand\"}").get());
        assertFalse(hc.equals(Disconnect.fromJson("{\"name\":\"Disconnect\", \"id\":\"test\"}").get()));

        assertEquals(Optional.empty(), HelloCommand.fromJson("kjdsvaskd"));
        assertEquals(Optional.empty(), HelloCommand.fromJson("{\"name\":\"PickTilesCommand\"}"));
    }
}