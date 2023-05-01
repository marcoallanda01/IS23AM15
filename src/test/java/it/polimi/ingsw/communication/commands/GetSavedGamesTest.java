package it.polimi.ingsw.communication.commands;

import it.polimi.ingsw.communication.responses.Hello;
import it.polimi.ingsw.communication.responses.SavedGames;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class GetSavedGamesTest {

    @Test
    void fromJson() {

        String json = "{\"name\":\"GetSavedGames\"}";
        System.out.println(json);
        System.out.println(new GetSavedGames().toJson());
        assertEquals(Optional.of(new GetSavedGames()), GetSavedGames.fromJson(json));

        assertFalse(new GetSavedGames().equals(3));

        assertEquals(Optional.empty(),GetSavedGames.fromJson("kjdsvaskd"));
        assertEquals(Optional.empty(),GetSavedGames.fromJson("{\"name\":\"BooleanResponse\", \"result\":true}"));
    }
}