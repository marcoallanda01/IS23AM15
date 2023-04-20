package it.polimi.ingsw.communication;

import com.google.gson.JsonParseException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JsonStringTest {

    @Test
    void JsonStringConstruction(){
        assertThrows(JsonParseException.class , ()->{new JsonString("{}sda");});
    }

    @Test
    void getJsonTest(){
        assertEquals("{\"casa\":true}", new JsonString("{\"casa\":true}").getJson());
    }

    @Test
    void toStringTest(){
        assertEquals("{\"casa\":true}",""+new JsonString("{\"casa\":true}")+"");
    }

}