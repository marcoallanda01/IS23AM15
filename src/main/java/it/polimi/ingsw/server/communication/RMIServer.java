package it.polimi.ingsw.server.communication;
import it.polimi.ingsw.communication.RMIClient;
import it.polimi.ingsw.server.controller.WaitLobbyException;
import it.polimi.ingsw.server.controller.*;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.*;

public interface RMIServer extends Remote{
    public Optional<String> hello(RMIClient client) throws RemoteException, WaitLobbyException;

    /**
     * @return null if the game is not loaded, but created (or there is no game), else return the list of possible players
     */
    public List<String> getLoadedGamePlayers();
    public Set<String> getSavedGames();
    public boolean joinFirstPlayer(String name, int numPlayersGame, String id);
    public List<String> loadGame(String name, String idFirstPlayer) throws GameLoadException, GameNameException,
            IllegalLobbyException;

    public boolean joinLoadedGameFirstPlayer(String name, String id) throws NicknameException;

    public boolean joinLoadedGameFirstPlayer(String name, String id, boolean easyRules) throws NicknameException;

    public String join(String player) throws FullGameException, NicknameTakenException, NicknameException;

    public boolean disconnect(String player);

    public boolean isFistPlayerPresent();

    public boolean isReadyToPlay();

    public ControllerProvider startGame() throws EmptyLobbyException;


}
