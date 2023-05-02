package it.polimi.ingsw.client;

import it.polimi.ingsw.communication.responses.*;
import it.polimi.ingsw.server.communication.ClientCommunication;
import it.polimi.ingsw.server.model.Tile;
import it.polimi.ingsw.server.model.TileType;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ClientCommunicationImplementation implements ClientCommunication {

    @Override
    public Hello hello() {
        return new Hello(Boolean.TRUE, Boolean.TRUE);
    }

    @Override
    public SavedGames getSavedGames() {
        return new SavedGames(new HashSet<>());
    }

    @Override
    public BooleanResponse joinFirstPlayer(String name, int numPlayersGame, String id) {
        return new BooleanResponse(Boolean.TRUE);
    }

    @Override
    public BooleanResponse joinFirstPlayer(String name, int numPlayersGame, String id, boolean easyRules) {
        return new BooleanResponse(Boolean.TRUE);
    }

    @Override
    public LoadGameResponse loadGame(String name, String idFirstPlayer) {
        return new LoadGameResponse();
    }

    @Override
    public LoadedGamePlayers getLoadedGamePlayers() {
        return new LoadedGamePlayers(new HashSet<>());
    }

    @Override
    public BooleanResponse joinLoadedGameFirstPlayer(String name, String id) {
        return new BooleanResponse(Boolean.TRUE) ;
    }

    @Override
    public JoinResponse join(String player) {
        return new JoinResponse("fancy id");
    }

    @Override
    public BooleanResponse disconnect(String playerId) {
        return new BooleanResponse(Boolean.TRUE);
    }

    @Override
    public BooleanResponse reconnect(String playerId) {
        return new BooleanResponse(Boolean.TRUE);
    }

    @Override
    public BooleanResponse isFistPlayerPresent() {
        return new BooleanResponse(Boolean.TRUE);
    }

    @Override
    public BooleanResponse pickTiles(Set<Tile> tiles) {
        return new BooleanResponse(Boolean.TRUE);
    }

    @Override
    public BooleanResponse putTiles(List<TileType> tiles, int column) {
        return new BooleanResponse(Boolean.TRUE);
    }

    @Override
    public void pong() {

    }
}
