package it.polimi.ingsw.client;

import it.polimi.ingsw.communication.responses.*;
import it.polimi.ingsw.client.communication.ClientCommunication;
import it.polimi.ingsw.server.model.Tile;
import it.polimi.ingsw.server.model.TileType;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ClientCommunicationImplementation implements ClientCommunication {

    @Override
    public void hello() {

    }

    @Override
    public void joinNewAsFirst(String player, int numPlayersGame, String idFirstPlayer) {

    }

    @Override
    public void joinNewAsFirst(String player, int numPlayersGame, String idFirstPlayer, boolean easyRules) {

    }

    @Override
    public void join(String player) {

    }

    @Override
    public void getSavedGames() {

    }

    @Override
    public void loadGame(String game, String idFirstPlayer) {

    }

    @Override
    public void getLoadedGamePlayers() {

    }

    @Override
    public void joinLoadedAsFirst(String player, String idFirstPlayer) {

    }

    @Override
    public void disconnect(String playerId) {

    }

    @Override
    public void reconnect(String playerId) {

    }

    @Override
    public void pickTiles(String playerId, Set<Tile> tiles) {

    }

    @Override
    public void putTiles(String playerId, List<TileType> tiles, int column) {

    }

    @Override
    public void sendMessage(String playerId, String player, String message) {

    }

    @Override
    public void pong(String playerId) {

    }
}
