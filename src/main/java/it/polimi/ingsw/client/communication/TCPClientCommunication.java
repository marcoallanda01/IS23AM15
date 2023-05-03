package it.polimi.ingsw.client.communication;

import it.polimi.ingsw.communication.commands.HelloCommand;
import it.polimi.ingsw.communication.responses.*;
import it.polimi.ingsw.server.communication.ClientCommunication;
import it.polimi.ingsw.server.model.Tile;
import it.polimi.ingsw.server.model.TileType;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.function.Function;
import java.util.function.Predicate;

public class TCPClientCommunication implements ClientCommunication {
    private TCPClientConnection tcpClientConnection;

    @Override
    public Hello hello() {
        tcpClientConnection.sendToServer((new HelloCommand()).toJson());
        Predicate<String> isCorrectType = (response) -> Hello.fromJson(response).isPresent();
        Function<String, Hello> parse = (response) -> Hello.fromJson(response).get();
        try {
            return parse.apply(tcpClientConnection.receiveFromServer(isCorrectType).get());
        } catch (InterruptedException e) {
            throw new ClientCommunicationException();
        } catch (ExecutionException e) {
            throw new ClientCommunicationException();
        }
    }

    @Override
    public SavedGames getSavedGames() {
        return null;
    }

    @Override
    public BooleanResponse joinFirstPlayer(String name, int numPlayersGame, String id) {
        return null;
    }

    @Override
    public BooleanResponse joinFirstPlayer(String name, int numPlayersGame, String id, boolean easyRules) {
        return null;
    }

    @Override
    public LoadGameResponse loadGame(String name, String idFirstPlayer) {
        return null;
    }

    @Override
    public LoadedGamePlayers getLoadedGamePlayers() {
        return null;
    }

    @Override
    public BooleanResponse joinLoadedGameFirstPlayer(String name, String id) {
        return null;
    }

    @Override
    public JoinResponse join(String player) {
        return null;
    }

    @Override
    public BooleanResponse disconnect(String playerId) {
        return null;
    }

    @Override
    public BooleanResponse reconnect(String playerId) {
        return null;
    }

    @Override
    public BooleanResponse isFistPlayerPresent() {
        return null;
    }

    @Override
    public BooleanResponse pickTiles(Set<Tile> tiles) {
        return null;
    }

    @Override
    public BooleanResponse putTiles(List<TileType> tiles, int column) {
        return null;
    }

    @Override
    public void pong() {

    }
}
