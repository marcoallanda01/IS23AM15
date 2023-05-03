package it.polimi.ingsw.communication.responses;

import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class CommonGoalsTest {

    @Test
    void fromJson() {
        Set<String> g = new HashSet<>();
        g.add("Ciao");
        g.add("Come");
        g.add("Stai");

        assertEquals(Optional.of(new CommonGoals(g)), CommonGoals.fromJson("{\"name\":\"CommonGoals\"," +
                "\"goals\":[\"Ciao\",\"Come\",\"Stai\"]}"));
        assertNotEquals(Optional.of(new CommonGoals(g)), CommonGoals.fromJson("{\"name\":\"CommonGoals\"," +
                "\"goals\":[\"Ciao\",\"Stai\",\"Come\"]}"));
        assertEquals(Optional.of(new CommonGoals(new HashSet<>())), CommonGoals.fromJson("{\"name\":\"CommonGoals\"," +
                "\"goals\":[]}"));
        assertEquals(Optional.empty(), CommonGoals.fromJson("{\"name\":\"CommonGoals\"}"));
        assertEquals(Optional.empty(), CommonGoals.fromJson("kjdsvaskd"));
        assertEquals(Optional.empty(), CommonGoals.fromJson("{\"answer\":true}"));
        assertEquals(Optional.empty(), CommonGoals.fromJson("{\"goals\":[\"Come\",\"Ciao\",\"Stai\"]}"));
        assertEquals(Optional.empty(), CommonGoals.fromJson("{\"name\":\"BooleanResponse\", \"result\":true}"));
    }
}