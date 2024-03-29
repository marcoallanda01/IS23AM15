package it.polimi.ingsw.server.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import it.polimi.ingsw.server.controller.exceptions.SaveException;
import it.polimi.ingsw.server.controller.serialization.DateTimeTypeAdapter;
import it.polimi.ingsw.server.controller.serialization.GameTypeAdapter;
import it.polimi.ingsw.server.controller.serialization.OptionalTypeAdapter;
import it.polimi.ingsw.server.model.*;
import it.polimi.ingsw.server.model.exceptions.PlayerNotFoundException;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Controlled used to play the game independently of the model and manage saves
 */
public class PlayController {
    private final Game game;
    private final String directory;
    private final PushNotificationController pushNotificationController;

    /**
     * PlayController constructor
     * @param game game object
     * @param directory saves directory
     * @param pushNotificationController push notification controller object
     */
    public PlayController(Game game, String directory, PushNotificationController pushNotificationController) {
        this.game = game;
        this.directory = directory;
        this.pushNotificationController = pushNotificationController;
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
     * @throws SaveException when a save with the same name already exists and overwrite is false
     * @throws IOException when the save folder cannot be created
     */
    public synchronized boolean saveGame(String name) throws SaveException, IOException {
        return saveGame(name, false);
    }

    /**
     * Save the game
     * @param name future name of the save
     * @param overwrite true if the save should overwrite an existing one
     * @return true if it was successful
     * @throws SaveException when a save with the same name already exists and overwrite is false
     * @throws IOException when the save folder cannot be created
     */
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
        pushNotificationController.notifyGameSaved(name);
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
    public synchronized Map<String, List<Integer>> getCommonCardsToTokens() {
        return game.getCommonCardsToTokens();
    }

    /**
     * Get end goals from game
     * @return end game goals names
     */
    public synchronized Set<String> getCommonGoals() {
        return game.getCommonGoals();
    }

    /**
     * Get common goals cards that player hasn't completed yet
     * @param nickname player's nickname
     * @return common goals cards list
     * @throws PlayerNotFoundException if there is no player with such nickname
     */
    public synchronized Set<String> getUnfulfilledCommonCards(String nickname) throws PlayerNotFoundException {
        return game.getUnfulfilledCommonCards(nickname);
    }

    /**
     * Get common goals cards that player has completed
     * @param nickname player's nickname
     * @return common goals cards list
     * @throws PlayerNotFoundException if there is no player with such nickname
     */
    public synchronized Set<String> getFulfilledCommonCards(String nickname) throws PlayerNotFoundException {
        return game.getFulfilledCommonCards(nickname);
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
     * Check if a player is playing
     * @param player player's nickname
     * @return true if there is a player with that name who is playing
     */
    public synchronized boolean isPlaying(String player) {
        for(Player p : game.getPlayersList()){
            if(p.getUserName().equals(player)){
                return p.isPlaying();
            }
        }
        return false;
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

    /**
     * Get player's bookshelf
     * @param player player's name
     * @return bookshelf tiles
     * @throws PlayerNotFoundException if no player wa found
     */
    public synchronized Set<Tile> getBookshelf(String player) throws PlayerNotFoundException {
        for(Player p : game.getPlayersList()){
            if(p.getUserName().equals(player)){
                return new HashSet<>(p.getBookShelf().getAllTiles());
            }
        }
        throw new PlayerNotFoundException("getBookshelf: no player "+player+" found");
    }

    /**
     * Get board
     * @return board tiles
     */
    public synchronized Set<Tile> getBoard() {
        return new HashSet<>(game.getBoard().getAllTiles());
    }

    /**
     * Get current player
     * @return player's nickname
     */
    public synchronized String getCurrentPlayer() {
        return game.getCurrentPlayer();
    }
}
