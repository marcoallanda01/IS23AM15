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

    /**
     * Perform pick tiles action in game
     * @param tiles list of tiles to pick
     * @param player player who is picking
     * @return true if the pick was successful
     */
    public synchronized boolean pickTiles(List<Tile> tiles, String player) {
        if(game.getCurrentPlayer().equals(player))
            return game.pickTiles(tiles);
        else
            return false;
    }

    /**
     * Perform put tiles action in game
     * @param tiles list of tiles to put in player's bookshelf
     * @param column bookshelf column where to put the tiles
     * @param player player who is performing the action
     * @return true if the put was successful
     */
    public synchronized boolean putTiles(List<Tile> tiles, int column, String player){
        if(game.getCurrentPlayer().equals(player))
            return game.putTiles(tiles, column);
        return false;
    }

    /**
     * Save the game
     * @param name future name of the save
     * @return true if it was successful
     * @throws SaveException
     * @throws IOException
     */
    public synchronized boolean saveGame(String name) throws SaveException, IOException {
        return saveGame(name, false); //TODO: write  why exception are thrown
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

    /**
     * Get player's points
     * @param nickname player's nickname
     * @return points
     * @throws PlayerNotFoundException if there is no player with such nickname
     */
    public synchronized Integer getPoints(String nickname) throws PlayerNotFoundException {
        return game.getPoints(nickname);
    }

    /**
     * Get common goals carts with their tokens from game
     * @return names of common goals carts with their tokens
     */
    public synchronized Map<String, List<Integer>> getCommonGoalCardsToTokens() {
        return game.getCommonGoalCardsToTokens();
    }

    /**
     * Get end goals from game
     * @return end game goals names
     */
    public synchronized Set<String> getEndGameGoals() {
        return game.getEndGameGoals();
    }

    /**
     * Get common goals cards that player hasn't completed yet
     * @param nickname player's nickname
     * @return common goals cards list
     * @throws PlayerNotFoundException if there is no player with such nickname
     */
    public synchronized Set<String> getUnfulfilledCommonGoalCards(String nickname) throws PlayerNotFoundException {
        return game.getUnfulfilledCommonGoalCards(nickname);
    }

    /**
     * Get common goals cards that player has completed
     * @param nickname player's nickname
     * @return common goals cards list
     * @throws PlayerNotFoundException if there is no player with such nickname
     */
    public synchronized Set<String> getFulfilledCommonGoalCards(String nickname) throws PlayerNotFoundException {
        return game.getFulfilledCommonGoalCards(nickname);
    }

    /**
     * Get personal card of a player
     * @param nickname player's nickname
     * @return name of the personal card
     * @throws PlayerNotFoundException if there is no player with such nickname
     */
    public synchronized String getPersonalGoalCard(String nickname) throws PlayerNotFoundException {
        return game.getPersonalGoalCard(nickname);
    }

    /**
     * Get tokens tha a player has gained
     * @param nickname player's nickname
     * @return list of tokens
     * @throws PlayerNotFoundException if there is no player with such nickname
     */
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
