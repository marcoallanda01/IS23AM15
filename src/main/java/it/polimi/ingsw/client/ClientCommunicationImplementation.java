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
    public BooleanResponse joinNewAsFirst(String name, int numPlayersGame, String idFirstPlayer) {
        return null;
    }

    @Override
    public BooleanResponse joinNewAsFirst(String name, int numPlayersGame, String idFirstPlayer, boolean easyRules) {
        return null;
    }

    @Override
    public SavedGames getSavedGames() {
        return new SavedGames(new HashSet<>());
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
    public BooleanResponse joinLoadedAsFirst(String name, String idFirstPlayer) {
        return null;
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
    public BooleanResponse pickTiles(String playerId, Set<Tile> tiles) {
        return new BooleanResponse(Boolean.TRUE);
    }

    @Override
    public BooleanResponse putTiles(String playerId, List<TileType> tiles, int column) {
        return new BooleanResponse(Boolean.TRUE);
    }

    @Override
    public BooleanResponse sendMessage(String playerId, String player, String message) {
        return null;
    }

    @Override
    public void pong(String playerId) {

    }
}
