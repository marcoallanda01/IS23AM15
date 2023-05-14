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

    public synchronized boolean pickTiles(List<Tile> tiles, String player) {
        if(game.getCurrentPlayer().equals(player))
            return game.pickTiles(tiles);
        else
            return false;
    }

    public synchronized boolean putTiles(List<Tile> tiles, int column, String player){
        if(game.getCurrentPlayer().equals(player))
            return game.putTiles(tiles, column);
        return false;
    }

    public synchronized boolean saveGame(String name) throws SaveException, IOException {
        return saveGame(name, false);
    }

    public synchronized boolean saveGame(String name, boolean overwrite) throws IOException, SaveException {
        Gson gson = new GsonBuilder().registerTypeAdapterFactory(OptionalTypeAdapter.FACTORY).registerTypeAdapter(LocalDateTime.class, new DateTimeTypeAdapter())
                .registerTypeAdapter(Game.class, new GameTypeAdapter()).create();
        String json = gson.toJson(this.game);
        JsonElement jsonElement = gson.fromJson(json, JsonElement.class);
        jsonElement.getAsJsonObject().addProperty("timestamp", LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));
        json = gson.toJson(jsonElement);
        File saves = new File(this.directory);
        if (!saves.exists()) {
            try {
                Files.createDirectories(Paths.get(this.directory));
            } catch (Exception e) {
                throw new IOException("Cannot create save folder:" + saves);
            }
        }
        File[] savesList = saves.listFiles();
        if(savesList != null && !overwrite) {
            for (File f : savesList) {
                if (f.getName().equals(name + ".json")) {
                    throw new SaveException("A save with this name already exists");
                }
            }
        }
        String save = this.directory + "/" + name + ".json";
        try {
            BufferedWriter writer = Files.newBufferedWriter(Paths.get(save));
            writer.write(json);
            writer.close();
        } catch (Exception e) {
            throw new IOException("Cannot save game");
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
    public synchronized boolean leave(String player){
        return game.disconnectPlayer(player);
    }

    /**
     * Method to make reconnect a player to the game
     * @param player player name (can be passed also a player name that doesn't exist)
     * @return true if the reconnection ended well. false if the player doesn't exist
     */
    public synchronized boolean reconnect(String player) {
        return game.reconnectPlayer(player);
    }

    /**
     * Check if there is a winner
     * @return true if there is a winner
     */
    public synchronized boolean isWinnerPresent() {
        return game.getWinner() != null;
    }

    /**
     * Get winner name if present
     * @return winner name; if winner is not present null
     */
    public synchronized String getWinner() {
        return game.getWinner();
    }

    /**
     * Get players of game
     * @return list of players name if order of turn
     */
    public synchronized List<String> getPlayers() {
        return game.getPlayers();
    }
}
