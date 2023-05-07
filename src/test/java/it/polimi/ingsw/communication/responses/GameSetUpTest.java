package it.polimi.ingsw.communication.responses;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class GameSetUpTest {

    @Test
    void fromJson() {
        List<String> g = new ArrayList<>();
        g.add("Ciao");
        g.add("Come");
        g.add("Stai");
        List<String> p = new ArrayList<>();
        p.add("p1");
        p.add("p2");
        p.add("p3");

        System.out.println(new GameSetUp(p, g).toJson());
        System.out.println("{\"name\":\"GameSetUp\"," +
                "\"players\":[\"p1\",\"p2\",\"p3\"], \"goals\":[\"Ciao\",\"Come\",\"Stai\"]}");
        assertEquals(Optional.of(new GameSetUp(p, g)), GameSetUp.fromJson("{\"name\":\"GameSetUp\"," +
                "\"players\":[\"p1\",\"p2\",\"p3\"], \"goals\":[\"Ciao\",\"Come\",\"Stai\"]}"));
        assertNotEquals(Optional.of(new GameSetUp(p, g)), GameSetUp.fromJson("{\"name\":\"GameSetUp\"," +
                "\"players\":[\"p1\",\"p2\",\"p3\"], \"goals\":[\"Ciao\",\"Stai\",\"Come\"]}"));
        assertEquals(Optional.of(new GameSetUp(p, new ArrayList<String>())),
                GameSetUp.fromJson("{\"name\":\"GameSetUp\"," +
                        "\"players\":[\"p1\",\"p2\",\"p3\"], \"goals\":[]}"));
        assertEquals(Optional.of(new GameSetUp(new ArrayList<String>(), g)),
                GameSetUp.fromJson("{\"name\":\"GameSetUp\", \"players\":[]," +
                        "\"goals\":[\"Ciao\",\"Come\",\"Stai\"]}"));


        assertEquals(Optional.empty(), GameSetUp.fromJson("{\"name\":\"GameSetUp\"," +
                "\"players\":[\"p1\",\"p2\",\"p3\"]"));
        assertEquals(Optional.empty(), GameSetUp.fromJson("{\"name\":\"GameSetUp\"," +
                "\"goals\":[\"Ciao\",\"Come\",\"Stai\"]}"));
        assertEquals(Optional.empty(), GameSetUp.fromJson("{\"name\":\"GameSetUp\"}"));
        assertEquals(Optional.empty(), GameSetUp.fromJson("kjdsvaskd"));
        assertEquals(Optional.empty(), GameSetUp.fromJson("{\"answer\":true}"));
        assertEquals(Optional.empty(), GameSetUp.fromJson("{\"goals\":[\"Come\",\"Ciao\",\"Stai\"]}"));
        assertEquals(Optional.empty(), GameSetUp.fromJson("{\"name\":\"BooleanResponse\", \"result\":true}"));
    }
}