package it.polimi.ingsw.client.communication;

import it.polimi.ingsw.communication.responses.*;
import it.polimi.ingsw.server.communication.ClientCommunication;
import it.polimi.ingsw.server.model.Tile;
import it.polimi.ingsw.server.model.TileType;

import java.rmi.RemoteException;
import java.util.List;
import java.util.Set;

public class RMIClientCommunication implements ClientCommunication {

    private RMIClientConnection rmiClientConnection;
    public RMIClientCommunication(RMIClientConnection rmiClientConnection) {
        this.rmiClientConnection = rmiClientConnection;
    }

    @Override
    public Hello hello() {
        try {
            return this.rmiClientConnection.getServer().hello();
        } catch (RemoteException e) {
            throw new ClientCommunicationException();
        }
    }

    @Override
    public BooleanResponse joinNewAsFirst(String player, int numPlayersGame, String idFirstPlayer) {
        try {
            return this.rmiClientConnection.getServer().joinNewAsFirst(player, numPlayersGame, idFirstPlayer);
        } catch (RemoteException e) {
            throw new ClientCommunicationException();
        }
    }

    @Override
    public BooleanResponse joinNewAsFirst(String player, int numPlayersGame, String idFirstPlayer, boolean easyRules) {
        try {
            return this.rmiClientConnection.getServer().joinNewAsFirst(player, numPlayersGame, idFirstPlayer, true);
        } catch (RemoteException e) {
            throw new ClientCommunicationException();
        }
    }

    @Override
    public SavedGames getSavedGames() {
        try {
            return this.rmiClientConnection.getServer().getSavedGames();
        } catch (RemoteException e) {
            throw new ClientCommunicationException();
        }
    }

    @Override
    public LoadGameResponse loadGame(String game, String idFirstPlayer) {
        try {
            return this.rmiClientConnection.getServer().loadGame(game, idFirstPlayer);
        } catch (RemoteException e) {
            throw new ClientCommunicationException();
        }
    }

    @Override
    public LoadedGamePlayers getLoadedGamePlayers() {
        try {
            return this.rmiClientConnection.getServer().getLoadedGamePlayers();
        } catch (RemoteException e) {
            throw new ClientCommunicationException();
        }
    }

    @Override
    public BooleanResponse joinLoadedAsFirst(String player, String idFirstPlayer) {
        try {
            return this.rmiClientConnection.getServer().joinLoadedAsFirst(player, idFirstPlayer);
        } catch (RemoteException e) {
            throw new ClientCommunicationException();
        }
    }

    @Override
    public JoinResponse join(String player) {
        try {
            return this.rmiClientConnection.getServer().join(player);
        } catch (RemoteException e) {
            throw new ClientCommunicationException();
        }
    }

    @Override
    public BooleanResponse disconnect(String playerId) {
        try {
            return this.rmiClientConnection.getServer().disconnect(playerId);
        } catch (RemoteException e) {
            throw new ClientCommunicationException();
        }
    }

    @Override
    public BooleanResponse reconnect(String playerId) {
        try {
            return this.rmiClientConnection.getServer().reconnect(playerId);
        } catch (RemoteException e) {
            throw new ClientCommunicationException();
        }
    }

    @Override
    public BooleanResponse pickTiles(String playerId, Set<Tile> tiles) {
        try {
            return this.rmiClientConnection.getServer().pickTiles(playerId, tiles);
        } catch (RemoteException e) {
            throw new ClientCommunicationException();
        }
    }

    @Override
    public BooleanResponse putTiles(String playerId, List<TileType> tiles, int column) {
        try {
            return this.rmiClientConnection.getServer().putTiles(playerId, tiles, column);
        } catch (RemoteException e) {
            throw new ClientCommunicationException();
        }
    }

    @Override
    public BooleanResponse sendMessage(String playerId, String player, String message) {
        try {
            return this.rmiClientConnection.getServer().sendMessage(playerId, player, message);
        } catch (RemoteException e) {
            throw new ClientCommunicationException();
        }
    }

    @Override
    public void pong(String playerId) {
        try {
            this.rmiClientConnection.getServer().pong(playerId);
        } catch (RemoteException e) {
            throw new ClientCommunicationException();
        }
    }
}
