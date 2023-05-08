package it.polimi.ingsw.client.communication;

import it.polimi.ingsw.communication.commands.*;
import it.polimi.ingsw.communication.responses.*;
import it.polimi.ingsw.server.model.Tile;
import it.polimi.ingsw.server.model.TileType;

import java.math.BigInteger;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * this class is a TCP based implementation of ClientCommunication
 * it uses a TCPClientClientConnection to send messages to the server
 * note that all of its methods block the main thread, therefore
 * it is advised to call them asynchronously or use multiple instances of ClientCommunication
 * NB: this class is thread safe because of the id system
 */
//TODO: MESSAGES MUST BE ASSIGNED AN UNIQUE ID FROM THE ClientCommunication AND THE SERVER NEEDS TO SEND BACK THAT ID
public class TCPClientCommunication implements ClientCommunication {
    private BigInteger idCounter;
    private TCPClientClientConnection tcpClientConnection;
    /**
     * @param tcpClientConnection the implementation of the connection
     */
    public TCPClientCommunication(TCPClientClientConnection tcpClientConnection) {
        idCounter = BigInteger.ZERO;
        this.tcpClientConnection = tcpClientConnection;
    }
    /**
     * this method sends a Hello to the server and waits for its response (blocking its thread)
     */
    @Override
    public Hello hello() {
        BigInteger id = generateId(); //TODO: needs to be passed to the constructor when constructor is updated
        tcpClientConnection.sendToServer((new HelloCommand()).toJson());
        return waitHello(id);
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
        BigInteger id = generateId(); //TODO: needs to be passed to the constructor when constructor is updated
        tcpClientConnection.sendToServer((new JoinNewAsFirst(player, numPlayersGame, idFirstPlayer, easyRules)).toJson());
        return waitBooleanResponse(id);
    }
    /**
     * this method sends a GetSavedGames to the server and waits for its response (blocking its thread)
     */
    @Override
    public SavedGames getSavedGames() {
        BigInteger id = generateId(); //TODO: needs to be passed to the constructor when constructor is updated
        tcpClientConnection.sendToServer((new GetSavedGames()).toJson());
        return waitSavedGames(id);
    }
    /**
     * this method sends a LoadGame to the server and waits for its response (blocking its thread)
     */
    @Override
    public LoadGameResponse loadGame(String game, String idFirstPlayer) {
        BigInteger id = generateId(); //TODO: needs to be passed to the constructor when constructor is updated
        tcpClientConnection.sendToServer((new LoadGame(idFirstPlayer, game)).toJson());
        return waitLoadGameResponse(id);
    }
    /**
     * this method sends a GetLoadedPlayers to the server and waits for its response (blocking its thread)
     */
    @Override
    public LoadedGamePlayers getLoadedGamePlayers() {
        BigInteger id = generateId(); //TODO: needs to be passed to the constructor when constructor is updated
        tcpClientConnection.sendToServer((new GetLoadedPlayers()).toJson());
        return waitLoadedGamePlayers(id);
    }
    /**
     * this method sends a JoinLoadedAsFirst to the server and waits for its response (blocking its thread)
     */
    @Override
    public BooleanResponse joinLoadedAsFirst(String player, String idFirstPlayer) {
        BigInteger id = generateId(); //TODO: needs to be passed to the constructor when constructor is updated
        tcpClientConnection.sendToServer((new JoinLoadedAsFirst(player, idFirstPlayer)).toJson());
        return waitBooleanResponse(id);
    }
    /**
     * this method sends a Join to the server and waits for its response (blocking its thread)
     */
    @Override
    public JoinResponse join(String player) {
        BigInteger id = generateId(); //TODO: needs to be passed to the constructor when constructor is updated
        tcpClientConnection.sendToServer((new Join(player)).toJson());
        return waitJoinResponse(id);
    }
    /**
     * this method sends a Disconnect to the server and waits for its response (thread blocking)
     */
    @Override
    public BooleanResponse disconnect(String playerId) {
        BigInteger id = generateId(); //TODO: needs to be passed to the constructor when constructor is updated
        tcpClientConnection.sendToServer((new Disconnect(playerId)).toJson());
        return waitBooleanResponse(id);
    }
    /**
     * this method sends a Reconnect to the server and waits for its response (thread blocking)
     */
    @Override
    public BooleanResponse reconnect(String playerId) {
        BigInteger id = generateId(); //TODO: needs to be passed to the constructor when constructor is updated
        tcpClientConnection.sendToServer((new Reconnect(playerId)).toJson());
        return waitBooleanResponse(id);
    }
    /**
     * this method sends a PickTilesCommand to the server and waits for its response (thread blocking)
     */
    @Override
    public BooleanResponse pickTiles(String playerId, Set<Tile> tiles) {
        BigInteger id = generateId(); //TODO: needs to be passed to the constructor when constructor is updated
        tcpClientConnection.sendToServer((new PickTilesCommand(playerId, tiles)).toJson());
        return waitBooleanResponse(id);
    }
    /**
     * this method sends a PutTilesCommand to the server and waits for its response (thread blocking)
     */
    @Override
    public BooleanResponse putTiles(String playerId, List<TileType> tiles, int column) {
        BigInteger id = generateId(); //TODO: needs to be passed to the constructor when constructor is updated
        tcpClientConnection.sendToServer((new PutTilesCommand(playerId, tiles, column)).toJson());
        return waitBooleanResponse(id);
    }
    /**
     * this method sends a SendMessage to the server and waits for its response (thread blocking)
     */
    @Override
    public BooleanResponse sendMessage(String playerId, String player, String message) {
        BigInteger id = generateId(); //TODO: needs to be passed to the constructor when constructor is updated
        tcpClientConnection.sendToServer((new SendMessage(playerId, player, message)).toJson());
        return waitBooleanResponse(id);
    }
    /**
     * this method sends a Pong to the server, doesn't wait
     */
    @Override
    public void pong(String playerId) {
        tcpClientConnection.sendToServer((new Pong(playerId)).toJson());
    }
    /**
     * @param id the id that the response should have
     * this method waits for a BooleanResponse that has the correct id
     */
    private BooleanResponse waitBooleanResponse(BigInteger id) {
        Predicate<String> isCorrect =
                (response) -> {
                    Boolean correctType = BooleanResponse.fromJson(response).isPresent();
                    //Boolean correctId = BooleanResponse.fromJson(response).getId = id;
                    return correctType; //TODO: && correctId
                };
        Function<String, BooleanResponse> toInstance = (response) -> BooleanResponse.fromJson(response).get();
        try {
            return toInstance.apply(tcpClientConnection.receiveFromServer(isCorrect).get());
        } catch (InterruptedException e) {
            throw new ClientCommunicationException();
        } catch (ExecutionException e) {
            throw new ClientCommunicationException();
        }
    }
    private Hello waitHello(BigInteger id) {
        Predicate<String> isCorrect =
                (response) -> {
                    Boolean correctType = Hello.fromJson(response).isPresent();
                    //Boolean correctId = Hello.fromJson(response).getId = id;
                    return correctType; //TODO: && correctId
                };
        Function<String, Hello> toInstance = (response) -> Hello.fromJson(response).get();
        try {
            return toInstance.apply(tcpClientConnection.receiveFromServer(isCorrect).get());
        } catch (InterruptedException e) {
            throw new ClientCommunicationException();
        } catch (ExecutionException e) {
            throw new ClientCommunicationException();
        }
    }
    private SavedGames waitSavedGames(BigInteger id) {
        Predicate<String> isCorrect =
                (response) -> {
                    Boolean correctType = SavedGames.fromJson(response).isPresent();
                    //Boolean correctId = SavedGames.fromJson(response).getId = id;
                    return correctType; //TODO: && correctId
                };
        Function<String, SavedGames> toInstance = (response) -> SavedGames.fromJson(response).get();
        try {
            return toInstance.apply(tcpClientConnection.receiveFromServer(isCorrect).get());
        } catch (InterruptedException e) {
            throw new ClientCommunicationException();
        } catch (ExecutionException e) {
            throw new ClientCommunicationException();
        }
    }
    private LoadGameResponse waitLoadGameResponse(BigInteger id) {
        Predicate<String> isCorrect =
                (response) -> {
                    Boolean correctType = LoadGameResponse.fromJson(response).isPresent();
                    //Boolean correctId = LoadGameResponse.fromJson(response).getId = id;
                    return correctType; //TODO: && correctId
                };
        Function<String, LoadGameResponse> toInstance = (response) -> LoadGameResponse.fromJson(response).get();
        try {
            return toInstance.apply(tcpClientConnection.receiveFromServer(isCorrect).get());
        } catch (InterruptedException e) {
            throw new ClientCommunicationException();
        } catch (ExecutionException e) {
            throw new ClientCommunicationException();
        }
    }
    private LoadedGamePlayers waitLoadedGamePlayers(BigInteger id) {
        Predicate<String> isCorrect =
                (response) -> {
                    Boolean correctType = LoadedGamePlayers.fromJson(response).isPresent();
                    //Boolean correctId = LoadedGamePlayers.fromJson(response).getId = id;
                    return correctType; //TODO: && correctId
                };
        Function<String, LoadedGamePlayers> toInstance = (response) -> LoadedGamePlayers.fromJson(response).get();
        try {
            return toInstance.apply(tcpClientConnection.receiveFromServer(isCorrect).get());
        } catch (InterruptedException e) {
            throw new ClientCommunicationException();
        } catch (ExecutionException e) {
            throw new ClientCommunicationException();
        }
    }
    private JoinResponse waitJoinResponse(BigInteger id) {
        Predicate<String> isCorrect =
                (response) -> {
                    Boolean correctType = JoinResponse.fromJson(response).isPresent();
                    //Boolean correctId = JoinResponse.fromJson(response).getId = id;
                    return correctType; //TODO: && correctId
                };
        Function<String, JoinResponse> toInstance = (response) -> JoinResponse.fromJson(response).get();
        try {
            return toInstance.apply(tcpClientConnection.receiveFromServer(isCorrect).get());
        } catch (InterruptedException e) {
            throw new ClientCommunicationException();
        } catch (ExecutionException e) {
            throw new ClientCommunicationException();
        }
    }
    private synchronized BigInteger generateId() {
        idCounter.add(BigInteger.ONE);
        return idCounter;
    }
}
