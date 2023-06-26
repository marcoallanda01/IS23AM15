package it.polimi.ingsw.client.communication;

import it.polimi.ingsw.communication.responses.GameSetUp;
import it.polimi.ingsw.server.model.Tile;
import it.polimi.ingsw.server.model.TileType;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * this class should be implemented from the client class that handles the notification,
 * being it a controller or a directly the view,
 * the client MUST use only one instance of this class
 */
public interface ClientNotificationListener {
    /**
     * Notifies the GameSetUp
     * @param gameSetUp the GameSetUp
     */
    void notifyGame(GameSetUp gameSetUp);
    /**
     * Notifies the winner
     * @param nickname the nickname of the winner
     */
    void notifyWinner(String nickname);
    /**
     * Notifies a board update
     * @param tiles the tiles of the board
     */
    void notifyBoard(Set<Tile> tiles);
    /**
     * Notifies a bookshelf update
     * @param nickname the nickname of the bookshelf owner
     * @param tiles the tiles of the bookshelf
     */
    void notifyBookshelf(String nickname, Set<Tile> tiles);
    /**
     * Notifies a points update
     * @param nickname the nickname of the player whose points changed
     * @param points the points of the player
     */
    void notifyPoints(String nickname, int points);
    /**
     * Notifies a turn update
     * @param nickname the nickname of the player whose turn started
     */
    void notifyTurn(String nickname);
    /**
     * Notifies the personal goal card of the player
     * @param nickname the nickname of the player
     * @param card the card of the player
     */
    void notifyPersonalGoalCard(String nickname, String card);
    /**
     * Notifies the common goal cards with their tokens
     * @param cardsToTokens the cards to the tokens on each card
     */
    void notifyCommonCards(Map<String, List<Integer>> cardsToTokens);
    /**
     * Notifies the common goals
     * @param goals the common goals
     */
    void notifyCommonGoals(Set<String> goals);
    /**
     * Notifies a chat message
     * @param nickname the nickname of the sender
     * @param message the content of the message
     * @param date the timestamp of the sending
     */
    void notifyChatMessage(String nickname, String message, String date);
    /**
     * Notifies a disconnection
     * @param nickname the nickname of disconnected player
     */
    void notifyDisconnection(String nickname);
    /**
     * Notifies the successfully saved game
     * @param game the name of the save
     */
    void notifyGameSaved(String game);
    /**
     * Notifies the ping
     */
    void notifyPing();
    /**
     * Notifies a reconnection
     * @param nickname the nickname of disconnected player
     */
    void notifyReconnection(String nickname);
    /**
     * Notifies a first join response
     * @param result the result
     */
    void notifyFirstJoinResponse(boolean result);
    /**
     * Notifies the available names given the loaded game
     * @param nicknames the nicknames of the players
     */
    void notifyLoadedGamePlayers(Set<String> nicknames);

    /**
     * Notifies the hello, communicating the status of the server
     * @param lobbyReady true if the lobby is ready
     * @param firstPlayerId 'NoFirst' if it is not a first player, otherwise the first player id
     * @param loadedGame true if the game is loaded
     */
    void notifyHello(boolean lobbyReady, String firstPlayerId, boolean loadedGame);

    /**
     * Notifies the saved games (that can be loaded)
     * @param games the saved games
     */
    void notifySavedGames(Set<String> games);

    /**
     * Notifies the join response
     * @param result the result
     * @param error the error, if any
     * @param id the id of the player
     */
    void notifyJoinResponse(boolean result, String error, String id);

    /**
     * Notifies the load game response
     * @param result the result
     * @param error the error
     */
    void notifyLoadGameResponse(boolean result, String error);

    /**
     * Notifies a generic error
     * @param message the error message
     */
    void notifyError(String message);

    /**
     * Notifies the tiles picked from the player
     * @param nickname the player who picked the tiles
     * @param tiles the tiles
     */
    void notifyPickedTiles(String nickname, List<TileType> tiles);
}
