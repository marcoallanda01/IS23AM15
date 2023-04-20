package it.polimi.ingsw.server.controller;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import it.polimi.ingsw.server.controller.OptionalTypeAdapter;
import it.polimi.ingsw.server.model.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

public class GameTypeAdapter extends TypeAdapter<Game> {

    private final Gson gson =
            new GsonBuilder().registerTypeAdapterFactory(OptionalTypeAdapter.FACTORY).registerTypeAdapter(LocalDateTime.class, new DateTimeTypeAdapter()).create();

    @Override
    public void write(JsonWriter jsonWriter, Game game) throws IOException {
        jsonWriter.beginObject();
        jsonWriter.name("players");
        gson.toJson(game.getPlayersList(), new TypeToken<List<Player>>() {
        }.getType(), jsonWriter);

        jsonWriter.name("winner");
        jsonWriter.value(game.getWinner());

        jsonWriter.name("isFirstGame");
        jsonWriter.value(game.isFirstGame());

        jsonWriter.name("board");
        gson.toJson(game.getBoard(), LivingRoomBoard.class, jsonWriter);

        jsonWriter.name("currentTurn");
        jsonWriter.beginObject();
        jsonWriter.name("pickedTiles");
        gson.toJson(game.getCurrentTurn().getPickedTiles(), new TypeToken<List<Tile>>() {
        }.getType(), jsonWriter);
        jsonWriter.name("currentPlayer");
        jsonWriter.value(game.getCurrentTurn().getCurrentPlayer().getUserName());
        jsonWriter.name("state");
        jsonWriter.value(game.getCurrentTurn().getState().toString());
        jsonWriter.endObject();

        jsonWriter.name("chat");
        gson.toJson(game.getChat(), Chat.class, jsonWriter);

        jsonWriter.endObject();
    }

    @Override
    public Game read(JsonReader in) throws IOException {
        List<Player> players = null;
        String winner = null;
        boolean isFirstGame = false;
        LivingRoomBoard board = null;
        Player currentPlayer = null;
        String stateName = null;
        Turn turn = null;
        Chat chat = null;

        in.beginObject();

        while (in.hasNext()) {
            String name = in.nextName();

            switch (name) {
                case "players" -> {
                    // Read the list of players
                    players = gson.fromJson(in, new TypeToken<List<Player>>() {
                    }.getType());
                    if (players == null || players.isEmpty()) throw new JsonParseException("Player list is null or empty");
                }
                case "winner" -> {
                    // Read the winner
                    winner = in.nextString();
                }
                case "isFirstGame" -> {
                    // Read the isFirstGame flag
                    isFirstGame = in.nextBoolean();
                }
                case "board" -> {
                    // Read the LivingRoomBoard object
                    board = gson.fromJson(in, LivingRoomBoard.class);
                    if (board == null) throw new JsonParseException("Board is null");
                }
                case "currentTurn" -> {
                    // Read the current turn object
                    in.beginObject();
                    while (in.hasNext()) {
                        String turnName = in.nextName();

                        switch (turnName) {
                            case "pickedTiles" -> {
                                // Read the list of picked tiles
                                List<Tile> pickedTiles = gson.fromJson(in, new TypeToken<List<Tile>>() {
                                }.getType());
                            }
                            case "currentPlayer" -> {
                                // Read the current player's username
                                String currentPlayerName = in.nextString();
                                currentPlayer = players.stream().filter(p -> p.getUserName().equals(currentPlayerName)).findFirst().orElse(null);
                            }
                            case "state" -> {
                                // Read the turn state
                                stateName = in.nextString();
                            }
                        }
                    }
                    if (currentPlayer == null) throw new JsonParseException("Current player is null");
                    if (stateName == null) throw new JsonParseException("State name is null");
                    turn = new Turn(currentPlayer, board);
                    switch (stateName) {
                        case "PickTilesState" -> turn.changeState(new PickTilesState(turn));
                        case "PutTileState" -> turn.changeState(new PutTilesState(turn));
                        case "EndState" -> turn.changeState(new EndState(turn));
                    }
                    in.endObject();
                }
                case "chat" -> {
                    // Read the chat object
                    chat = gson.fromJson(in, Chat.class);
                }
                // Ignore unknown fields
                default -> in.skipValue();
            }
        }

        // End reading the JSON object
        in.endObject();

        if(players == null || board == null || turn == null || chat == null)
            throw new JsonParseException("Some fields are null");
        return new Game(players, winner, isFirstGame, board, turn, chat);
    }

}
