package it.polimi.ingsw.communication.responses;

import it.polimi.ingsw.communication.JsonString;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class MsgTest {

    @Test
    void toJson() throws Exception {
        BooleanResponse br = new BooleanResponse(true);
        JsonString json1 = new JsonString("{\"name\":\"BooleanResponse\", \"result\":true}");
        assertEquals(json1, new JsonString(br.toJson()));
        br = new BooleanResponse(false);
        JsonString json2 = new JsonString("{\"name\":\"BooleanResponse\", \"result\":false}");
        assertEquals(json2, new JsonString(br.toJson()));
    }

    @Test
    void PolymorphismCheck(){
        Msg m = SavedGames.fromJson("{\"name\":\"SavedGames\"," +
                "\"names\":[\"Come\",\"Ciao\",\"Stai\"]}").get();
        String json = m.toJson();
        System.out.println("Case1"+json);
        assertEquals("{ \"name\": \"SavedGames\", \"names\": [\"Come\",\"Ciao\",\"Stai\"]}", json);
    }

    @Test
    void getName() {
        Hello h = new Hello("test");
        assertEquals("Hello", h.getName());
    }

    @Test
    void nameFromJson() {
        assertEquals("test", Msg.nameFromJson("{\"name\":\"test\"}").get());
        assertEquals("test", Msg.nameFromJson("{\"name\":\"test\", \"num\":1}").get());
        assertNotEquals("test", Msg.nameFromJson("{\"name\":\"boh\", \"num\":1}").get());
        assertEquals(Optional.empty(), Msg.nameFromJson("{\"num\":1}"));
        assertEquals(Optional.empty(), Msg.nameFromJson("{wqeeq"));
    }
}