package it.polimi.ingsw.communication.responses;

import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class CommonCardsTest {

    @Test
    void fromJson() {
        List<Integer> tokens1 =new ArrayList<>();
        List<Integer> tokens2 =new ArrayList<>();
        Map<String, List<Integer>> cardsAndTokens = new HashMap<>();
        tokens1.add(4);
        tokens1.add(2);
        tokens1.add(1);
        tokens2.add(5);
        tokens2.add(3);
        tokens2.add(1);
        cardsAndTokens.put("c1", tokens1);
        cardsAndTokens.put("c2", tokens2);

        CommonCards cc = new CommonCards(cardsAndTokens);

        System.out.println(cc.toJson());
        System.out.println("{\"name\":\"CommonCards\"," +
                "\"cardsAndTokens\":{\"c2\":[5,3,1], \"c1\":[4,2,1]}}");

        assertEquals(cc, CommonCards.fromJson("{\"name\":\"CommonCards\"," +
                "\"cardsAndTokens\":{\"c2\":[5,3,1], \"c1\":[4,2,1]}}").get());
        assertNotEquals(cc, CommonCards.fromJson("{\"name\":\"CommonCards\"," +
                "\"cardsAndTokens\":{\"c2\":[6,3,1], \"c1\":[4,2,1]}}").get());
        assertNotEquals(cc, CommonCards.fromJson("{\"name\":\"CommonCards\"," +
                "\"cardsAndTokens\":{\"c1\":[5,3,1], \"c2\":[4,2,1]}}").get());
        assertNotEquals(cc, CommonCards.fromJson("{\"name\":\"CommonCards\"," +
                "\"cardsAndTokens\":{\"c1\":[4,2,1]}}").get());

        assertEquals(Optional.empty(), CommonCards.fromJson("{\"name\":\"CommonCards\"}"));
        assertEquals(Optional.empty(), CommonCards.fromJson("kjdsvaskd"));
        assertEquals(Optional.empty(), CommonCards.fromJson("{\"answer\":true}"));
        assertEquals(Optional.empty(), CommonCards.fromJson("{\"cardsAndTokens\":{\"c2\":[5,3,1], \"c1\":[4,2,1]}}"));
        assertEquals(Optional.empty(), CommonCards.fromJson("{\"name\":\"BooleanResponse\", \"result\":true}"));
    }
}