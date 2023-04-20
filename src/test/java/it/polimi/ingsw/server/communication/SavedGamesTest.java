package it.polimi.ingsw.server.communication;

import com.google.gson.Gson;
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
        it.polimi.ingsw.server.communication.SavedGames sg = new it.polimi.ingsw.server.communication.SavedGames(n);
        Gson gson = new Gson();
        System.out.println(sg.toJson());
        System.out.println(gson.toJson(sg));
        assertEquals(gson.fromJson(sg.toJson(), it.polimi.ingsw.server.communication.SavedGames.class), gson.fromJson((gson.toJson(sg)), it.polimi.ingsw.server.communication.SavedGames.class));
    }

    @Test
    void fromJson() {
        Set<String> n = new HashSet<>();
        n.add("Ciao");
        n.add("Come");
        n.add("Stai");

        assertEquals(Optional.of(new it.polimi.ingsw.server.communication.SavedGames(n)), it.polimi.ingsw.server.communication.SavedGames.fromJson("{\"name\":\"SavedGames\"," +
                "\"names\":[\"Come\",\"Ciao\",\"Stai\"]}"));
        assertEquals(Optional.of(new it.polimi.ingsw.server.communication.SavedGames(new HashSet<String>())), it.polimi.ingsw.server.communication.SavedGames.fromJson("{\"name\":\"SavedGames\"," +
                "\"names\":[]}"));
        assertEquals(Optional.empty(), it.polimi.ingsw.server.communication.SavedGames.fromJson("{\"name\":\"SavedGames\"}"));
        assertEquals(Optional.empty(), it.polimi.ingsw.server.communication.SavedGames.fromJson("kjdsvaskd"));
        assertEquals(Optional.empty(), it.polimi.ingsw.server.communication.SavedGames.fromJson("{\"answer\":true}"));
        assertEquals(Optional.empty(), it.polimi.ingsw.server.communication.SavedGames.fromJson("{\"names\":[\"Come\",\"Ciao\",\"Stai\"]}"));
        assertEquals(Optional.empty(), it.polimi.ingsw.server.communication.SavedGames.fromJson("{\"name\":\"BooleanResponse\", \"result\":true}"));
    }

    @Test
    void toJson(){
        Set<String> n = new HashSet<>();
        n.add("Ciao");
        n.add("Come");
        n.add("Stai");
        System.out.println("toJson: "+ new it.polimi.ingsw.server.communication.SavedGames(n).toJson());
        assertEquals(it.polimi.ingsw.server.communication.SavedGames.fromJson("{\"name\":\"SavedGames\"," +
                "\"names\":[\"Come\",\"Ciao\",\"Stai\"]}"), it.polimi.ingsw.server.communication.SavedGames.fromJson(new it.polimi.ingsw.server.communication.SavedGames(n).toJson()));
    }
}