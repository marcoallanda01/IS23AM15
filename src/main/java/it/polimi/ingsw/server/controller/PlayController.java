package it.polimi.ingsw.server.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import it.polimi.ingsw.server.model.*;

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

    public synchronized boolean pickTiles(List<Tile> tiles) {
        return game.pickTiles(tiles);
    }

    public synchronized boolean putTiles(List<Tile> tiles, int column) {
        return game.putTiles(tiles, column);
    }

    public synchronized boolean saveGame() throws IOException {
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

    public synchronized Integer getPoints(String nickname) throws PlayerNotFoundException {
        return game.getPoints(nickname);
    }

    public synchronized Map<String, List<Integer>> getCommonGoalCardsToTokens() {
        return game.getCommonGoalCardsToTokens();
    }

    public synchronized Set<String> getEndGameGoals() {
        return game.getEndGameGoals();
    }

    public synchronized Set<String> getUnfulfilledCommonGoalCards(String nickname) throws PlayerNotFoundException {
        return game.getUnfulfilledCommonGoalCards(nickname);
    }

    public synchronized Set<String> getFulfilledCommonGoalCards(String nickname) throws PlayerNotFoundException {
        return game.getFulfilledCommonGoalCards(nickname);
    }

    public synchronized String getPersonalGoalCard(String nickname) throws PlayerNotFoundException {
        return game.getPersonalGoalCard(nickname);
    }

    public synchronized List<Integer> getTokens(String nickname) throws PlayerNotFoundException {
        return game.getTokens(nickname);
    }

    /**
     * Method to make leave the game a player
     * @param player player name (can be passed also a player name that doesn't exist)
     * @return true if the disconnection ended well. false if the player doesn't exist
     */
    public boolean leave(String player) {
        return true;
    }
}
