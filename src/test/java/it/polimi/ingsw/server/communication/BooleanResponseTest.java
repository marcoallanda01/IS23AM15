package it.polimi.ingsw.server.communication;

import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class BooleanResponseTest {

    @Test
    void fromJson() throws Exception {
        it.polimi.ingsw.server.communication.JsonString json1 = new it.polimi.ingsw.server.communication.JsonString("{\"name\":\"BooleanResponse\", \"result\":true}");
        it.polimi.ingsw.server.communication.JsonString json2 = new it.polimi.ingsw.server.communication.JsonString("{\"name\":\"BooleanResponse\", \"result\":false}");
        assertEquals(Optional.of(new it.polimi.ingsw.server.communication.BooleanResponse(true)), it.polimi.ingsw.server.communication.BooleanResponse.fromJson(json1.getJson()));
        assertEquals(Optional.of(new it.polimi.ingsw.server.communication.BooleanResponse(false)), it.polimi.ingsw.server.communication.BooleanResponse.fromJson(json2.getJson()));
        assertEquals(Optional.empty(), it.polimi.ingsw.server.communication.BooleanResponse.fromJson("kjdsvaskd"));
        assertEquals(Optional.empty(), it.polimi.ingsw.server.communication.BooleanResponse.fromJson("{\"answer\":true}"));
        assertEquals(Optional.empty(), it.polimi.ingsw.server.communication.BooleanResponse.fromJson("{\"result\":true}"));
        assertEquals(Optional.empty(), it.polimi.ingsw.server.communication.BooleanResponse.fromJson("{\"name\":\"Response\", \"answer\":true}"));
    }
}