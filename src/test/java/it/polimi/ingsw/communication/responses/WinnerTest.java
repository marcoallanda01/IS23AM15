package it.polimi.ingsw.communication.responses;

import it.polimi.ingsw.communication.responses.Winner;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class WinnerTest {

    @Test
    void fromJson() {
        assertEquals(Optional.of(new Winner("test")), Winner.fromJson("{\"name\":\"Winner\", \"player\":\"test\"}"));

        assertEquals(Optional.empty(), Winner.fromJson("{\"name\":\"Winner\"}"));
        assertEquals(Optional.empty(),Winner.fromJson("kjdsvaskd"));
        assertEquals(Optional.empty(),Winner.fromJson("{\"answer\":true}"));
        assertEquals(Optional.empty(),Winner.fromJson("{\"player\":\"test\"}"));
        assertEquals(Optional.empty(),Winner.fromJson("{\"name\":\"BooleanResponse\", \"result\":true}"));
    }
}