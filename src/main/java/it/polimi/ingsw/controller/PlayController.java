package it.polimi.ingsw.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.PlayerNotFoundException;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Optional;

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
        System.out.println("json = " + json);
        File saves = new File("saves");
        if (!saves.exists()) {
            saves.mkdir();
        }
        File[] savesList = saves.listFiles();
        int n = 0;
        if (savesList != null) {
            n = savesList.length;
        }
        String save = "saves/save" + n + ".json";
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

    public Integer getPlayerPoints(String nickname) throws PlayerNotFoundException {
        return game.getPlayerPoints(nickname);
    }
}
