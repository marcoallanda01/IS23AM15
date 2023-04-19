package it.polimi.ingsw.communication;

import it.polimi.ingsw.controller.*;

import java.util.List;

public interface GameServer {
    public Hello hello(); // WaitLobbyException;

    public SavedGames getSavedGames();


    public BooleanResponse joinFirstPlayer(String name, int numPlayersGame, String id);
    public BooleanResponse joinFirstPlayer(String name, int numPlayersGame, String id, boolean easyRules);
    public LoadGameResponse loadGame(String name, String idFirstPlayer); // throws GameLoadException, GameNameException, IllegalLobbyException;
    /**
     * @return null if the game is not loaded, but created (or there is no game), else return the list of possible players
     */
    public LoadedGamePlayers getLoadedGamePlayers();
    public BooleanResponse joinLoadedGameFirstPlayer(String name, String id);

    public JoinResponse join(String player); // throws FullGameException, NicknameTakenException, NicknameException;

    public BooleanResponse disconnect(String player);

    public BooleanResponse isFistPlayerPresent();


    // TODO: methods not needed because the game MUST start as soon as the full capacity is reached
    // public BooleanResponse isReadyToPlay();
    // public BooleanResponse startGame() throws EmptyLobbyException;
}
