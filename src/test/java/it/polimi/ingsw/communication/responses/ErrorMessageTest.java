package it.polimi.ingsw.communication.responses;

import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class ErrorMessageTest {

    @Test
    void fromJson() {
        ErrorMessage em = new ErrorMessage("test");
        assertEquals(em, ErrorMessage.fromJson("{\"name\":\"ErrorMessage\", \"message\":\"test\"}").get());
        assertFalse(em.equals(BooleanResponse.fromJson("{\"name\":\"BooleanResponse\"}").get()));
        assertNotEquals(em, ErrorMessage.fromJson("{\"name\":\"ErrorMessage\", \"message\":\"test1\"}").get());

        assertEquals(Optional.empty(), ErrorMessage.fromJson("kjdsvaskd"));
        assertEquals(Optional.empty(), ErrorMessage.fromJson("{\"name\":\"ErrorMessage\"}"));
        assertEquals(Optional.empty(), ErrorMessage.fromJson("{\"name\":\"PickTilesCommand\"}"));

    }
}