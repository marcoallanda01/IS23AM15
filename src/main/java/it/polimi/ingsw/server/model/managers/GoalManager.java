package it.polimi.ingsw.server.model.managers;

import java.io.*;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.function.Predicate;

import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import it.polimi.ingsw.server.model.*;
import it.polimi.ingsw.server.model.exceptions.ArrestGameException;
import it.polimi.ingsw.server.model.exceptions.InvalidPatternParameterException;
import it.polimi.ingsw.server.model.managers.patterns.AdjacentPattern;
import it.polimi.ingsw.server.model.managers.patterns.Pattern;
import it.polimi.ingsw.server.model.managers.patterns.PersonalPattern;
import it.polimi.ingsw.server.model.managers.patterns.SpecificPattern;
import org.jetbrains.annotations.NotNull;

/**
 * Class used to load the goals, handle all the managers and calculate total points
 */
public class GoalManager{
    private final List<PointsManager> pointsManagers = new ArrayList<>();
    private final CommonCardsPointsManager commonCardsPointsManager;
    private final PersonalCardsPointsManager personalCardsPointsManager;
    private final CommonGoalsPointsManager commonGoalsPointsManager;
    private final int commonCardsToDraw;
    /**
     * if set to false only pointsManagers with updateRule set to END_TURN will be updated every turn
     * if set to true only pointsManagers with updateRule set to END_GAME will NOT be updated every turn
     */
    private Boolean frequentUpdates = Boolean.TRUE;

    /**
     * Read a card from
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
            } catch (NullPointerException e){
                System.err.printf(
                        "NullPointerException: The loading of the %d pattern in %s is failed. This is probably"
                                + " caused by absent files in the card.\nException Message: %s", i,
                        cardsType, e.getMessage());
            }catch (RuntimeException e) {
                // Here I can go on with the loading of others cards
                System.err.printf(
                        "RuntimeException: The loading of the %d pattern in %s is failed. This is probably"
                                + " caused by wrong formatting of the file.\nException Message: %s", i,
                        cardsType, e.getMessage());
            }
        }

        return patterns;
    }

    /**
     * Read a pattern
     * @param cardJson JsonElement representing the card
     * @return pattern to be generated from parsing. Returns null if pattern is invalid
     * @throws RuntimeException if there is bad formatting in the file, but we must throw so the GoalManager can try to
     *                          read the other patterns
     */
    private Pattern readPattern(JsonElement cardJson) throws RuntimeException {
        Pattern pattern;

        JsonObject patternJ = cardJson.getAsJsonObject();
        JsonElement nameElement = patternJ.get("name");
        if(nameElement == null){
            throw new JsonParseException("name parameter absent");
        }
        JsonElement typeElement = patternJ.get("type");
        if(typeElement == null){
            throw new JsonParseException("type parameter absent");
        }
        String name = nameElement.getAsString();
        String type = typeElement.getAsString();
        switch (type) {
            case "specific" -> {
                int groupNum = patternJ.get("group_num").getAsInt();
                int maxC = patternJ.get("max").getAsInt();
                int minC = patternJ.get("min").getAsInt();
                boolean sgc = patternJ.get("sgc").getAsString().equals("Y");
                boolean fe = patternJ.get("fe").getAsString().equals("Y");
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
                try {
                    pattern = new SpecificPattern(name, matrixs, groupNum, sgc, minC, maxC, fe);
                } catch (InvalidPatternParameterException e) {
                    throw new RuntimeException(e);
                }
            }
            case "adjacent" -> {
                int minTiles = patternJ.get("min_tiles").getAsInt();
                int maxTiles = patternJ.get("min_groups").getAsInt();
                int points = patternJ.get("points").getAsInt();
                try {
                    pattern = new AdjacentPattern(name, minTiles, maxTiles, points);
                } catch (InvalidPatternParameterException e) {
                    throw new RuntimeException(e);
                }
            }
            case "personal" -> {
                JsonArray tilesJ = patternJ.get("tiles").getAsJsonArray();
                Set<Tile> tiles = new HashSet<>();
                for (int i = 0; i < tilesJ.size(); i++) {
                    JsonArray tileJ = tilesJ.get(i).getAsJsonArray();
                    int x = tileJ.get(0).getAsInt();
                    int y = tileJ.get(1).getAsInt();
                    String tileTypeName = tileJ.get(2).getAsString();
                    TileType tileType = TileType.tileTypeFromName(tileTypeName);
                    if(tileType == null){
                        throw new IllegalStateException("Invalid tileType in personal("+tileTypeName+")");
                    }
                    Tile t = new Tile(x, y, tileType);
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
                try {
                    pattern = new PersonalPattern(name, tiles, checkToPoints);
                } catch (InvalidPatternParameterException e) {
                    throw new RuntimeException(e.getMessage());
                }
            }
            default -> throw new RuntimeException("No card exists with type: "+type);
        }

        return pattern;
    }

    /**
     * gets the file reader given a file
     * @param path file path
     * @param intern true if the file is from internal resources
     * @return the file reader
     * @throws FileNotFoundException if the file is not found
     * @throws URISyntaxException if there were problems with the uri interface
     */
    private Reader getSetupFileReader (String path, boolean intern) throws FileNotFoundException, URISyntaxException {
        if (intern) {
            return new InputStreamReader(getClass().getClassLoader().getResourceAsStream(path),  StandardCharsets.UTF_8);
        }
        return new FileReader(path);
    }

    /**
     * Reads settings from json file and initializes the managers accordingly
     * @param players   players playing the game
     * @param setUpFile file json where there are the patterns associated with card type
     * @param isFirstGame true if is the first game of the players (game rule)
     * @param fromInternResources true if setUpFile used is from internal resources
     * @throws ArrestGameException if occurred very bad errors in parsing or json stream
     */
    public GoalManager(List<Player> players, String setUpFile, boolean isFirstGame, boolean fromInternResources) throws ArrestGameException {
        this.commonCardsToDraw = isFirstGame ? 1 : 2;

        Set<Pattern> patternsCommonGoals = new HashSet<>();
        Set<Pattern> patternsPersonalGoals = new HashSet<>();
        LinkedHashSet<Pattern> patternsEndGoals = new LinkedHashSet<>();

        try {
            JsonReader reader1 = new JsonReader(getSetupFileReader(setUpFile, fromInternResources));
            patternsCommonGoals.addAll(readCards(reader1, "common_cards"));
            reader1.close();

            JsonReader reader2 = new JsonReader(getSetupFileReader(setUpFile, fromInternResources));
            patternsEndGoals.addAll(readCards(reader2, "end_game"));
            reader2.close();


            for(Pattern p : patternsEndGoals){
                System.out.println("Pattern: "+p.toString());
            }

            for(Pattern p : patternsCommonGoals){
                System.out.println("Pattern: "+p.toString());
            }
            if(patternsCommonGoals.size() < commonCardsToDraw){
                throw new ArrestGameException("Not enough common cards as game rules are set");
            }

            JsonReader reader3 = new JsonReader(getSetupFileReader(setUpFile, fromInternResources));
            patternsPersonalGoals.addAll(readCards(reader3, "personal_cards"));
            if(patternsPersonalGoals.size() < players.size()){
                throw new ArrestGameException("Not enough personal cards form all players");
            }
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
            throw new ArrestGameException("URISyntaxException occurred!");
        }catch (NullPointerException e){
            System.err.println("Error occurred in Goal Manager: NullPointerException occurred with file " + setUpFile + " at its opening");
            throw new ArrestGameException("ArrestGameException:" + "Error occurred in GoalManager at the opening of the file", e);
        }


        // creating default managers
        this.commonCardsPointsManager = new CommonCardsPointsManager(players, new Deck(patternsCommonGoals), this.commonCardsToDraw);
        this.personalCardsPointsManager = new PersonalCardsPointsManager(players, new Deck(patternsPersonalGoals));
        this.commonGoalsPointsManager = new CommonGoalsPointsManager(players, patternsEndGoals);

        this.pointsManagers.add(this.commonCardsPointsManager);
        this.pointsManagers.add(this.personalCardsPointsManager);
        this.pointsManagers.add(this.commonGoalsPointsManager);
    }

    /**
     * Constructor of GoalManager when game is loaded
     * @param commonCardsPointsManager commonCardsPointsManager
     * @param personalCardsPointsManager personalCardsPointsManager
     * @param commonGoalsPointsManager commonGoalsPointsManager
     * @param frequentUpdates frequentUpdates
     * @param CommonCardsToDraw CommonCardsToDraw
     */
    public GoalManager(CommonCardsPointsManager commonCardsPointsManager, PersonalCardsPointsManager personalCardsPointsManager, CommonGoalsPointsManager commonGoalsPointsManager,
                       Boolean frequentUpdates, int CommonCardsToDraw) {
        this.commonCardsToDraw = CommonCardsToDraw;
        this.commonCardsPointsManager = commonCardsPointsManager;
        this.personalCardsPointsManager = personalCardsPointsManager;
        this.commonGoalsPointsManager = commonGoalsPointsManager;
        this.pointsManagers.add(this.commonCardsPointsManager);
        this.pointsManagers.add(this.personalCardsPointsManager);
        this.pointsManagers.add(this.commonGoalsPointsManager);
        this.frequentUpdates = frequentUpdates;
    }

    /**
     * Update the points at the end of a turn
     * @param player the player
     *               updates the points of the player relative to every pointsManager that should be updated every turn
     * if frequent is set to true, unless the pm specifies end game updates, it will be updated
     * if frequent is set to false, unless the pm specifies end turn updates, it will NOT be updated
     */
    public void updatePointsTurn(Player player) {
        if(player != null) {
            Predicate<PointsManager> toUpdate = frequentUpdates ? pointsManager -> !pointsManager.getUpdateRule().equals(UpdateRule.END_GAME) :
                    pointsManager -> pointsManager.getUpdateRule().equals(UpdateRule.END_TURN);
            pointsManagers.stream().filter(toUpdate)
                    .forEach(pointsManager -> pointsManager.updatePoints(player));
            player.setPoints(this.getPoints(player));
            System.out.println(player.getUserName() + " points: " + this.getPoints(player));
            System.out.println(pointsManagers);
        }
    }

    /**
     * Update the points at the end of the game
     * @param player the player
     *               updates the points of the player relative to every pointsManager
     */
    public void updatePointsEnd(Player player) {
        if(player != null){
            pointsManagers.forEach(PointsManager -> PointsManager.updatePoints(player));
        }
    }

    /**
     * Get player's points
     * @param player player
     * @return points
     */
    public int getPoints(Player player) {

        return pointsManagers.stream()
                .map(pointsManager -> pointsManager.getPoints(player)).reduce(0, Integer::sum)
                + (player.isFirstToFinish() ? 1 : 0);
    }

    /**
     * Get player with most points
     * @param players list of the player among us calculate the winner
     * @return player with most points
     */
    public String getWinner(@NotNull List<Player> players){
        for(Player player : players){
            updatePointsEnd(player);
        }
        //Last update of player points
        players.forEach(this::updatePointsEnd);
        players.forEach((p) -> p.setPoints(this.getPoints(p)));
        // only currently playing players are considered
        return players.stream().filter(Player::isPlaying).max(Comparator.comparingInt(this::getPoints)).get().getUserName();
    }

    /**
     * Get common cards with their associated tokens
     * @return a map associating cards to tokens
     */
    // this method does not take a card as input because cards are handled exclusively  by CardsManagers
    public Map<Pattern, Stack<Integer>> getCommonCardsToTokens() {
        return commonCardsPointsManager.getCardsToTokens();
    }

    /**
     * Get the tokens of a player
     * @param player the player
     * @return the tokens of the player:
     */
    public List<Integer> getTokens(Player player) {
        return commonCardsPointsManager.getTokens(player);
    }

    /**
     * Get common cards that a player didn't complete
     * @param player the player
     * @return the unfulfilled cards of the player
     */
    public Set<Pattern> getUnfulfilledCommonCards(Player player) {
        return commonCardsPointsManager.getUnfulfilledCards(player);
    }

    /**
     * Get common cards that a player did complete
     * @param player the player
     * @return the fulfilled cards of the player
     */
    public Set<Pattern> getFulfilledCommonCards(Player player) {
        return commonCardsPointsManager.getFulfilledCards(player);
    }

    /**
     * Get personal card of a player
     * @param player the player
     * @return the personal card of the player
     */
    public Pattern getPersonalCard(Player player) {
        return personalCardsPointsManager.getCard(player);
    }

    /**
     * Get common goals patterns
     * @return patterns
     */
    public Set<Pattern> getCommonGoals() {
        return commonGoalsPointsManager.getPatterns();
    }

    /**
     * Used for serialization.
     * Get the CommonCardsPointsManager
     * @return CommonCardsPointsManager object
     */
    public CommonCardsPointsManager getCommonCardsPointsManager() {
        return commonCardsPointsManager;
    }

    /**
     * Used for serialization.
     * Get the getPersonalGoalCardManager
     * @return getPersonalGoalCardManager object
     */
    public PersonalCardsPointsManager getPersonalGoalCardManager() {
        return personalCardsPointsManager;
    }

    /**
     * Used for serialization.
     * Get the CommonGoalsPointsManager
     * @return CommonGoalsPointsManager object
     */
    public CommonGoalsPointsManager getCommonGoalsPointsManager() {
        return commonGoalsPointsManager;
    }

    /**
     * Used for serialization.
     * Get FrequentUpdates mode
     * @return true if FrequentUpdates mode is active
     */
    public Boolean isFrequentUpdates() {
        return frequentUpdates;
    }

    /**
     * Used for serialization.
     * Get the getCommonCardsToDraw
     * @return getCommonCardsToDraw object
     */
    public int getCommonCardsToDraw() {
        return commonCardsToDraw;
    }
}
