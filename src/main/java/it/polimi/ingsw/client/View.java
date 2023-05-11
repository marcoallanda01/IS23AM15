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
    private String nickname;
    private Set<Tile> bookShelf;
    private List<String> players;
    private Map<String,Set<Tile>> otherBookShelves;
    private Set<Tile> livingRoomBoard;
    private Map<String,Map<String,String>> chat;
    private List<String> goals;
    private Map<String,Integer> points;
    private String currentTurnPlayer;
    private String personalGoal;
    private Map<String,List<Integer>> commonGoalsCards;
    private Set<String> commonGoals;
    private String game;
    private Set<String> savedGames;
    public void render(){}
    public void showError(String error){}
    public void showChat(){}
}
