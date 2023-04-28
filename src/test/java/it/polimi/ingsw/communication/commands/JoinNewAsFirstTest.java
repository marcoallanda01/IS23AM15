package it.polimi.ingsw.communication.commands;

import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class JoinNewAsFirstTest {

    @Test
    void fromJson() {
        JoinNewAsFirst jnfp = new JoinNewAsFirst("test", 3, "id", false);

        assertEquals(jnfp, JoinNewAsFirst.fromJson("{\"name\":\"JoinNewAsFirst\"," +
                "\"player\":\"test\"," +
                "\"numOfPlayers\":3," +
                "\"idFirstPlayer\":\"id\"," +
                "\"easyRule\":false" +
                "}"
        ).get());

        assertEquals(Optional.empty(), JoinNewAsFirst.fromJson("{\"name\":\"JoinNewAsFirst\"," +
                "\"player\":\"test\"," +
                "\"numOfPlayers\":3," +
                "\"easyRule\":false" +
                "}"
        ));

        assertEquals(Optional.empty(), JoinNewAsFirst.fromJson("{\"name\":\"JoinNewAsFirst\"," +
                "\"numOfPlayers\":3," +
                "\"idFirstPlayer\":\"id\"," +
                "\"easyRule\":false" +
                "}"
        ));

        assertEquals(Optional.empty(), JoinNewAsFirst.fromJson("{\"name\":\"Nope\"," +
                "\"player\":\"test\"," +
                "\"numOfPlayers\":3," +
                "\"idFirstPlayer\":\"id\"," +
                "\"easyRule\":false" +
                "}"
        ));

        assertFalse(jnfp.equals(JoinNewAsFirst.fromJson("{\"name\":\"JoinNewAsFirst\"," +
                "\"player\":\"test\"," +
                "\"numOfPlayers\":4," +
                "\"idFirstPlayer\":\"id\"," +
                "\"easyRule\":false" +
                "}"
        )));
        assertFalse(jnfp.equals(JoinNewAsFirst.fromJson("{\"name\":\"JoinNewAsFirst\"," +
                "\"player\":\"no\"," +
                "\"numOfPlayers\":3," +
                "\"idFirstPlayer\":\"id\"," +
                "\"easyRule\":false" +
                "}"
        )));
        assertFalse(jnfp.equals(JoinNewAsFirst.fromJson("{\"name\":\"JoinNewAsFirst\"," +
                "\"player\":\"test\"," +
                "\"numOfPlayers\":3," +
                "\"idFirstPlayer\":\"id3\"," +
                "\"easyRule\":false" +
                "}"
        )));
        assertFalse(jnfp.equals(JoinNewAsFirst.fromJson("{\"name\":\"JoinNewAsFirst\"," +
                "\"player\":\"test\"," +
                "\"numOfPlayers\":3," +
                "\"idFirstPlayer\":\"id\"," +
                "\"easyRule\":true" +
                "}"
        )));
        assertTrue(jnfp.equals(JoinNewAsFirst.fromJson("{\"name\":\"JoinNewAsFirst\"," +
                "\"player\":\"test\"," +
                "\"numOfPlayers\":3," +
                "\"idFirstPlayer\":\"id\"," +
                "\"easyRule\":false" +
                "}"
        ).get()));

        assertFalse(jnfp.equals(Disconnect.fromJson("{\"name\":\"Disconnect\", \"id\":\"boh\"}").get()));

        assertEquals(Optional.empty(), JoinNewAsFirst.fromJson("kjdsvaskd"));
        assertEquals(Optional.empty(),JoinNewAsFirst.fromJson("{\"answer\":true}"));
        assertEquals(Optional.empty(),JoinNewAsFirst.fromJson("{\"idFirstPlayer\":\"test\"}"));
        assertEquals(Optional.empty(),JoinNewAsFirst.fromJson("{\"name\":\"PickTilesCommand\"}"));
    }
}