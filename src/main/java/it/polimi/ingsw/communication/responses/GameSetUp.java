package it.polimi.ingsw.communication.responses;

import java.util.List;

public class GameSetUp extends Msg{
    public List<String> players;
    

    public GameSetUp(){
        super("GameSetUp");
    }
}
