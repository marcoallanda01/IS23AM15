package it.polimi.ingsw.server.model;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;

import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import org.jetbrains.annotations.NotNull;

public class GoalManager {
    private final List<PointsManager> pointsManagers = new ArrayList<>();
    private final CommonGoalCardManager commonGoalCardManager;
    private final PersonalGoalCardManager personalGoalCardManager;
    private final EndGamePointsManager endGamePointsManager;
    //private List<List<Pattern>> patterns;
    /**
     * if set to false only pointsManagers with updateRule set to END_TURN will be updated every turn
     * if set to true only pointsManagers with updateRule set to END_GAME will NOT be updated every turn
     */
    private Boolean frequentUpdates = Boolean.TRUE;

    /**
     * @param reader    reader from witch read the json
     * @param cardsType type of cards in the file json
     * @return list of patterns of selected cardsType
     * @throws JsonIOException     from JsonParser
     * @throws JsonSyntaxException from JsonParser
     */
    private List<Pattern> readCards(@NotNull JsonReader reader, String cardsType) throws JsonIOException, JsonSyntaxException {
        List<Pattern> patterns = new ArrayList<>();
        JsonObject json = JsonParser.parseReader(reader).getAsJsonObject();
        JsonArray commonCards = json.get(cardsType).getAsJsonArray();
        /*
        I decided to do not do like that because I can not identify the exact card that generated the exception
        If decide to do that explicit patterns as final
        commonCards.forEach((cardJ) -> {
            try {
                patterns.add(readPattern(cardJ));
            } catch (IOException e) {
                System.out.println("The loading of a card is fia");
            }
        });
        */

        for (int i = 0; i < commonCards.size(); i++) {
            try {
                patterns.add(readPattern(commonCards.get(i)));
            } catch (RuntimeException e) {
                // Here I can go on with the loading of others cards
                System.err.printf(
                        "RuntimeException: The loading of the %d pattern in %s is failed. This is probably" + " caused by wrong formatting of the file.\nException Message: %s", i,
                        cardsType, e.getMessage());
            }
        }

        return patterns;
    }

    /**
     * @param cardJson JsonElement representing the card
     * @return pattern to be generated from parsing. Returns null if pattern is invalid
     * @throws RuntimeException if there is bad formatting in the file, but we must throw so the GoalManager can try to
     *                          read the other patterns
     */
    private Pattern readPattern(JsonElement cardJson) throws RuntimeException {
        Pattern pattern = null;

        JsonObject patternJ = cardJson.getAsJsonObject();
        String name = patternJ.get("name").getAsString();
        String type = patternJ.get("type").getAsString();
        // TODO: funzioni separate
        switch (type) {
            case "specific" -> {
                /*
                {"name":"DIAGONAL",
                        "type":"specific", "masks": [{"matrix":[[1,0,0,0,1],[0,1,0,0,0],[0,0,1,0,0],[0,0,0,1,0],[0,0,0,0,1]]}, {"matrix":[[0,0,0,0,1],[0,0,0,1,0],[0,0,1,0,0],[0,1,0,0,0],[1,0,0,0,0]]}],
                    "grup_num":1, "sgc":"N","max":1,"min":1
                },
                */
                int groupNum = patternJ.get("group_num").getAsInt();
                int maxC = patternJ.get("max").getAsInt();
                int minC = patternJ.get("min").getAsInt();
                boolean sgc = patternJ.get("sgc").getAsString().equals("Y");
                JsonArray matrixsJ = patternJ.get("masks").getAsJsonArray();
                List<List<List<Boolean>>> matrixs = new ArrayList<>();
                for (int k = 0; k < matrixsJ.size(); k++) {
                    List<List<Boolean>> matrix = new ArrayList<>();
                    JsonArray matrixJ = matrixsJ.get(k).getAsJsonArray();
                    for (int i = 0; i < matrixJ.size(); i++) {
                        JsonArray rowJ = matrixJ.get(i).getAsJsonArray();
                        List<Boolean> rowList = new ArrayList<>();
                        for (int j = 0; j < rowJ.size(); j++) {
                            int c = rowJ.get(j).getAsInt();
                            switch (c) {
                                case 1 -> rowList.add(true);
                                case 0 -> rowList.add(false);
                                default -> {
                                    System.err.println("Wrong format in specific pattern");
                                    throw new IllegalStateException("Wrong format in specific pattern");
                                }
                            }
                        }
                        matrix.add(rowList);
                    }
                    matrixs.add(matrix);
                }
                pattern = new Specific(name, matrixs, groupNum, sgc, minC, maxC);
            }
            case "adjacent" -> {
                /*
                {"name":"3_ADJACENT",
                        "type":"adjacent", "min_tiles":3, "min_groups":1, "points":"2"
                },
                 */
                int minTiles = patternJ.get("min_tiles").getAsInt();
                int maxTiles = patternJ.get("min_groups").getAsInt();
                int points = patternJ.get("points").getAsInt();
                pattern = new Adjacent(name, minTiles, maxTiles, points);
            }
            case "personal" -> {
                /*
                {"name":"pc_1", "tiles":[[0,0,"PLANT"], [0,2,"FRAME"], [1,5,"CAT"], [2,4,"BOOK"], [3,1,"GAME"], [5,2,"TROPHIE"]],"check_to_points" : [[1,1], [2,2], [3,4], [4,6], [5,9], [6,12]] },
                */
                JsonArray tilesJ = patternJ.get("tiles").getAsJsonArray();
                List<Tile> tiles = new ArrayList<>();
                for (int i = 0; i < tilesJ.size(); i++) {
                    JsonArray tileJ = tilesJ.get(i).getAsJsonArray();
                    int x = tileJ.get(0).getAsInt();
                    int y = tileJ.get(1).getAsInt();
                    String tileTypeName = tileJ.get(1).getAsString();
                    if (tileTypeName == null) {
                        System.err.println("Invalid tileType in personal");
                        throw new IllegalStateException("Invalid tileType in personal");
                    }
                    Tile t = new Tile(x, y, TileType.tileTypeFromName(tileTypeName));
                    tiles.add(t);
                }
                JsonArray checkToPointsJ = patternJ.get("check_to_points").getAsJsonArray();
                List<int[]> checkToPoints = new ArrayList<>();
                for (int i = 0; i < checkToPointsJ.size(); i++) {
                    JsonArray coupleJ = checkToPointsJ.get(i).getAsJsonArray();
                    int check = coupleJ.get(0).getAsInt();
                    int p = coupleJ.get(1).getAsInt();
                    checkToPoints.add(new int[]{check, p});
                }
                // Check to points must be already ordered in the json file
                pattern = new Personal(name, tiles, checkToPoints);
            }
        }

        return pattern;
    }


    /**
     * @param players   players playing the game
     * @param setUpFile file json where there are the patterns associated with card type
     * @throws ArrestGameException if occurred very bad errors in parsing or json stream
     */
    public GoalManager(List<Player> players, String setUpFile) throws ArrestGameException {

        // I use list and not set because I could choose to have to same card so the probability increase
        Set<Pattern> patternsCommonGoals = new HashSet<>();
        Set<Pattern> patternsPersonalGoals = new HashSet<>();

        // Just only one pattern for every goal that is in common and all are active at the same time
        Set<Pattern> patternsEndGoals = new HashSet<>();

        FileReader in;
        try {
            in = new FileReader(Paths.get(getClass().getClassLoader().getResource(setUpFile).toURI()).toFile());
        } catch (FileNotFoundException e) {
            System.err.println("Error occurred in Goal Manager: file " + setUpFile + " can not be found!");
            System.err.println("More details: " + e);
            throw new ArrestGameException("ArrestGameException: Error occurred in GoalManager", e);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        try {

            JsonReader reader1 = new JsonReader(new FileReader(Paths.get(getClass().getClassLoader().getResource(setUpFile).toURI()).toFile()));
            patternsCommonGoals.addAll(readCards(reader1, "common_cards"));
            reader1.close();

            JsonReader reader2 = new JsonReader(new FileReader(Paths.get(getClass().getClassLoader().getResource(setUpFile).toURI()).toFile()));
            patternsEndGoals.addAll(readCards(reader2, "end_game"));
            reader2.close();



            for(Pattern p : patternsEndGoals){
                System.out.println("Pattern: "+p.toString());
            }


            for(Pattern p : patternsCommonGoals){
                System.out.println("Pattern: "+p.toString());
            }

            JsonReader reader3 = new JsonReader(new FileReader(Paths.get(getClass().getClassLoader().getResource(setUpFile).toURI()).toFile()));
            patternsPersonalGoals.addAll(readCards(reader3, "personal_cards"));
            reader3.close();

        } catch (JsonIOException e) {
            System.err.println("Error occurred in Goal Manager: JsonIOException occurred with file " + setUpFile +
                    ". This exception is raised when Gson was unable to read an input stream or" + " write to one.");
            throw new ArrestGameException("ArrestGameException:" + "Error occurred in GoalManager at the reading of the JsonReader", e);
        } catch (JsonParseException e) {
            System.err.println("Error occurred in Goal Manager: JsonIOException occurred with file " + setUpFile +
                    ". This exception is raised if there is a serious issue that occurs during parsing" + "of a Json string");
            throw new ArrestGameException("ArrestGameException:" + "Error occurred in GoalManager at the reading of the JsonReader", e);
        }
        catch (IOException e) {
            System.err.println("Error occurred in Goal Manager: IOException occurred with file " + setUpFile + " at the closing of the reader");
            System.err.println("More details: " + e);
            throw new ArrestGameException("ArrestGameException:" + "Error occurred in GoalManager at the closing of the JsonReader", e);
        } catch (URISyntaxException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        /*catch (InterruptedException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }*/


        // creating default managers, in future to add another manager add it here
        this.commonGoalCardManager = new CommonGoalCardManager(players, new Deck(patternsCommonGoals));
        this.personalGoalCardManager = new PersonalGoalCardManager(players, new Deck(patternsPersonalGoals));
        this.endGamePointsManager = new EndGamePointsManager(players, patternsEndGoals);

        this.pointsManagers.add(this.commonGoalCardManager);
        this.pointsManagers.add(this.personalGoalCardManager);
        this.pointsManagers.add(this.endGamePointsManager);
    }

    public GoalManager(CommonGoalCardManager commonGoalCardManager, PersonalGoalCardManager personalGoalCardManager, EndGamePointsManager endGamePointsManager,
                       Boolean frequentUpdates) {
        this.commonGoalCardManager = commonGoalCardManager;
        this.personalGoalCardManager = personalGoalCardManager;
        this.endGamePointsManager = endGamePointsManager;
        this.pointsManagers.add(this.commonGoalCardManager);
        this.pointsManagers.add(this.personalGoalCardManager);
        this.pointsManagers.add(this.endGamePointsManager);
        this.frequentUpdates = frequentUpdates;
    }

    /**
     * @param player the player
     *               updates the points of the player relative to every pointsManager that should be updated every turn
     */
    public void updatePointsTurn(Player player) {
        Predicate<PointsManager> toUpdate = frequentUpdates ? pointsManager -> pointsManager.getUpdateRule().equals(UpdateRule.END_GAME) :
                pointsManager -> pointsManager.getUpdateRule().equals(UpdateRule.END_TURN);
        pointsManagers.stream().filter(toUpdate).forEach(pointsManager -> pointsManager.updatePoints(player));
    }

    /**
     * @param player the player
     *               updates the points of the player relative to every pointsManager
     */
    public void updatePointsEnd(Player player) {
        pointsManagers.forEach(PointsManager -> PointsManager.updatePoints(player));
    }

    public int getPoints(Player player) {
        return pointsManagers.stream().map(pointsManager -> pointsManager.getPoints(player)).mapToInt(Integer::intValue).sum() + (player.isFirstToFinish() ? 1 : 0);
    }
    // good for now, might want to clone or send a simplified version of these objects for security reasons (again)

    public String getWinner(List<Player> players){
        for(Player player : players){
            updatePointsEnd(player);
        }
        return players.stream().max(Comparator.comparingInt(this::getPoints)).get().getUserName();
    }

    /**
     * @return a map associating cards to tokens
     */
    // this method does not take a card as input because cards are handled exclusively  by CardsManagers
    public Map<Pattern, Stack<Integer>> getCommonCardsToTokens() {
        return commonGoalCardManager.getCardsToTokens();
    }

    /**
     * @param player the player
     * @return the tokens of the player:
     */
    public List<Integer> getTokens(Player player) {
        return commonGoalCardManager.getTokens(player);
    }

    /**
     * @param player the player
     * @return the unfulfilled cards of the player
     */
    public Set<Pattern> getUnfulfilledCommonCards(Player player) {
        return commonGoalCardManager.getUnfulfilledCards(player);
    }

    /**
     * @param player the player
     * @return the fulfilled cards of the player
     */
    public Set<Pattern> getFulfilledCommonCards(Player player) {
        return commonGoalCardManager.getFulfilledCards(player);
    }

    /**
     * @param player the player
     * @return the personal card of the player
     */
    public Pattern getPersonalCard(Player player) {
        return personalGoalCardManager.getCard(player);
    }

    public Set<Pattern> getEndGameGoals() {
        return endGamePointsManager.getPatterns();
    }

    /**
     * Used for serialization
     */
    public CommonGoalCardManager getCommonGoalCardManager() {
        return commonGoalCardManager;
    }

    /**
     * Used for serialization
     */
    public PersonalGoalCardManager getPersonalGoalCardManager() {
        return personalGoalCardManager;
    }

    /**
     * Used for serialization
     */
    public EndGamePointsManager getEndGamePointsManager() {
        return endGamePointsManager;
    }

    /**
     * Used for serialization
     */
    public Boolean getFrequentUpdates() {
        return frequentUpdates;
    }
}
