package it.polimi.ingsw.server.communication;

import it.polimi.ingsw.server.communication.responses.*;
import it.polimi.ingsw.server.model.Tile;

import java.util.List;

public interface ClientCommunication {

    // Methods for the connection and lobby creation
    public Hello hello();
    public SavedGames getSavedGames();
    public BooleanResponse joinFirstPlayer(String name, int numPlayersGame, String id);
    public BooleanResponse joinFirstPlayer(String name, int numPlayersGame, String id, boolean easyRules);
    public LoadGameResponse loadGame(String name, String idFirstPlayer);
    /**
     * @return null if the game is not loaded, but created (or there is no game), else return the list of possible players
     */
    public LoadedGamePlayers getLoadedGamePlayers();
    public BooleanResponse joinLoadedGameFirstPlayer(String name, String id);

    /**
     * Use this function to join the game without creating it
     * @param player player name
     * @return JoinResponse
     */
    public JoinResponse join(String player);

    /**
     * This function remove the player from the lobby if the game is not started, otherwise send it to
     * the WC (disconnection resilience)
     * @param playerId id of the player
     */
    public BooleanResponse disconnect(String playerId);
    public BooleanResponse isFistPlayerPresent();

    // Methods for the play of the turn
    public BooleanResponse pickTiles(List<Tile> tiles);

    //TODO: to be discussed "param: tiles"
    public BooleanResponse putTiles(List<Tile> tiles, int column);


    public void pong();

}
