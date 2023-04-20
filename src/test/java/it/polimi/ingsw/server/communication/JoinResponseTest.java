package it.polimi.ingsw.server.communication;

import it.polimi.ingsw.server.controller.FullGameException;
import it.polimi.ingsw.server.controller.NicknameException;
import it.polimi.ingsw.server.controller.NicknameTakenException;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class JoinResponseTest {

    @Test
    void fromJson() {
        assertEquals(Optional.of(new it.polimi.ingsw.server.communication.JoinResponse("test")), it.polimi.ingsw.server.communication.JoinResponse.fromJson("{\"name\":\"JoinResponse\"," +
                "\"result\":true, \"id\":\"test\"}"));
        it.polimi.ingsw.server.communication.JoinResponse jre = new it.polimi.ingsw.server.communication.JoinResponse(new NicknameException());
        it.polimi.ingsw.server.communication.JoinResponse jra = it.polimi.ingsw.server.communication.JoinResponse.fromJson("{\"name\":\"JoinResponse\",\"result\":false," +
                "\"error\":\"NicknameException\"}").get();
        assertEquals(jre , jra);
        assertEquals(Optional.of(new it.polimi.ingsw.server.communication.JoinResponse(new NicknameTakenException())),
                it.polimi.ingsw.server.communication.JoinResponse.fromJson("{\"name\":\"JoinResponse\",\"result\":false, \"error\":\"NicknameTakenException\"}"));
        assertEquals(Optional.of(new it.polimi.ingsw.server.communication.JoinResponse(new FullGameException())),
                it.polimi.ingsw.server.communication.JoinResponse.fromJson("{\"name\":\"JoinResponse\",\"result\":false, \"error\":\"FullGameException\"}"));

        assertNotEquals(Optional.of(new it.polimi.ingsw.server.communication.JoinResponse(new FullGameException())),
                it.polimi.ingsw.server.communication.JoinResponse.fromJson("{\"name\":\"JoinResponse\",\"result\":false, \"error\":\"NicknameException\"}"));

        assertEquals(Optional.empty(),
                it.polimi.ingsw.server.communication.JoinResponse.fromJson("{\"name\":\"JoinResponse\",\"result\":true, \"error\":\"NicknameException\"}"));
        assertEquals(Optional.empty(), it.polimi.ingsw.server.communication.JoinResponse.fromJson("{\"name\":\"JoinResponse\"," +
                "\"result\":false, \"id\":\"test\"}"));

        assertEquals(Optional.empty(), it.polimi.ingsw.server.communication.JoinResponse.fromJson("{\"name\":\"JoinResponse\"}"));

        assertEquals(Optional.empty(), it.polimi.ingsw.server.communication.JoinResponse.fromJson("kjdsvaskd"));
        assertEquals(Optional.empty(), it.polimi.ingsw.server.communication.JoinResponse.fromJson("{\"answer\":true}"));

        assertEquals(Optional.empty(), it.polimi.ingsw.server.communication.JoinResponse.fromJson("{\"result\":false, \"error\":\"NicknameException\"}"));

        assertEquals(Optional.empty(), it.polimi.ingsw.server.communication.JoinResponse.fromJson("{\"name\":\"BooleanResponse\", \"result\":true}"));
    }
}