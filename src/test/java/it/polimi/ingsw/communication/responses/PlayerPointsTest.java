package it.polimi.ingsw.communication.responses;

import it.polimi.ingsw.communication.responses.PlayerPoints;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class PlayerPointsTest {

    @Test
    void fromJson() {
        assertEquals(Optional.of(new PlayerPoints("test", 4)),
                PlayerPoints.fromJson("{\"name\":\"PlayerPoints\", \"player\":\"test\", \"points\":4}"));
        assertEquals(Optional.of(new PlayerPoints("test", 0)),
                PlayerPoints.fromJson("{\"name\":\"PlayerPoints\", \"player\":\"test\", \"points\":0}"));

        assertEquals(Optional.empty(),
                PlayerPoints.fromJson("{\"name\":\"PlayerPoints\", \"player\":\"test\", \"points\":-1}"));
        assertEquals(Optional.empty(), PlayerPoints.fromJson("{\"name\":\"PlayerPoints\"}"));
        assertEquals(Optional.empty(),PlayerPoints.fromJson("kjdsvaskd"));
        assertEquals(Optional.empty(),PlayerPoints.fromJson("{\"answer\":true}"));
        assertEquals(Optional.empty(),PlayerPoints.fromJson("{\"player\":\"test\"}"));
        assertEquals(Optional.empty(),PlayerPoints.fromJson("{\"points\":3}"));
        assertEquals(Optional.empty(),PlayerPoints.fromJson("{\"player\":\"test\", \"points\":3}"));
        assertEquals(Optional.empty(),PlayerPoints.fromJson("{\"name\":\"BooleanResponse\", \"result\":true}"));
    }
}