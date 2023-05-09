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
    public Hello hello() throws RemoteException;
    public void joinNewAsFirst(RMIClient client, String player, int numPlayersGame, String idFirstPlayer) throws RemoteException;
    public void joinNewAsFirst(RMIClient client, String player, int numPlayersGame, String idFirstPlayer, boolean easyRules) throws RemoteException;
    public JoinResponse join(RMIClient client, String player) throws RemoteException;
    public SavedGames getSavedGames() throws RemoteException;
    public LoadGameResponse loadGame(String game, String idFirstPlayer) throws RemoteException;
    public LoadedGamePlayers getLoadedGamePlayers() throws RemoteException;
    public void joinLoadedAsFirst(RMIClient client, String player, String idFirstPlayer) throws RemoteException;
    public void disconnect(String playerId) throws RemoteException;
    public void reconnect(RMIClient client, String playerId) throws RemoteException;
    public void pickTiles(String playerId, Set<Tile> tiles) throws RemoteException;
    public void putTiles(String playerId, List<TileType> tiles, int column) throws RemoteException;
    public void saveGame(String playerId, String gameName) throws RemoteException;
    public void sendMessage(String playerId, String player, String message) throws RemoteException;
    public void pong(String playerId) throws RemoteException;

}
