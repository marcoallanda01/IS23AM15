package it.polimi.ingsw.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.PlayerNotFoundException;
import it.polimi.ingsw.model.Token;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.*;

public class PlayController {
    private final Game game;
    private final String directory;

    public PlayController(Game game, String directory) {
        this.game = game;
        this.directory = directory;
    }

    public boolean saveGame() throws IOException {
        Gson gson = new GsonBuilder().registerTypeAdapterFactory(OptionalTypeAdapter.FACTORY).registerTypeAdapter(LocalDateTime.class, new DateTimeTypeAdapter()).create();
        String json = gson.toJson(game);
        File saves = new File(this.directory);
        if (!saves.exists()) {
            throw new IOException("Can not find "+saves.toString());
        }
        File[] savesList = saves.listFiles();
        int n = 0;
        if (savesList != null) {
            n = savesList.length;
        }
        String save = this.directory + n + ".json";
        try {
            BufferedWriter writer = Files.newBufferedWriter(Paths.get(save));
            writer.write(json);
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public Integer getPoints(String nickname) throws PlayerNotFoundException {
        return game.getPoints(nickname);
    }
    public Map<String, Stack<Token>> getCommonGoalCardsToTokens() {
        return game.getCommonGoalCardsToTokens();
    }
    public List<String> getEndGameGoals() {
        return game.getEndGameGoals();
    }
    public List<String> getUnfulfilledCommonGoalCards(String nickname) throws PlayerNotFoundException{
        return game.getUnfulfilledCommonGoalCards(nickname);
    }
    public List<String> getFulfilledCommonGoalCards(String nickname) throws PlayerNotFoundException{
        return game.getFulfilledCommonGoalCards(nickname);
    }
    public String getPersonalGoalCard(String nickname) throws PlayerNotFoundException{
        return game.getPersonalGoalCard(nickname);
    }
    public Set<Token> getTokens(String nickname) throws PlayerNotFoundException {
        return game.getTokens(nickname);
    }


}
