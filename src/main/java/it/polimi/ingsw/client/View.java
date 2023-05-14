package it.polimi.ingsw.client;

import it.polimi.ingsw.communication.responses.CommonCards;
import it.polimi.ingsw.communication.responses.GameSetUp;
import it.polimi.ingsw.client.communication.ClientCommunication;
import it.polimi.ingsw.server.model.BookShelf;
import it.polimi.ingsw.server.model.Chat;
import it.polimi.ingsw.server.model.LivingRoomBoard;
import it.polimi.ingsw.server.model.Tile;

import java.util.List;
import java.util.Map;
import java.util.Set;

public abstract class View {
    protected String nickname;
    protected Set<Tile> bookShelf;
    protected List<String> players;
    protected Map<String,Set<Tile>> otherBookShelves;
    protected Set<Tile> livingRoomBoard;
    protected int numberOfPlayers;
    protected boolean easyRules;
    protected Map<String,Map<String,String>> chat;
    protected List<String> goals;
    protected Map<String,Integer> points;
    protected String currentTurnPlayer;
    protected String personalGoal;
    protected Map<String,List<Integer>> commonGoalsCards;
    protected Set<String> commonGoals;
    protected String game;
    protected List<String> savedGames;
    public void render(){}
    public void showError(String error){}
    public void showChat(){}
}
