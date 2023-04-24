package it.polimi.ingsw.server.communication;

import com.google.gson.Gson;
import it.polimi.ingsw.server.communication.responses.SavedGames;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class SavedGamesTest {

    @Test
    void tryGson(){
        Set<String> n = new HashSet<>();
        n.add("Ciao");
        n.add("Come");
        n.add("Stai");
        SavedGames sg = new SavedGames(n);
        Gson gson = new Gson();
        System.out.println(sg.toJson());
        System.out.println(gson.toJson(sg));
        assertEquals(gson.fromJson(sg.toJson(), SavedGames.class), gson.fromJson((gson.toJson(sg)), SavedGames.class));
    }

    @Test
    void fromJson() {
        Set<String> n = new HashSet<>();
        n.add("Ciao");
        n.add("Come");
        n.add("Stai");

        assertEquals(Optional.of(new SavedGames(n)), SavedGames.fromJson("{\"name\":\"SavedGames\"," +
                "\"names\":[\"Come\",\"Ciao\",\"Stai\"]}"));
        assertEquals(Optional.of(new SavedGames(new HashSet<String>())), SavedGames.fromJson("{\"name\":\"SavedGames\"," +
                "\"names\":[]}"));
        assertEquals(Optional.empty(), SavedGames.fromJson("{\"name\":\"SavedGames\"}"));
        assertEquals(Optional.empty(), SavedGames.fromJson("kjdsvaskd"));
        assertEquals(Optional.empty(), SavedGames.fromJson("{\"answer\":true}"));
        assertEquals(Optional.empty(), SavedGames.fromJson("{\"names\":[\"Come\",\"Ciao\",\"Stai\"]}"));
        assertEquals(Optional.empty(), SavedGames.fromJson("{\"name\":\"BooleanResponse\", \"result\":true}"));
    }

    @Test
    void toJson(){
        Set<String> n = new HashSet<>();
        n.add("Ciao");
        n.add("Come");
        n.add("Stai");
        System.out.println("toJson: "+ new SavedGames(n).toJson());
        assertEquals(SavedGames.fromJson("{\"name\":\"SavedGames\"," +
                "\"names\":[\"Come\",\"Ciao\",\"Stai\"]}"), SavedGames.fromJson(new SavedGames(n).toJson()));
    }
}