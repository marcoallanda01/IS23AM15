package it.polimi.ingsw.communication.responses;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CommonGoals extends Msg{
    public List<String> goals;

    public CommonGoals(@NotNull List<String> goals) {
        super("CommonGoals");
        this.goals = new ArrayList<>(goals);
    }

    /**
     * Generator of CommonGoals from a json string
     * @param json json string from which generate returned object
     * @return Optional of CommonGoals, empty if json string was not coherent
     */
    public static Optional<CommonGoals> fromJson(String json) {
        CommonGoals cg;
        try {
            Gson gson = new Gson();
            cg = gson.fromJson(json, CommonGoals.class);
        }catch (JsonSyntaxException e){
            return Optional.empty();
        }
        if(!"CommonGoals".equals(cg.name) || cg.goals == null){
            return Optional.empty();
        }
        return Optional.of(cg);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CommonGoals that = (CommonGoals) o;
        return goals.equals(that.goals);
    }
}
