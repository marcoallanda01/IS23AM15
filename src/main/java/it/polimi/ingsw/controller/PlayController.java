package it.polimi.ingsw.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import it.polimi.ingsw.model.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class PlayController {
    private final Game game;
    private final String directory;

    public PlayController(Game game, String directory) {
        this.game = game;
        this.directory = directory;
    }

    public boolean saveGame() throws IOException {
        Gson gson = new GsonBuilder().registerTypeAdapterFactory(OptionalTypeAdapter.FACTORY).registerTypeAdapter(LocalDateTime.class, new DateTimeTypeAdapter())
                .registerTypeAdapter(Game.class, new GameTypeAdapter()).create();
        String json = gson.toJson(this.game);
        JsonElement jsonElement = gson.fromJson(json, JsonElement.class);
        jsonElement.getAsJsonObject().addProperty("timestamp", LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));
        json = gson.toJson(jsonElement);
        File saves = new File(this.directory);
        if (!saves.exists()) {
            throw new IOException("Can not find " + saves);
        }
        File[] savesList = saves.listFiles();
        int n = 0;
        if (savesList != null) {
            n = savesList.length;
        }
        String save = this.directory + "/" + n + ".json";
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

    public Set<String> getEndGameGoals() {
        return game.getEndGameGoals();
    }

    public Set<String> getUnfulfilledCommonGoalCards(String nickname) throws PlayerNotFoundException {
        return game.getUnfulfilledCommonGoalCards(nickname);
    }

    public Set<String> getFulfilledCommonGoalCards(String nickname) throws PlayerNotFoundException {
        return game.getFulfilledCommonGoalCards(nickname);
    }

    public String getPersonalGoalCard(String nickname) throws PlayerNotFoundException {
        return game.getPersonalGoalCard(nickname);
    }

    public Set<Token> getTokens(String nickname) throws PlayerNotFoundException {
        return game.getTokens(nickname);
    }


}
