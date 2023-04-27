package it.polimi.ingsw.communication.responses;

import it.polimi.ingsw.communication.responses.BooleanResponse;
import it.polimi.ingsw.communication.JsonString;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class BooleanResponseTest {

    @Test
    void fromJson() throws Exception {
        JsonString json1 = new JsonString("{\"name\":\"BooleanResponse\", \"result\":true}");
        JsonString json2 = new JsonString("{\"name\":\"BooleanResponse\", \"result\":false}");
        assertEquals(Optional.of(new BooleanResponse(true)), BooleanResponse.fromJson(json1.getJson()));
        assertEquals(Optional.of(new BooleanResponse(false)), BooleanResponse.fromJson(json2.getJson()));
        assertEquals(Optional.empty(), BooleanResponse.fromJson("kjdsvaskd"));
        assertEquals(Optional.empty(), BooleanResponse.fromJson("{\"answer\":true}"));
        assertEquals(Optional.empty(), BooleanResponse.fromJson("{\"result\":true}"));
        assertEquals(Optional.empty(), BooleanResponse.fromJson("{\"name\":\"Response\", \"answer\":true}"));
    }
}