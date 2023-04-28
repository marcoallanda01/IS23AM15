package it.polimi.ingsw.communication.commands;

import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class JoinLoadedAsFirstTest {

    @Test
    void fromJson() {
        JoinLoadedAsFirst jlfp = new JoinLoadedAsFirst("test", "id");

        assertEquals(jlfp, JoinLoadedAsFirst.fromJson("{\"name\":\"JoinLoadedAsFirst\"," +
                "\"player\":\"test\"," +
                "\"idFirstPlayer\":\"id\"" +
                "}"
        ).get());

        assertEquals(Optional.empty(), JoinLoadedAsFirst.fromJson("{\"name\":\"JoinLoadedAsFirst\"," +
                "\"player\":\"test\"" +
                "}"
        ));

        assertEquals(Optional.empty(), JoinLoadedAsFirst.fromJson("{\"name\":\"JoinLoadedAsFirst\"," +
                "\"idFirstPlayer\":\"id\"" +
                "}"
        ));

        assertFalse(jlfp.equals(Disconnect.fromJson("{\"name\":\"Disconnect\", \"id\":\"boh\"}").get()));

        assertEquals(Optional.empty(), JoinLoadedAsFirst.fromJson("kjdsvaskd"));
        assertEquals(Optional.empty(), JoinLoadedAsFirst.fromJson("{\"answer\":true}"));
        assertEquals(Optional.empty(), JoinLoadedAsFirst.fromJson("{\"idFirstPlayer\":\"test\"}"));
        assertEquals(Optional.empty(), JoinLoadedAsFirst.fromJson("{\"name\":\"PickTilesCommand\"}"));
    }
}