package it.polimi.ingsw.modules;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

import com.google.gson.stream.JsonReader;

public class GoalManager {
    private List<Player> players;
    private List<PointsManager> pointsManagers = new ArrayList<>();

    private CommonGoalCardManager commonGoalCardManager;
    private PersonalGoalCardManager personalGoalCardManager;
    private EndGamePointsManager endGamePointsManager;

    private List<List<Pattern>> patterns;

    private List<List<Pattern>> readJsonStream(FileReader in) throws IOException {
        JsonReader reader = new JsonReader(in);
        List<List<Pattern>> patterns = new ArrayList<>();
        try {
            patterns.add(readCommonCards(reader));
            patterns.add(readEndGame(reader));
            patterns.add(readPersonalCard(reader));
        } finally {
            reader.close();
        }
        return patterns;
    }

    private List<Pattern> readCommonCards(JsonReader reader) throws IOException{
        List<Pattern> patterns = new ArrayList<>();

        reader.beginArray();
        try{
            while (reader.hasNext()) {
                Pattern pattern = readPattern(reader);
                if(pattern != null)
                    patterns.add(pattern);
            }
        } catch (IOException e) {
            System.err.println("qui");
        }
        reader.endArray();

        return patterns;
    }

    private List<Pattern> readEndGame(JsonReader reader) throws IOException{
        List<Pattern> patterns = new ArrayList<>();

        reader.beginArray();
        try{
            while (reader.hasNext()) {
                Pattern pattern = readPattern(reader);
                if(pattern != null)
                    patterns.add(pattern);
            }
        } catch (IOException e) {
            System.err.println("qui");
        }
        reader.endArray();

        return patterns;
    }

    private List<Pattern> readPersonalCard(JsonReader reader) throws IOException{
        List<Pattern> patterns = new ArrayList<>();


        return patterns;
    }

    private Pattern readPattern(JsonReader reader) throws IOException{
        Pattern pattern = null;

        reader.beginObject(); // throws IOException
        try{
            String name = reader.nextString();
            String type = reader.nextString();
            int grup_num = 0;
            switch (type){
                case "specific":
                    while (reader.hasNext()) {
                        String x = reader.nextName();
                    }
                case "line" :

                case "adjacent" :

            }
        }
        catch(IllegalStateException e){
            System.err.println(e.toString());
        }
        reader.endObject();

        return pattern;
    }


    public GoalManager(List<Player> players, String setUpFile) throws ArrestGameException {
        this.players = new ArrayList<Player>(players);

        // I use list and not set because I could choose to have to same card so the probability increase
        List<Pattern> commonGoalCardPatternsToNames = new ArrayList<Pattern>();
        List<Pattern> personalGoalCardManagerPatterns = new ArrayList<Pattern>();

        // Just only one pattern for every goal that is in common and all are active at the same time
        Set<Pattern> EndGamePointsManagerPatterns = new HashSet<Pattern>();

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
            readJsonStream(in);
        }catch (IOException e) {
            System.err.println("Error occurred in Goal Manager: IOException occurred with file " + setUpFile + " bad formatted");
            System.err.println("More details: " + e.toString());
            throw new ArrestGameException("ArrestGameException: Error occurred in GoalManager", e);
        }


        // creating default managers, in future to add another manager add it here
        this.commonGoalCardManager = new CommonGoalCardManager(players, new Deck(commonGoalCardPatternsToNames));
        this.personalGoalCardManager = new PersonalGoalCardManager(players, new Deck(personalGoalCardManagerPatterns));
        this.endGamePointsManager = new EndGamePointsManager(players, EndGamePointsManagerPatterns);

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
        pointsManagers.forEach(pointsmanager -> pointsmanager.updatePointsTurn());
    }

    public void updatePointsEnd() {
        pointsManagers.forEach(pointsmanager -> pointsmanager.updatePoints());
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

    public List<List<Pattern>> getPatterns() {
        return new ArrayList<>(this.patterns);
    }
}
