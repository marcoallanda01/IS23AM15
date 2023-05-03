package it.polimi.ingsw.client.communication;

import it.polimi.ingsw.communication.commands.*;
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

/**
 * this class is a TCP based implementation of ClientCommunication
 */
public class TCPClientCommunication implements ClientCommunication {
    private TCPClientConnection tcpClientConnection;
    /**
     * @param tcpClientConnection the implementation of the connection
     */
    public TCPClientCommunication(TCPClientConnection tcpClientConnection) {
        this.tcpClientConnection = tcpClientConnection;
    }
    /**
     * this method sends a Hello to the server and waits for its response (blocking its thread)
     */
    @Override
    public Hello hello() {
        tcpClientConnection.sendToServer((new HelloCommand()).toJson());
        Predicate<String> isCorrectType = (response) -> Hello.fromJson(response).isPresent();
        Function<String, Hello> toInstance = (response) -> Hello.fromJson(response).get();
        try {
            return toInstance.apply(tcpClientConnection.receiveFromServer(isCorrectType).get());
        } catch (InterruptedException e) {
            throw new ClientCommunicationException();
        } catch (ExecutionException e) {
            throw new ClientCommunicationException();
        }
    }
    /**
     * this method sends a JoinNewAsFirst to the server and waits for its response (blocking its thread)
     */
    @Override
    public BooleanResponse joinNewAsFirst(String player, int numPlayersGame, String idFirstPlayer) {
        return joinNewAsFirst(player, numPlayersGame, idFirstPlayer, false);
    }
    /**
     * this method sends a JoinNewAsFirst to the server and waits for its response (blocking its thread)
     */
    @Override
    public BooleanResponse joinNewAsFirst(String player, int numPlayersGame, String idFirstPlayer, boolean easyRules) {
        tcpClientConnection.sendToServer((new JoinNewAsFirst(player, numPlayersGame, idFirstPlayer, easyRules)).toJson());
        return getBooleanResponse();
    }

    /**
     * this method sends a GetSavedGames to the server and waits for its response (blocking its thread)
     */
    @Override
    public SavedGames getSavedGames() {
        tcpClientConnection.sendToServer((new GetSavedGames()).toJson());
        Predicate<String> isCorrectType = (response) -> SavedGames.fromJson(response).isPresent();
        Function<String, SavedGames> toInstance = (response) -> SavedGames.fromJson(response).get();
        try {
            return toInstance.apply(tcpClientConnection.receiveFromServer(isCorrectType).get());
        } catch (InterruptedException e) {
            throw new ClientCommunicationException();
        } catch (ExecutionException e) {
            throw new ClientCommunicationException();
        }
    }
    /**
     * this method sends a LoadGame to the server and waits for its response (blocking its thread)
     */
    @Override
    public LoadGameResponse loadGame(String game, String idFirstPlayer) {
        tcpClientConnection.sendToServer((new LoadGame(idFirstPlayer, game)).toJson());
        Predicate<String> isCorrectType = (response) -> LoadGameResponse.fromJson(response).isPresent();
        Function<String, LoadGameResponse> toInstance = (response) -> LoadGameResponse.fromJson(response).get();
        try {
            return toInstance.apply(tcpClientConnection.receiveFromServer(isCorrectType).get());
        } catch (InterruptedException e) {
            throw new ClientCommunicationException();
        } catch (ExecutionException e) {
            throw new ClientCommunicationException();
        }
    }
    /**
     * this method sends a GetLoadedPlayers to the server and waits for its response (blocking its thread)
     */
    @Override
    public LoadedGamePlayers getLoadedGamePlayers() {
        tcpClientConnection.sendToServer((new GetLoadedPlayers()).toJson());
        Predicate<String> isCorrectType = (response) -> LoadedGamePlayers.fromJson(response).isPresent();
        Function<String, LoadedGamePlayers> toInstance = (response) -> LoadedGamePlayers.fromJson(response).get();
        try {
            return toInstance.apply(tcpClientConnection.receiveFromServer(isCorrectType).get());
        } catch (InterruptedException e) {
            throw new ClientCommunicationException();
        } catch (ExecutionException e) {
            throw new ClientCommunicationException();
        }
    }
    /**
     * this method sends a JoinLoadedAsFirst to the server and waits for its response (blocking its thread)
     */
    @Override
    public BooleanResponse joinLoadedAsFirst(String player, String idFirstPlayer) {
        tcpClientConnection.sendToServer((new JoinLoadedAsFirst(player, idFirstPlayer)).toJson());
        return getBooleanResponse();
    }
    /**
     * this method sends a Join to the server and waits for its response (blocking its thread)
     */
    @Override
    public JoinResponse join(String player) {
        tcpClientConnection.sendToServer((new Join(player)).toJson());
        Predicate<String> isCorrectType = (response) -> JoinResponse.fromJson(response).isPresent();
        Function<String, JoinResponse> toInstance = (response) -> JoinResponse.fromJson(response).get();
        try {
            return toInstance.apply(tcpClientConnection.receiveFromServer(isCorrectType).get());
        } catch (InterruptedException e) {
            throw new ClientCommunicationException();
        } catch (ExecutionException e) {
            throw new ClientCommunicationException();
        }
    }
    /**
     * this method sends a Disconnect to the server and waits for its response (blocking its thread)
     */
    @Override
    public BooleanResponse disconnect(String playerId) {
        tcpClientConnection.sendToServer((new Disconnect(playerId)).toJson());
        return getBooleanResponse();
    }
    /**
     * this method sends a Reconnect to the server and waits for its response (blocking its thread)
     */
    @Override
    public BooleanResponse reconnect(String playerId) {
        tcpClientConnection.sendToServer((new Reconnect(playerId)).toJson());
        return getBooleanResponse();
    }

    /**
     * this method sends a PickTilesCommand to the server and waits for its response (blocking its thread)
     */
    @Override
    public BooleanResponse pickTiles(String playerId, Set<Tile> tiles) {
        tcpClientConnection.sendToServer((new PickTilesCommand(playerId, tiles)).toJson());
        return getBooleanResponse();
    }
    /**
     * this method sends a PutTilesCommand to the server and waits for its response (blocking its thread)
     */
    @Override
    public BooleanResponse putTiles(String playerId, List<TileType> tiles, int column) {
        tcpClientConnection.sendToServer((new PutTilesCommand(playerId, tiles, column)).toJson());
        return getBooleanResponse();
    }

    @Override
    public BooleanResponse sendMessage(String playerId, String player, String message) {
        tcpClientConnection.sendToServer((new SendMessage(playerId, player, message)).toJson());
        return getBooleanResponse();
    }

    private BooleanResponse getBooleanResponse() {
        Predicate<String> isCorrectType = (response) -> BooleanResponse.fromJson(response).isPresent();
        Function<String, BooleanResponse> toInstance = (response) -> BooleanResponse.fromJson(response).get();
        try {
            return toInstance.apply(tcpClientConnection.receiveFromServer(isCorrectType).get());
        } catch (InterruptedException e) {
            throw new ClientCommunicationException();
        } catch (ExecutionException e) {
            throw new ClientCommunicationException();
        }
    }

    /**
     * this method sends a Pong to the server
     */
    @Override
    public void pong(String playerId) {
        tcpClientConnection.sendToServer((new Pong(playerId)).toJson());
    }
}
