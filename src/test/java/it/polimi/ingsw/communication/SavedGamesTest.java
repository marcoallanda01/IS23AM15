package it.polimi.ingsw.communication;

import com.google.gson.Gson;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class SavedGamesTest {


    @Test
    void toJson() {

    }

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
    }
}