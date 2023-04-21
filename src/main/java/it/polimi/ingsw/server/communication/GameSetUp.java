package it.polimi.ingsw.server.communication;

import it.polimi.ingsw.server.model.Adjacent;

import java.util.List;
import java.util.Set;

/**
 * GameSetUP is the object sent to the player at the beginning of a starting game, it includes all type of objectives.
 * It doesn't include player points and tiles in the board/bookshelf because there are notifyChangeBoard,
 * notifyChangeBookShelf, updatePlayerPoints, notifyYourTurn
 */
public class GameSetUp extends Msg{
    public String personalCard; // code of the personal card
    public Set<String> commonCards; // codes of common cards
    public Set<String> adjacentCards; // codes of adjacentCards

    public GameSetUp(String personalCard, Set<String> commonCards, Set<String> adjacentCards) {
        super("GameSetUp");
        this.personalCard = personalCard;
        this.commonCards = commonCards;
        this.adjacentCards = adjacentCards;
    }
}
