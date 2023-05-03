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
    public SavedGames getSavedGames() {
        try {
            return this.rmiClientConnection.getServer().getSavedGames();
        } catch (RemoteException e) {
            throw new ClientCommunicationException();
        }
    }

    @Override
    public BooleanResponse joinFirstPlayer(String name, int numPlayersGame, String id) {
        try {
            return this.rmiClientConnection.getServer().joinFirstPlayer(name, numPlayersGame, id);
        } catch (RemoteException e) {
            throw new ClientCommunicationException();
        }
    }

    @Override
    public BooleanResponse joinFirstPlayer(String name, int numPlayersGame, String id, boolean easyRules) {
        try {
            return this.rmiClientConnection.getServer().joinFirstPlayer(name, numPlayersGame, id, easyRules);
        } catch (RemoteException e) {
            throw new ClientCommunicationException();
        }
    }

    @Override
    public LoadGameResponse loadGame(String name, String idFirstPlayer) {
        try {
            return this.rmiClientConnection.getServer().loadGame(name, idFirstPlayer);
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
    public BooleanResponse joinLoadedGameFirstPlayer(String name, String id) {
        try {
            return this.rmiClientConnection.getServer().joinLoadedGameFirstPlayer(name, id);
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
    public BooleanResponse isFistPlayerPresent() {
        try {
            return this.rmiClientConnection.getServer().isFistPlayerPresent();
        } catch (RemoteException e) {
            throw new ClientCommunicationException();
        }
    }

    @Override
    public BooleanResponse pickTiles(Set<Tile> tiles) {
        try {
            return this.rmiClientConnection.getServer().pickTiles(tiles);
        } catch (RemoteException e) {
            throw new ClientCommunicationException();
        }
    }

    @Override
    public BooleanResponse putTiles(List<TileType> tiles, int column) {
        try {
            return this.rmiClientConnection.getServer().putTiles(tiles, column);
        } catch (RemoteException e) {
            throw new ClientCommunicationException();
        }
    }

    @Override
    public void pong() {
        try {
            this.rmiClientConnection.getServer().pong();
        } catch (RemoteException e) {
            throw new ClientCommunicationException();
        }
    }
}
