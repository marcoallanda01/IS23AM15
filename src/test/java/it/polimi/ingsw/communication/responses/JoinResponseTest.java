package it.polimi.ingsw.communication.responses;

import it.polimi.ingsw.communication.responses.JoinResponse;
import it.polimi.ingsw.server.controller.FullGameException;
import it.polimi.ingsw.server.controller.NicknameException;
import it.polimi.ingsw.server.controller.NicknameTakenException;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class JoinResponseTest {

    @Test
    void fromJson() {
        assertEquals(Optional.of(new JoinResponse("test")), JoinResponse.fromJson("{\"name\":\"JoinResponse\"," +
                "\"result\":true, \"id\":\"test\"}"));
        JoinResponse jre = new JoinResponse(new NicknameException());
        JoinResponse jra = JoinResponse.fromJson("{\"name\":\"JoinResponse\",\"result\":false," +
                "\"error\":\"NicknameException\"}").get();
        assertEquals(jre , jra);
        assertEquals(Optional.of(new JoinResponse(new NicknameTakenException())),
                JoinResponse.fromJson("{\"name\":\"JoinResponse\",\"result\":false, \"error\":\"NicknameTakenException\"}"));
        assertEquals(Optional.of(new JoinResponse(new FullGameException())),
                JoinResponse.fromJson("{\"name\":\"JoinResponse\",\"result\":false, \"error\":\"FullGameException\"}"));

        assertNotEquals(Optional.of(new JoinResponse(new FullGameException())),
                JoinResponse.fromJson("{\"name\":\"JoinResponse\",\"result\":false, \"error\":\"NicknameException\"}"));

        assertEquals(Optional.empty(),
                JoinResponse.fromJson("{\"name\":\"JoinResponse\",\"result\":true, \"error\":\"NicknameException\"}"));
        assertEquals(Optional.empty(), JoinResponse.fromJson("{\"name\":\"JoinResponse\"," +
                "\"result\":false, \"id\":\"test\"}"));

        assertEquals(Optional.empty(), JoinResponse.fromJson("{\"name\":\"JoinResponse\"}"));

        assertEquals(Optional.empty(), JoinResponse.fromJson("kjdsvaskd"));
        assertEquals(Optional.empty(), JoinResponse.fromJson("{\"answer\":true}"));

        assertEquals(Optional.empty(), JoinResponse.fromJson("{\"result\":false, \"error\":\"NicknameException\"}"));

        assertEquals(Optional.empty(), JoinResponse.fromJson("{\"name\":\"BooleanResponse\", \"result\":true}"));
    }
}