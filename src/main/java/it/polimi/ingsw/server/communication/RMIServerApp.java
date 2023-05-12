package it.polimi.ingsw.server.communication;

import it.polimi.ingsw.communication.responses.*;
import it.polimi.ingsw.communication.rmi.RMIClient;
import it.polimi.ingsw.communication.rmi.RMIServer;
import it.polimi.ingsw.server.controller.*;
import it.polimi.ingsw.server.model.PlayerNotFoundException;
import it.polimi.ingsw.server.model.Tile;
import it.polimi.ingsw.server.model.TileType;

import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

public class RMIServerApp implements RMIServer {
    private final List<RMIClient> clients;
    private final Map<RMIClient, String> playersIds;

    private final Lobby lobby;

    private ControllerProvider controllerProvider = null;
    private PlayController playController = null;
    private ChatController chatController = null;
    private final RMIServerCommunication pushNotificationHandler;

    private final int port;
    public RMIServerApp(int port, Lobby lobby, RMIServerCommunication rmiServerCommunication) throws RemoteException {
        super();
        this.port = port;
        this.lobby = lobby;
        this.playController = null;
        this.clients = Collections.synchronizedList(new ArrayList<>());
        this.playersIds = Collections.synchronizedMap(new HashMap<>());
        this.pushNotificationHandler = rmiServerCommunication;
    }

    public void start() throws RemoteException {
        Registry registry = LocateRegistry.createRegistry(this.port);
        try {
            registry.bind("ServerRMIApp", this);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Server RMI ready");
    }

    /**
     * Method for add a playing client
     * @param client client socket
     */
    private synchronized void addPlayingClient(RMIClient client, String id){
        playersIds.put(client, id);
        pushNotificationHandler.addPlayingClient(client);
    }

    /**
     * Method for close a client
     * @param client client
     */
    private void closeClient(RMIClient client){
        this.playersIds.remove(client);
        this.pushNotificationHandler.removeClient(client);
        try {
            UnicastRemoteObject.unexportObject(client, true);
        }catch (IOException b){
            b.printStackTrace();
        }
    }

    /**
     * Check if we are in the playing phase of the game
     * @return true if we are
     */
    private synchronized boolean isGameActive(){
        boolean playControllerActive;
        playControllerActive  = (controllerProvider != null);
        return playControllerActive;
    }

    /**
     * Tries to start game if not started
     */
    private synchronized void tryStartGame(){
        try {
            controllerProvider = lobby.startGame();
            playController = controllerProvider.getPlayController();
            chatController = controllerProvider.getChatController();
            System.out.println("Player joined, game started!");
            pushNotificationHandler.gameSetUp();
        } catch (EmptyLobbyException e) {
            System.out.println("Player joined, but lobby not full!");
        }
    }

    /**
     * Start the game from the extern
     */
    public synchronized void startGame(ControllerProvider controllerProvider){
        this.controllerProvider = controllerProvider;
        this.playController = this.controllerProvider.getPlayController();
        this.chatController = this.controllerProvider.getChatController();
    }

    @Override
    public Hello hello() throws RemoteException {
        Hello hello;
        if (!isGameActive()) {
            try {
                Optional<String> idfp = lobby.join();
                if (idfp.isEmpty()) {
                    hello = new Hello(lobby.getIsCreating(), lobby.isGameLoaded());
                } else {
                    hello = new Hello(idfp.get());
                }

            } catch (WaitLobbyException e) {
                hello = new Hello(false, false);
            }

        } else {
            hello = new Hello(true, false);
        }
        return hello;
    }

    @Override
    public FirstJoinResponse joinNewAsFirst(RMIClient client, String player, int numPlayersGame, String idFirstPlayer) throws RemoteException {
         return joinNewAsFirst(client, player,  numPlayersGame,  idFirstPlayer, false);
    }


    @Override
    public FirstJoinResponse joinNewAsFirst(RMIClient client, String player, int numPlayersGame, String idFirstPlayer, boolean easyRules) throws RemoteException {
        BooleanResponse br;
        if(client != null) {
            boolean res = lobby.joinFirstPlayer(player, numPlayersGame, easyRules, idFirstPlayer);
            if (res) {
                addPlayingClient(client, idFirstPlayer);
            }
            br = new BooleanResponse(res);
        }
        else{
            br = new BooleanResponse(false);
        }
        return new FirstJoinResponse(true);
        //return br;
    }


    @Override
    public JoinResponse join(RMIClient client, String player) throws RemoteException {
        JoinResponse joinResponse = null;
        if(client != null){
            try {
                joinResponse = new JoinResponse(lobby.addPlayer(player));
            } catch (NicknameTakenException e) {
                joinResponse = new JoinResponse(e);
            } catch (NicknameException e) {
                joinResponse = new JoinResponse(e);
            } catch (FullGameException e) {
                joinResponse = new JoinResponse(e);
            } catch (FirstPlayerAbsentException e) {
                throw new RuntimeException(e);
            }
            if(joinResponse.result){
                addPlayingClient(client, joinResponse.id);
            }
            tryStartGame();
        }
        return joinResponse;
    }


    @Override
    public SavedGames getSavedGames() throws RemoteException {
        return new SavedGames(lobby.getSavedGames());
    }


    @Override
    public LoadGameResponse loadGame(String game, String idFirstPlayer) throws RemoteException {
        LoadGameResponse loadGameResponse;
        try {
            lobby.loadGame(game, idFirstPlayer);
            loadGameResponse = new LoadGameResponse();
        } catch (GameLoadException e) {
            loadGameResponse = new LoadGameResponse(e);
        } catch (GameNameException e) {
            loadGameResponse = new LoadGameResponse(e);
        } catch (IllegalLobbyException e) {
            loadGameResponse = new LoadGameResponse(e);
        }
        return  loadGameResponse;
    }


    @Override
    public LoadedGamePlayers getLoadedGamePlayers() throws RemoteException {
        LoadedGamePlayers lgp;
        if (!isGameActive()) {
            Set<String> pns = new HashSet<>(lobby.getLoadedPlayersNames());
            lgp =  new LoadedGamePlayers(pns);
        } else {
            lgp = new LoadedGamePlayers(new HashSet<>());
        }
        return lgp;
    }


    @Override
    public FirstJoinResponse joinLoadedAsFirst(RMIClient client, String player, String idFirstPlayer) throws RemoteException {
        BooleanResponse br;
        if(client != null) {
            try {
                boolean res = lobby.joinLoadedGameFirstPlayer(player, idFirstPlayer);
                br = new BooleanResponse(res);
                if (res) {
                    addPlayingClient(client, idFirstPlayer);
                }
            } catch (NicknameException e) {
                br = new BooleanResponse(false);
            }
        }
        else {
            br = new BooleanResponse(false);
        }
       // return br;
        return new FirstJoinResponse(true);
    }


    @Override
    public void disconnect(String playerId) throws RemoteException {
        BooleanResponse br;
        RMIClient client = playersIds.entrySet()
                .stream()
                .filter((e)->{return e.getValue().equals(playerId);})
                .map(Map.Entry::getKey)
                .findFirst().orElseGet(null);
        if(client == null){
           br = new BooleanResponse(false);
        }
        else {
            if (isGameActive()) {
                boolean res = playController.leave(playersIds.get(client));
                if (res) {
                    // TODO: to finish
                    //notifyDisconnection(playersIds.get(client));
                }
                br = new BooleanResponse(res);
            } else {
                br = new BooleanResponse(true);
            }
            closeClient(client);
        }
        //return br;
    }


    @Override
    public void reconnect(RMIClient client, String playerId) throws RemoteException {
        String id = playerId;
        BooleanResponse br;
        if(client != null) {
            if (isGameActive()) {
                String name = lobby.getNameFromId(id);
                if (name != null) {
                    boolean res = playController.reconnect(name);
                    br = new BooleanResponse(res);
                    if (res) {
                        addPlayingClient(client, id);
                    }
                } else
                    br = new BooleanResponse(false);
            } else {
                br = new BooleanResponse(false);
            }
        }
        else {
            br = new BooleanResponse(false);
        }
        //return br;
    }


    @Override
    public void pickTiles(String playerId, Set<Tile> tiles) throws RemoteException {
        BooleanResponse br;
        String namep = lobby.getNameFromId(playerId);
        if (isGameActive() && namep != null) {
            br = new BooleanResponse(playController.pickTiles(new ArrayList<>(tiles), namep));
        } else {
            br = new BooleanResponse(false);
        }
        //return br;
    }


    @Override
    public void putTiles(String playerId, List<TileType> tiles, int column) throws RemoteException {
        BooleanResponse br;
        String namep = lobby.getNameFromId(playerId);
        if (isGameActive() && namep != null) {
            List<Tile> tilesPut = tiles.stream().map(Tile::new).toList();
            br = new BooleanResponse(playController.putTiles(tilesPut, column, namep));
        } else {
            br = new BooleanResponse(false);
        }
        //return br;
    }

    @Override
    public void saveGame(String playerId, String gameName) throws RemoteException {
        if (isGameActive() && lobby.getNameFromId(playerId) != null) {
            boolean res;
            try {
                res = playController.saveGame(gameName);
            } catch (Exception e) {
                e.printStackTrace();
                res = false;
            }
            if (res) {
                // TODO: notify all players
            }
        }
    }


    @Override
    public void sendMessage(String playerId, String player, String message) throws RemoteException {
        String sender = lobby.getNameFromId(playerId);
        if (isGameActive() && sender != null) {
            if (player != null) {
                try {
                    chatController.sendMessage(sender, player, message);
                } catch (PlayerNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void sendMessage(String playerId, String message) throws RemoteException {
        // TODO
    }


    @Override
    public void pong(String playerId) throws RemoteException {
        //TODO: finish
    }
}
