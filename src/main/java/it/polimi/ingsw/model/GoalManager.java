package it.polimi.ingsw.model;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import it.polimi.ingsw.model.ArrestGameException;
import org.jetbrains.annotations.NotNull;

public class GoalManager {
    private final List<PointsManager> pointsManagers = new ArrayList<>();

    private final CommonGoalCardManager commonGoalCardManager;
    private final PersonalGoalCardManager personalGoalCardManager;
    private final EndGamePointsManager endGamePointsManager;

    private List<List<Pattern>> patterns;

    /**
     *
     * @param reader reader from witch read the json
     * @param cardsType type of cards in the file json
     * @return list of patterns of selected cardsType
     * @throws JsonIOException from JsonParser
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

        for(int i = 0; i < commonCards.size(); i++){
            try {
                patterns.add(readPattern(commonCards.get(i)));
            } catch (RuntimeException e) {
                // Here I can go on with the loading of others cards
                System.err.printf("RuntimeException: The loading of the %d pattern in %s is failed. This is probably" +
                        " caused by wrong formatting of the file.\nException Message: %s", i, cardsType, e.getMessage());
            }
        }

        return patterns;
    }

    /**
     * @param cardJson JsonElment representing the card
     * @return pattern to be generated from parsing. Returns null if pattern is invalid
     * @throws RuntimeException if there is bad formatting in the file, but we must throw so the GoalManager can try to
     *      read the other patterns
     */
    private Pattern readPattern(JsonElement cardJson) throws RuntimeException{
        Pattern pattern = null;

        JsonObject patternJ = cardJson.getAsJsonObject();
        String name = patternJ.get("name").getAsString();
        String type = patternJ.get("type").getAsString();
        switch (type) {
            case "specific" -> {
                /* {"name":"X", "type":"specific", "pattern":{"matrix":[[1,0,1],[0,1,0],[1,0,1]]}
                "group_num":1, "sgc":"N", "max":1, "min":1} */
                int groupNum = patternJ.get("group_num").getAsInt();
                int maxC = patternJ.get("max").getAsInt();
                int minC = patternJ.get("min").getAsInt();
                boolean sgc = patternJ.get("sgc").getAsString().equals("Y");
                JsonArray matrixsJ = patternJ.get("pattern").getAsJsonArray();
                List<List<List<Boolean>>> matrixs = new ArrayList<List<List<Boolean>>>();
                for (int k = 0; k < matrixsJ.size(); k++) {
                    List<List<Boolean>> matrix = new ArrayList<List<Boolean>>();
                    JsonArray matrixJ = matrixsJ.get(k).getAsJsonArray();
                    for (int i = 0; i < matrixJ.size(); i++) {
                        JsonArray rowJ = matrixJ.get(i).getAsJsonArray();
                        List<Boolean> rowList = new ArrayList<Boolean>();
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
            case "line" -> {
                //{"name":"2_ROWS", "type":"line", "tiles_num":5, "directions":["O"], "sgc":"N", "max":5,"min":5}
                int tilesNum = patternJ.get("tiles_num").getAsInt();
                int groupNuml = patternJ.get("group_num").getAsInt();
                int maxCl = patternJ.get("max").getAsInt();
                int minCl = patternJ.get("min").getAsInt();
                boolean sgcl = patternJ.get("sgc").getAsString().equals("Y");
                JsonArray directionsJ = patternJ.get("directions").getAsJsonArray();
                List<Character> directions = new ArrayList<>();
                for (int i = 0; i < directionsJ.size(); i++) {
                    String d = directionsJ.get(i).getAsString();
                    if (d.length() > 1) {
                        System.err.println("Wrong format in line directions");
                        throw new IllegalStateException("Wrong format in line directions");
                    }
                    directions.add(d.charAt(0));
                }
                pattern = new Line(name, tilesNum, directions, groupNuml, sgcl, minCl, maxCl);
            }
            case "adjacent" -> {
                // {"name":"6_ADJACENT", "type":"adjacent", "min_tiles":6, "max_tiles":30, points:"8"}
                int minTiles = patternJ.get("min_tiles").getAsInt();
                int maxTiles = patternJ.get("max_tiles").getAsInt();
                int points = patternJ.get("points").getAsInt();
                pattern = new Adjacent(name, minTiles, maxTiles, points);
            }
            case "personal" -> {
                // {"name":"pc_1", "type" : "personal",
                //	"tiles":[[0,0,"PLANT"], [0,2,"FRAME"], [1,5,"CAT"], [2,4,"BOOK"], [3,1,"GAME"], [5,2,"TROPHIE"]]
                //	"check_to_points" : [[1,1], [2,2], [3,4], [4,6], [5,9], [6,12]] }
                JsonArray tilesJ = patternJ.get("tiles").getAsJsonArray();
                List<Tile> tiles = new ArrayList<Tile>();
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
                List<int[]> checkToPoints = new ArrayList<int[]>();
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
     *
     * @param players players playing the game
     * @param setUpFile file json where there are the patterns associated with card type
     * @throws ArrestGameException if occurred very bad errors in parsing or json stream
     */
    public GoalManager(List<Player> players, String setUpFile) throws ArrestGameException {

        // I use list and not set because I could choose to have to same card so the probability increase
        List<Pattern> patternsCommonGoals = new ArrayList<Pattern>();
        List<Pattern> patternsPersonalGoals = new ArrayList<Pattern>();

        // Just only one pattern for every goal that is in common and all are active at the same time
        Set<Pattern> patternsEndGoals = new HashSet<Pattern>();

        FileReader in;
        try {
            in = new FileReader(setUpFile);
        }
        catch (FileNotFoundException e){
            System.err.println("Error occurred in Goal Manager: file " + setUpFile + " can not be found!");
            System.err.println("More details: " + e.toString());
            throw new ArrestGameException("ArrestGameException: Error occurred in GoalManager", e);
        }

        try{
            JsonReader reader = new JsonReader(in);
            try {
                patternsCommonGoals.addAll(readCards(reader, "common_cards"));
                patternsPersonalGoals.addAll(readCards(reader, "end_game"));
                patternsEndGoals.addAll(readCards(reader, "personal_cards"));
            }catch (JsonIOException e){
                System.err.println("Error occurred in Goal Manager: JsonIOException occurred with file " +
                        setUpFile + ". This exception is raised when Gson was unable to read an input stream or" +
                        " write to one.");
                throw new ArrestGameException("ArrestGameException:" +
                        "Error occurred in GoalManager at the reading of the JsonReader", e);
            }catch (JsonParseException e){
                System.err.println("Error occurred in Goal Manager: JsonIOException occurred with file " +
                        setUpFile + ". This exception is raised if there is a serious issue that occurs during parsing" +
                        "of a Json string");
                throw new ArrestGameException("ArrestGameException:" +
                        "Error occurred in GoalManager at the reading of the JsonReader", e);
            }
            finally {
                reader.close();
            }
        }catch (IOException e) {
            System.err.println("Error occurred in Goal Manager: IOException occurred with file " +
                    setUpFile + " at the closing of the reader");
            System.err.println("More details: " + e.toString());
            throw new ArrestGameException("ArrestGameException:" +
                    "Error occurred in GoalManager at the closing of the JsonReader", e);
        }


        // creating default managers, in future to add another manager add it here
        this.commonGoalCardManager = new CommonGoalCardManager(players, new Deck(patternsCommonGoals));
        this.personalGoalCardManager = new PersonalGoalCardManager(players, new Deck(patternsPersonalGoals));
        this.endGamePointsManager = new EndGamePointsManager(players, patternsEndGoals);

        this.pointsManagers.add(this.commonGoalCardManager);
        this.pointsManagers.add(this.personalGoalCardManager);
        this.pointsManagers.add(this.endGamePointsManager);

    }

    public void updatePointsTurn() {
        // NB: updatePointsTurn() can't actually update the points for each PointsManager
        // this issue is caused from the fact that points are stored in the player and
        // therefore are supposed to be updated just once (at the end of the game)
        // this function actually just updates the CommonGoalCardManager (which needs to be updated every turn)
        // (but does not update the points relative to the CommonGoalCardManager!, just its state)
        // a updatePointsTurn will be added to every manager, if points will be moved
        // to the manager itself this will allow to display updated player points every turn
        // and not just at the end of the game
        pointsManagers.forEach(PointsManager::updatePointsTurn);
    }

    public void updatePointsEnd() {
        pointsManagers.forEach(PointsManager::updatePoints);
    }

    // good for now, might want to clone or send a simplified version of these objects for security reasons (again)
    /**
     * @return a map associating cards to tokens
     */
    // this method does not take a card as input because cards are handled exclusively  by CardsManagers
    public Map<Card, Stack<Token>> getCommonCardsToTokens() {
        return commonGoalCardManager.getCardsToTokens();
    }

    /**
     * @param player the player
     * @return the tokens of the player:
     */
    public Set<Token> getPlayerTokens(Player player) {
        return commonGoalCardManager.getPlayerTokens(player);
    }

    /**
     * @param player the player
     * @return the unfulfilled cards of the player
     */
    public Set<Card> getPlayerCommonUnfulfilledCards(Player player) {
        return commonGoalCardManager.getPlayerUnfulfilledCards(player);
    }
    /**
     * @param player the player
     * @return the fulfilled cards of the player
     */
    public Set<Card> getPlayerCommonFulfilledCards(Player player) {
        return commonGoalCardManager.getPlayerFulfilledCards(player);
    }
    /**
     * @param player the player
     * @return the personal card of the player
     */
    public Card getPlayerPersonalCards(Player player) {
        return personalGoalCardManager.getPlayerCard(player);
    }
}
