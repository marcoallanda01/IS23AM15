package it.polimi.ingsw.server.communication;

import it.polimi.ingsw.server.communication.responses.Msg;

import java.util.Set;

/**
 * GameSetUP is the object sent to the player at the beginning of a starting game, it includes all type of objectives.
 * It doesn't include player points and tiles in the board/bookshelf because there are notifyChangeBoard,
 * notifyChangeBookShelf, updatePlayerPoints, notifyYourTurn
 */
public class GameSetUp extends Msg {
    public String personalCard; // code of the personal card
    public Set<String> commonCards; // codes of common cards
    public Set<String> commonGoals; // codes of commonGoals
    public Set<String> players; // names of the players

    public GameSetUp(String personalCard, Set<String> commonCards, Set<String> commonGoals, Set<String> players) {
        super("GameSetUp");
        this.personalCard = personalCard;
        this.commonCards = commonCards;
        this.commonGoals = commonGoals;
        this.players = players;
    }
}
