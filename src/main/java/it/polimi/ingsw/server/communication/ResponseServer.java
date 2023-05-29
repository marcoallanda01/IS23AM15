package it.polimi.ingsw.server.communication;

import it.polimi.ingsw.communication.commands.*;
import it.polimi.ingsw.communication.responses.*;
import it.polimi.ingsw.server.controller.*;
import it.polimi.ingsw.server.controller.exceptions.*;
import it.polimi.ingsw.server.model.exceptions.ArrestGameException;
import it.polimi.ingsw.server.model.exceptions.PlayerNotFoundException;
import it.polimi.ingsw.server.model.Tile;

import java.util.*;


/**
 * Class that implements methods to answer a client request
 */
public abstract class ResponseServer{

    protected final Lobby lobby;
    protected ControllerProvider controllerProvider;
    protected PlayController playController;
    protected ChatController chatController;

    protected final String playLock;
    private final Timer pingPongService;
    protected final String ANONYMOUS_PING_ID = "NoId";
    /**
     * Map to check if the last ping pong was answered. Map client to boolean.
     */
    private final Map<Object, Boolean> pingPongMap;
    private final Map<Object, TimerTask> pingPongTasks;

    /**
     * Create a RespondServer. Its use is to implement methods to answer a client request.
     * @param lobby lobby shared between RespondServers
     * @param sharedLock shared lock on lobby, controllerProvider, playController, chatController between RespondServers
     */
    public ResponseServer(Lobby lobby, String sharedLock){
        this.lobby = lobby;
        this.playLock = sharedLock;
        this.controllerProvider = null;
        this.playController = null;
        this.chatController = null;
        this.pingPongService = new Timer();
        this.pingPongMap = Collections.synchronizedMap(new HashMap<>());
        this.pingPongTasks  = Collections.synchronizedMap(new HashMap<>());
    }

    /**
     * Respond to a hello command
     * @param hc command
     * @return hello msg
     */
    protected Hello respondHello(HelloCommand hc, Object client){
        Hello hello;
        if (!isGameActive()) {
            try {
                Optional<String> idfp = lobby.join();
                if (idfp.isEmpty()) {
                    hello = new Hello(!lobby.getIsCreating(), lobby.isGameLoaded());
                } else {
                    hello = new Hello(idfp.get());
                    startPingPong(client, idfp.get());
                }
            } catch (WaitLobbyException e) {
                hello = new Hello(false, false);
            }

        } else {
            hello = new Hello(true, false);
        }
        return hello;
    }

    /**
     * Respond to a JoinNewAsFirst command
     * @param jnf command
     * @param client client object
     * @return FirstJoinResponse msg
     */
    protected FirstJoinResponse respondJoinNewAsFirst(JoinNewAsFirst jnf, Object client){
        boolean res = lobby.joinFirstPlayer(jnf.player, jnf.numOfPlayers, jnf.easyRules, jnf.idFirstPlayer);
        if(res){
            addPlayingClient(client, jnf.idFirstPlayer);
        }
        return new FirstJoinResponse(res);
    }

    /**
     * Respond to a JoinNewAsFirst command
     * @param j command
     * @param client client object
     * @return JoinResponse msg
     */
    protected JoinResponse respondJoin(Join j, Object client) throws FirstPlayerAbsentException {
        JoinResponse joinResponse;
        System.out.println("\u001B[38;5;202m respond join called \u001B[0m");
        String name = getPlayerNameFromClient(client);
        if(name == null) { // if client didn't join
            try {
                synchronized (playLock) {
                    joinResponse = new JoinResponse(lobby.addPlayer(j.player));
                }
            } catch (NicknameTakenException e) {
                joinResponse = new JoinResponse(e);
            } catch (NicknameException e) {
                joinResponse = new JoinResponse(e);
            } catch (FullGameException e) {
                joinResponse = new JoinResponse(e);
            }
            if (joinResponse.result) {
                System.out.println("\u001B[38;5;202m respond join: adding client \u001B[0m");
                addPlayingClient(client, joinResponse.id);
                startPingPong(client, joinResponse.id);
            }

            ResponseServer rs = this;
            //TODO: choose
            new Thread(() -> {
                System.out.println("\u001B[38;5;202m started a new thread with tryStartGame() \u001B[0m");
                rs.tryStartGame();
                return;
            });//.start();
            tryStartGame();
            System.out.println("\u001B[38;5;202m respond join after try to start game here \u001B[0m");
            return joinResponse;
        }
        String idPlayer = lobby.getIdFromName(name);
        sendErrorMessage(idPlayer, "You already joined under the name: "+name);
        return new JoinResponse(idPlayer);
    }

    /**
     * Respond to a GetSavedGames command
     * @param gsg command
     * @return SavedGames msg
     */
    protected SavedGames respondGetSavedGames(GetSavedGames gsg){
        return new SavedGames(lobby.getSavedGames());
    }

    /**
     * Respond to a GetSavedGames command
     * @param lg Load Game Command
     * @return LoadGameRespond
     */
    protected LoadGameResponse respondLoadGame(LoadGame lg){
        LoadGameResponse loadGameResponse;
        try {
            lobby.loadGame(lg.game, lg.idFirstPlayer);
            loadGameResponse = new LoadGameResponse();
        } catch (GameLoadException e) {
            loadGameResponse = new LoadGameResponse(e);
        } catch (GameNameException e) {
            loadGameResponse = new LoadGameResponse(e);
        } catch (IllegalLobbyException e) {
            loadGameResponse = new LoadGameResponse(e);
        }
        return loadGameResponse;
    }

    /**
     * Respond to a GetLoadedPlayers command
     * @param glp command
     * @return LoadedGamePlayers
     */
    protected LoadedGamePlayers respondGetLoadedPlayers(GetLoadedPlayers glp){
        LoadedGamePlayers loadedGamePlayers;
        if (!isGameActive()) {
            Set<String> pns = new HashSet<>(lobby.getLoadedPlayersNames());
            loadedGamePlayers = new LoadedGamePlayers(pns);
        } else {
           loadedGamePlayers = new LoadedGamePlayers(new HashSet<>());
        }
        return loadedGamePlayers;
    }

    /**
     * Respond to a JoinLoadedAsFirst command
     * @param jlf command
     * @param client client object
     * @return FirstJoinResponse
     */
    protected FirstJoinResponse respondJoinLoadedAsFirst(JoinLoadedAsFirst jlf, Object client){
        FirstJoinResponse fjr;
        try {
            boolean res = lobby.joinLoadedGameFirstPlayer(jlf.player, jlf.idFirstPlayer);
            fjr = new FirstJoinResponse(res);
            if(res){
                addPlayingClient(client, jlf.idFirstPlayer);
            }
        } catch (NicknameException e) {
            fjr = new FirstJoinResponse(false);
        }
        return fjr;
    }

    /**
     * "Respond" to a Disconnect command and close client connection
     * @param d command
     * @param client client object
     */
    protected void respondDisconnect(Disconnect d, Object client){
        disconnectPlayer(d.getId(), client);
    }

    /**
     * "Respond" to a Reconnect command and in case add the client to playing clients
     * @param r command
     * @param client client object
     */
    protected void respondReconnect(Reconnect r, Object client){
        String id = r.getId();
        if (isGameActive()) {
            String name = lobby.getNameFromId(id);
            if (name != null) {
                try {
                    addPlayingClient(client, id);
                    if (!playController.reconnect(name)) {
                        System.err.println("respondReconnect: something went wrong with reconnection of " + id
                                + ", probably client never disconnected for the server!");
                        //handleReconnection(client);
                        throw new Exception("Player already connected!");
                    }
                }
                catch (RuntimeException e){
                    System.out.println("Problem in model during respondReconnect "+e.getMessage());
                }
                catch (Exception e){
                    System.out.println("respondReconnect: "+e.getMessage());
                }
            }
        }
    }

    /**
     * Respond to a pick tiles command and send an error message if pick tiles failed
     * @param ptc command
     */
    protected void respondPickTiles(PickTilesCommand ptc){
        String namep = lobby.getNameFromId(ptc.getId());
        if (isGameActive() && namep != null) {
            if(! playController.pickTiles(new ArrayList<>(ptc.tiles), namep) ){
                sendErrorMessage(ptc.getId(), "This tiles can't be picked!");
            }
        }
    }

    /**
     * Respond to a put tiles command and send an error message if put tiles failed
     * @param ptc command
     */
    protected void respondPutTiles(PutTilesCommand ptc){
        String namep = lobby.getNameFromId(ptc.getId());
        if (isGameActive() && namep != null) {
            List<Tile> tilesPut = ptc.tiles.stream().map(Tile::new).toList();
            if(! playController.putTiles(tilesPut, ptc.column, namep) ){
                sendErrorMessage(ptc.getId(), "Can't put this tiles like this!");
            }
        }
    }

    /**
     * Respond to a save game command
     * @param sg command
     */
    protected void respondSaveGame(SaveGame sg){
        synchronized (playLock) {
            String namep = lobby.getNameFromId(sg.getId());
            if (isGameActive() && namep != null) {
                boolean res;
                try {
                    playController.saveGame(sg.game);
                } catch (Exception e) {
                    sendErrorMessage(sg.getId(), "This tiles can't be picked!");
                }
            }
        }
    }

    /**
     * Respond to a send message command
     * @param sm command
     */
    protected void respondSendMessage(SendMessage sm){
        String sender = lobby.getNameFromId(sm.getId());
        if (isGameActive() && sender != null) {
            if (sm.player != null) {
                try {
                    chatController.sendMessage(sender, sm.player, sm.message);
                } catch (PlayerNotFoundException e) {
                    System.out.println(sender+" tried to send a message to "+sm.player+", but either receiver or sender don't exists!");
                    sendErrorMessage(sm.getId(), "Can not send message! Try again!");
                }
            } else {
                try {
                    chatController.sendMessage(sender, sm.message);
                } catch (PlayerNotFoundException e) {
                    System.out.println(sender+" tried to send a message to all but the sender doesn't exists!");
                    sendErrorMessage(sm.getId(), "Can not send message! Try again!");
                }
            }
        }
    }

    /**
     * Close a client connection
     * @param client client object. A cast is needed
     */
    protected abstract void closeClient(Object client);

    /**
     * Get name of a playing client form his connection
     * @param client client object. A cast is needed
     * @return client's nickname. Return null if client is not in game
     */
    protected abstract String getPlayerNameFromClient(Object client);

    /**
     * Add a playing client
     * @param client object (need cast)
     * @param id player's id
     */
    protected abstract void addPlayingClient(Object client, String id);

    /**
     * Lock playLock and tries to start the game
     */
    protected void tryStartGame(){
        System.out.println("\u001B[38;5;202m tryStartGame called \u001B[0m");
        synchronized (playLock){
            System.out.println("\u001B[38;5;202m tryStartGame: playlock address:"+System.identityHashCode(playLock)+" \u001B[0m");
            System.out.println("\u001B[38;5;202m tryStartGame: playLock acquired  \u001B[0m");
            if(controllerProvider == null) {
                if (!lobby.isPlaying()) {
                    System.out.println("\u001B[38;5;202m tryStartGame: trying to start game  \u001B[0m");
                    try {
                        System.out.println("\u001B[38;5;202m tryStartGame: before startGame  \u001B[0m");
                        controllerProvider = lobby.startGame();
                        System.out.println("Player joined, game started!"+controllerProvider);
                    } catch (EmptyLobbyException e) {
                        System.out.println("Player joined, but lobby not full!");
                        return;
                    } catch (ArrestGameException e) {
                        System.err.println("Game arrested unexpected!");
                        sendErrorMessageToAll("SERVER ERROR: Game can not start! Please retry or try later!");
                        return;
                    }
                } else {
                    controllerProvider = lobby.getControllerProvider();
                    System.out.println("Game started from another protocol! " + controllerProvider);
                }
                playController = controllerProvider.getPlayController();
                chatController = controllerProvider.getChatController();
            }
            System.out.println("\u001B[38;5;202m tryStartGame: playLock releasing...  \u001B[0m");
        }
    }

    /**
     * check if game is active
     */
    protected boolean isGameActive(){
        return lobby.isPlaying();
    }

    /**
     * reset game and lobby.
     * Must call when game is ended
     */
    protected void reset(){
        synchronized (playLock){
            lobby.reset();
            controllerProvider = null;
            playController = null;
            chatController = null;
        }
    }

    /**
     * Handle pong message
     * @param pong pong object
     * @param client client object
     */
    protected void respondPong(Pong pong, Object client){
        if(this.pingPongMap.get(client) != null){
            this.pingPongMap.put(client, true);
            System.out.println("\u001B[94mPong received by "+pong.getId() +"\u001B[0m");
        }
    }

    /**
     * Starts ping pong between client and server
     * @param client client's object
     * @param id player's id
     */
    protected void startPingPong(Object client, String id){
        if(id == null){
            System.err.println("startPingPong id null from "+client+"!");
            return;
        }
        if(pingPongTasks.containsKey(client) && !ANONYMOUS_PING_ID.equals(id)){
            System.out.println("\u001B[94mAnonymous client "+client+" changed in "+id+"\u001B[0m");
            pingPongTasks.get(client).cancel();
            pingPongTasks.remove(client);
        }
        this.pingPongMap.put(client, true);
        TimerTask PingTask = new TimerTask() {
            public void run() {
                Boolean responded = pingPongMap.get(client);
                System.out.println("\u001B[94mping pong:"+pingPongMap+"\u001B[0m");
                if (responded == null) {
                    //If res is null means that client disconnected
                    this.cancel();
                } else if (responded.equals(false)) {
                    disconnectPlayer(id, client);
                    this.cancel();
                } else {
                    pingPongMap.put(client, false);
                    ping(client);
                }
            }
        };
        this.pingPongService.scheduleAtFixedRate(PingTask, 5000, 3000);
        this.pingPongTasks.put(client, PingTask);
    }

    /**
     * Disconnect a player and calls closeClient()
     * @param id player's id
     * @param client client object
     */
    private void disconnectPlayer(String id, Object client){
        pingPongMap.remove(client);
        synchronized (playLock){
            if (isGameActive()) {
                String playerName = getPlayerNameFromClient(client);
                if(!playController.leave(playerName)){
                    System.err.println("Problems with disconnection of "+playerName);
                }
            }else{
                //Game not started yet
                lobby.removePlayer(id);
            }
        }
        closeClient(client);
    }

    /**
     * Ping a client to keep connection alive
     * @param client object
     */
    protected abstract void ping(Object client);

    /**
     * Send an error message to a player
     * @param player player's id
     * @param message Error message
     */
    protected abstract void sendErrorMessage(String player, String message);

    /**
     * Send an error message to all players
     * @param message Error message
     */
    protected abstract void sendErrorMessageToAll(String message);

    /**
     * Handle reconnection of a client sending them all the necessary.
     * @param client client's object
     */
   // protected abstract void handleReconnection(Object client);

}
