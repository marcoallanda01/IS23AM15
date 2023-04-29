package it.polimi.ingsw.communication.responses;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class CommonCards extends Msg{
    public Map<String, List<Integer>> cardsAndTokens;
    public CommonCards(@NotNull Map<String, List<Integer>> cardsAndTokens) {
        super("CommonCards");
        this.cardsAndTokens = new HashMap<>(cardsAndTokens);
    }

    /**
     * Generator of CommonGoals from a json string
     * @param json json string from which generate returned object
     * @return Optional of CommonGoals, empty if json string was not coherent
     */
    public static Optional<CommonCards> fromJson(String json) {
        CommonCards cc;
        try {
            Gson gson = new Gson();
            cc = gson.fromJson(json, CommonCards.class);
        }catch (JsonSyntaxException e){
            return Optional.empty();
        }
        if(!"CommonCards".equals(cc.name) || cc.cardsAndTokens == null){
            return Optional.empty();
        }
        return Optional.of(cc);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CommonCards that = (CommonCards) o;
        return Objects.equals(cardsAndTokens, that.cardsAndTokens);
    }
}
