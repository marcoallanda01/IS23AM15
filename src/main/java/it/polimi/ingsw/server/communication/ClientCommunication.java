package it.polimi.ingsw.server.communication;

import it.polimi.ingsw.communication.responses.*;
import it.polimi.ingsw.server.model.Tile;
import it.polimi.ingsw.server.model.TileRule;
import it.polimi.ingsw.server.model.TileType;

import java.util.List;
import java.util.Set;

public interface ClientCommunication {

    // Methods for the connection and lobby creation
    public Hello hello();
    public BooleanResponse joinNewAsFirst(String player, int numPlayersGame, String idFirstPlayer);
    public BooleanResponse joinNewAsFirst(String player, int numPlayersGame, String idFirstPlayer, boolean easyRules);
    /**
     * Use this function to join the game without creating it
     * @param player player name
     * @return JoinResponse
     */
    public JoinResponse join(String player);

    public SavedGames getSavedGames();
    public LoadGameResponse loadGame(String game, String idFirstPlayer);
    /**
     * @return null if the game is not loaded, but created (or there is no game), else return the list of possible players
     */
    public LoadedGamePlayers getLoadedGamePlayers();
    public BooleanResponse joinLoadedAsFirst(String player, String idFirstPlayer);
    /**
     * This function remove the player from the lobby if the game is not started, otherwise send it to
     * the WC (disconnection resilience)
     * @param playerId id of the player
     */
    public BooleanResponse disconnect(String playerId);
    public BooleanResponse reconnect(String playerId);
    // Methods for the play of the turn
    public BooleanResponse pickTiles(String playerId, Set<Tile> tiles);
    public BooleanResponse putTiles(String playerId, List<TileType> tiles, int column);
    public BooleanResponse sendMessage(String playerId, String player, String message);
    public void pong(String playerId);

}
