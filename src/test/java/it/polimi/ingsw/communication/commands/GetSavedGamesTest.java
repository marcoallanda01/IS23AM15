package it.polimi.ingsw.communication.commands;

import it.polimi.ingsw.communication.responses.Hello;
import it.polimi.ingsw.communication.responses.SavedGames;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class GetSavedGamesTest {

    @Test
    void fromJson() {
        String idFirstPlayer = "test";

        String json = "{\"name\":\"GetSavedGames\",\"idFirstPlayer\":\""+idFirstPlayer+"\"}";
        System.out.println(json);
        System.out.println(new GetSavedGames(idFirstPlayer).toJson());
        assertEquals(Optional.of(new GetSavedGames(idFirstPlayer)), GetSavedGames.fromJson(json));
        json = "{\"name\":\"GetSavedGames\",\"idFirstPlayer\":\""+idFirstPlayer+"\"}";
        assertNotEquals(Optional.of(new GetSavedGames("boh")), GetSavedGames.fromJson(json));


        assertEquals(Optional.empty(),GetSavedGames.fromJson("kjdsvaskd"));
        assertEquals(Optional.empty(),GetSavedGames.fromJson("{\"answer\":true}"));
        assertEquals(Optional.empty(),GetSavedGames.fromJson("{\"idFirstPlayer\":\"test\"}"));
        assertEquals(Optional.empty(),GetSavedGames.fromJson("{\"name\":\"GetSavedGames\"}"));
        assertEquals(Optional.empty(),GetSavedGames.fromJson("{\"name\":\"BooleanResponse\", \"result\":true}"));
    }
}