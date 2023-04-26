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
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.stream.Collectors;

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


        GoalManager goalManager = game.getGoalManager();
        if(goalManager != null) {
            jsonWriter.name("goalManager");
            jsonWriter.beginObject();
            {
                jsonWriter.name("commonGoalCardManager");
                jsonWriter.beginObject();
                {
                    jsonWriter.name("playersToPoints");
                    Map<String, Integer> playerToPoints = goalManager.getCommonGoalCardManager().getPlayersToPoints().entrySet().stream()
                            .collect(Collectors.toMap(entry -> entry.getKey().getUserName(), Map.Entry::getValue));
                    gson.toJson(playerToPoints, new TypeToken<Map<String, Integer>>() {
                    }.getType(), jsonWriter);
                    jsonWriter.name("updateRule");
                    gson.toJson(goalManager.getCommonGoalCardManager().getUpdateRule());
                    jsonWriter.name("deck");
                    gson.toJson(goalManager.getCommonGoalCardManager().getDeck(), new TypeToken<Deck>() {
                    }.getType(), jsonWriter);

                    jsonWriter.name("cardsToTokens");
                    gson.toJson(goalManager.getCommonGoalCardManager().getCardsToTokens(), new TypeToken<Map<Card, Stack<Token>>>() {
                    }.getType(), jsonWriter);
                    jsonWriter.name("playersToTokens");
                    Map<String, Set<Token>> playerToToken = goalManager.getCommonGoalCardManager().getPlayersToTokens().entrySet().stream()
                            .collect(Collectors.toMap(entry -> entry.getKey().getUserName(), Map.Entry::getValue));
                    gson.toJson(playerToToken, new TypeToken<Map<String, Set<Token>>>() {
                    }.getType(), jsonWriter);
                    jsonWriter.name("playersToUnfulfilledCards");
                    Map<String, Set<Card>> playerToUnfulfilledCards = goalManager.getCommonGoalCardManager().getPlayersToUnfulfilledCards().entrySet().stream()
                            .collect(Collectors.toMap(entry -> entry.getKey().getUserName(), Map.Entry::getValue));
                    gson.toJson(playerToUnfulfilledCards, new TypeToken<Map<String, Set<Card>>>() {
                    }.getType(), jsonWriter);
                }
                jsonWriter.endObject();
                jsonWriter.name("personalGoalCardManager");
                jsonWriter.beginObject();
                {
                    jsonWriter.name("playersToPoints");
                    Map<String, Integer> playerToPoints = goalManager.getPersonalGoalCardManager().getPlayersToPoints().entrySet().stream()
                            .collect(Collectors.toMap(entry -> entry.getKey().getUserName(), Map.Entry::getValue));
                    gson.toJson(playerToPoints, new TypeToken<Map<String, Integer>>() {
                    }.getType(), jsonWriter);
                    jsonWriter.name("updateRule");
                    gson.toJson(goalManager.getPersonalGoalCardManager().getUpdateRule());
                    jsonWriter.name("deck");
                    gson.toJson(goalManager.getPersonalGoalCardManager().getDeck(), new TypeToken<Deck>() {
                    }.getType(), jsonWriter);
                    jsonWriter.name("playersToCards");
                    Map<String, Card> playerToCard = goalManager.getPersonalGoalCardManager().getPlayersToCards().entrySet().stream()
                            .collect(Collectors.toMap(entry -> entry.getKey().getUserName(), Map.Entry::getValue));
                    gson.toJson(playerToCard, new TypeToken<Map<String, Card>>() {
                    }.getType(), jsonWriter);
                }
                jsonWriter.endObject();
                jsonWriter.name("endGamePointsManager");
                jsonWriter.beginObject();
                {
                    jsonWriter.name("playersToPoints");
                    Map<String, Integer> playerToPoints = goalManager.getEndGamePointsManager().getPlayersToPoints().entrySet().stream()
                            .collect(Collectors.toMap(entry -> entry.getKey().getUserName(), Map.Entry::getValue));
                    gson.toJson(playerToPoints, new TypeToken<Map<String, Integer>>() {
                    }.getType(), jsonWriter);
                    jsonWriter.name("updateRule");
                    gson.toJson(goalManager.getEndGamePointsManager().getUpdateRule());
                    jsonWriter.name("patterns");
                    gson.toJson(goalManager.getEndGamePointsManager().getPatterns(), new TypeToken<Set<Pattern>>() {
                    }.getType(), jsonWriter);
                }
                jsonWriter.endObject();
                jsonWriter.name("frequentUpdates");
                jsonWriter.value(goalManager.getFrequentUpdates());
            }
            jsonWriter.endObject();
        }

        jsonWriter.endObject();
    }

    @Override
    public Game read(JsonReader in) throws IOException {
        List<Player> players = null;
        String winner = null;
        boolean isFirstGame = false;
        LivingRoomBoard board = null;
        List<Tile> pickedTiles = null;
        Player currentPlayer = null;
        String stateName = null;
        Turn turn = null;
        Chat chat = null;

        GoalManager goalManager = null;
        CommonGoalCardManager commonGoalCardManager = null;
        Map<Card, Stack<Token>> cardsToTokens = null;
        Map<Player, Integer> playerToPoints = null;
        UpdateRule updateRule = null;
        Deck deck = null;
        Map<Player, Set<Token>> playerToTokens = null;
        Map<Player, Set<Card>> playerToUnfulfilledCards = null;
        PersonalGoalCardManager personalGoalCardManager = null;
        Map<Player, Integer> playerToPoints2 = null;
        UpdateRule updateRule2 = null;
        Deck deck2 = null;
        Map<Player, Card> playerToCards = null;
        EndGamePointsManager endGamePointsManager = null;
        Map<Player, Integer> playerToPoints3 = null;
        UpdateRule updateRule3 = null;
        Set<Pattern> patterns = null;
        boolean frequentUpdates = false;

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
                                pickedTiles = gson.fromJson(in, new TypeToken<List<Tile>>() {
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
                    turn = new Turn(pickedTiles, currentPlayer, board);
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
                case "goalManager" -> {
                    in.beginObject();

                    List<Player> finalPlayers = players;
                    while (in.hasNext()) {
                        String field = in.nextName();

                        switch (field) {
                            case "commonGoalCardManager" -> {
                                in.beginObject();
                                while (in.hasNext()) {
                                    String commonGoalCardManagerField = in.nextName();

                                    switch (commonGoalCardManagerField) {
                                        case "playersToPoints" -> {
                                            Map<String, Integer> playerToPointsNames = gson.fromJson(in, new TypeToken<Map<String, Integer>>() {
                                            }.getType());
                                            playerToPoints = playerToPointsNames.entrySet().stream().collect(
                                                    Collectors.toMap(entry -> finalPlayers.stream().filter(p -> p.getUserName().equals(entry.getKey())).findFirst().orElse(null),
                                                            Map.Entry::getValue));
                                        }
                                        case "updateRule" -> {
                                            updateRule = gson.fromJson(in, UpdateRule.class);
                                        }
                                        case "deck" -> {
                                            deck = gson.fromJson(in, Deck.class);
                                        }
                                        case "cardsToTokens" -> {
                                            cardsToTokens = gson.fromJson(in, new TypeToken<Map<Card, Stack<Token>>>() {
                                            }.getType());
                                        }
                                        case "playersToTokens" -> {
                                            Map<String, Set<Token>> playerToTokensNames = gson.fromJson(in, new TypeToken<Map<String, Set<Token>>>() {
                                            }.getType());
                                            playerToTokens = playerToTokensNames.entrySet().stream().collect(
                                                    Collectors.toMap(entry -> finalPlayers.stream().filter(p -> p.getUserName().equals(entry.getKey())).findFirst().orElse(null),
                                                            Map.Entry::getValue));
                                        }
                                        case "playersToUnfulfilledCards" -> {
                                            Map<String, Set<Card>> playerToUnfulfilledCardsNames = gson.fromJson(in, new TypeToken<Map<String, Set<Card>>>() {
                                            }.getType());
                                            playerToUnfulfilledCards = playerToUnfulfilledCardsNames.entrySet().stream().collect(
                                                    Collectors.toMap(entry -> finalPlayers.stream().filter(p -> p.getUserName().equals(entry.getKey())).findFirst().orElse(null),
                                                            Map.Entry::getValue));
                                        }
                                    }

                                }
                                if (playerToPoints == null || updateRule == null || deck == null || playerToTokens == null || playerToUnfulfilledCards == null)
                                    throw new JsonParseException("Some fields are null");
                                commonGoalCardManager =
                                        new CommonGoalCardManager(players, playerToPoints, updateRule, deck, cardsToTokens, playerToTokens, playerToUnfulfilledCards);
                                in.endObject();
                            }
                            case "personalGoalCardManager" -> {
                                in.beginObject();
                                while (in.hasNext()) {
                                    String personalGoalCardManagerField = in.nextName();

                                    switch (personalGoalCardManagerField) {
                                        case "playersToPoints" -> {
                                            Map<String, Integer> playerToPointsNames = gson.fromJson(in, new TypeToken<Map<String, Integer>>() {
                                            }.getType());
                                            playerToPoints2 = playerToPointsNames.entrySet().stream().collect(
                                                    Collectors.toMap(entry -> finalPlayers.stream().filter(p -> p.getUserName().equals(entry.getKey())).findFirst().orElse(null),
                                                            Map.Entry::getValue));
                                        }
                                        case "updateRule" -> {
                                            updateRule2 = gson.fromJson(in, UpdateRule.class);
                                        }
                                        case "deck" -> {
                                            deck2 = gson.fromJson(in, Deck.class);
                                        }
                                        case "playersToCards" -> {
                                            Map<String, Card> playerToCardsNames = gson.fromJson(in, new TypeToken<Map<String, Card>>() {
                                            }.getType());
                                            playerToCards = playerToCardsNames.entrySet().stream().collect(
                                                    Collectors.toMap(entry -> finalPlayers.stream().filter(p -> p.getUserName().equals(entry.getKey())).findFirst().orElse(null),
                                                            Map.Entry::getValue));
                                        }
                                    }
                                }
                                if (playerToPoints2 == null || updateRule2 == null || deck2 == null || playerToCards == null) throw new JsonParseException("Some fields are null");
                                personalGoalCardManager = new PersonalGoalCardManager(players, playerToPoints2, updateRule2, deck2, playerToCards);
                                in.endObject();
                            }
                            case "EndGamePointsManager" -> {
                                in.beginObject();
                                while (in.hasNext()) {
                                    String endGamePointsManagerField = in.nextName();

                                    switch (endGamePointsManagerField) {
                                        case "playersToPoints" -> {
                                            Map<String, Integer> playerToPointsNames = gson.fromJson(in, new TypeToken<Map<String, Integer>>() {
                                            }.getType());
                                            playerToPoints3 = playerToPointsNames.entrySet().stream().collect(
                                                    Collectors.toMap(entry -> finalPlayers.stream().filter(p -> p.getUserName().equals(entry.getKey())).findFirst().orElse(null),
                                                            Map.Entry::getValue));
                                        }
                                        case "updateRule" -> {
                                            updateRule3 = gson.fromJson(in, UpdateRule.class);
                                        }
                                        case "patterns" -> {
                                            patterns = gson.fromJson(in, new TypeToken<Set<Pattern>>() {
                                            }.getType());
                                        }
                                    }
                                }
                                if (playerToPoints3 == null || updateRule3 == null || patterns == null) throw new JsonParseException("Some fields are null");
                                endGamePointsManager = new EndGamePointsManager(players, playerToPoints3, updateRule3, patterns);
                                in.endObject();
                            }
                            case "frequentUpdates" -> {
                                frequentUpdates = in.nextBoolean();
                            }
                        }
                    }
                    if (commonGoalCardManager == null || personalGoalCardManager == null || endGamePointsManager == null)
                        throw new JsonParseException("At least one of the goal managers is null");
                    goalManager = new GoalManager(commonGoalCardManager, personalGoalCardManager, endGamePointsManager, frequentUpdates);
                }
                // Ignore unknown fields
                default -> in.skipValue();
            }
        }
        // End reading the JSON object
        in.endObject();

        if (players == null || board == null || turn == null || chat == null) throw new JsonParseException("Some final elements of game are null");
        return new Game(players, winner, isFirstGame, board, turn, chat, goalManager);
    }

}
