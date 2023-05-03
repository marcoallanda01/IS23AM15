package it.polimi.ingsw.communication.rmi;

import it.polimi.ingsw.communication.responses.*;
import it.polimi.ingsw.server.controller.ChatController;
import it.polimi.ingsw.server.controller.Lobby;
import it.polimi.ingsw.server.controller.PlayController;
import it.polimi.ingsw.server.model.Tile;
import it.polimi.ingsw.server.model.TileType;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Set;

public interface RMIServer extends Remote {

    // Methods for the connection and lobby creation
    public Hello hello() throws RemoteException;
    public SavedGames getSavedGames() throws RemoteException;
    public BooleanResponse joinFirstPlayer(String name, int numPlayersGame, String id) throws RemoteException;
    public BooleanResponse joinFirstPlayer(String name, int numPlayersGame, String id, boolean easyRules) throws RemoteException;
    public LoadGameResponse loadGame(String name, String idFirstPlayer) throws RemoteException;
    public LoadedGamePlayers getLoadedGamePlayers() throws RemoteException;
    public BooleanResponse joinLoadedGameFirstPlayer(String name, String id) throws RemoteException;
    public JoinResponse join(String player) throws RemoteException;
    public BooleanResponse disconnect(String playerId) throws RemoteException;
    public BooleanResponse reconnect(String playerId) throws RemoteException;
    public BooleanResponse isFistPlayerPresent() throws RemoteException;
    public BooleanResponse pickTiles(Set<Tile> tiles) throws RemoteException;
    public BooleanResponse putTiles(List<TileType> tiles, int column) throws RemoteException;
    public void pong() throws RemoteException;
    void login(RMIClient rmiClient) throws RemoteException;
}
