package it.polimi.ingsw.server.communication;

import com.google.gson.internal.bind.TreeTypeAdapter;
import it.polimi.ingsw.communication.commands.*;
import it.polimi.ingsw.communication.responses.*;
import it.polimi.ingsw.server.controller.*;
import it.polimi.ingsw.server.model.ArrestGameException;
import it.polimi.ingsw.server.model.Player;
import it.polimi.ingsw.server.model.PlayerNotFoundException;
import it.polimi.ingsw.server.model.Tile;

import java.util.*;


/**
 * Class that implements methods to answer a client request
 */
// TODO: make TCPServer and RMIServerApp to extends this
public abstract class ResponseServer{

    protected final Lobby lobby;
    protected ControllerProvider controllerProvider;
    protected PlayController playController;
    protected ChatController chatController;

    protected final String playLock;

    public ResponseServer(Lobby lobby, String sharedLock){
        this.lobby = lobby;
        this.playLock = sharedLock;
        controllerProvider = null;
        playController = null;
        chatController = null;
    }

    /**
     * Respond to a hello command
     * @param hc command
     * @return hello msg
     */
    protected Hello respondHello(HelloCommand hc){
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
    protected JoinResponse respondJoin(Join j, Object client){
        JoinResponse joinResponse;
        System.out.println("\u001B[38;5;202m respond join called \u001B[0m");
        try {
            joinResponse = new JoinResponse(lobby.addPlayer(j.player));
        } catch (NicknameTakenException e) {
            joinResponse = new JoinResponse(e);
        } catch (NicknameException e) {
            joinResponse = new JoinResponse(e);
        } catch (FullGameException e) {
            joinResponse = new JoinResponse(e);
        }
        if(joinResponse.result){
            System.out.println("\u001B[38;5;202m respond join: adding client \u001B[0m");
            addPlayingClient(client, joinResponse.id);
        }

        ResponseServer rs = this;
        new Thread(() -> {
            System.out.println("\u001B[38;5;202m started a new thread with tryStartGame() \u001B[0m");
            rs.tryStartGame();
            return;
        }).start();

        System.out.println("\u001B[38;5;202m respond join after try to start game here \u001B[0m");
        return joinResponse;
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
        if (isGameActive()) {
            String playerName = getPlayerNameFromClient(client);
            if(!playController.leave(playerName)){
                System.err.println("Problems with disconnection of "+playerName);
            }
        }
        closeClient(client);
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
                if(playController.reconnect(name)){
                    addPlayingClient(client, id);
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
                //TODO send error message
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
                //TODO send error message
            }
        }
    }

    /**
     * Respond to a save game command and save the game if was sent from a playing player
     * @param sg command
     */
    protected void respondSaveGame(SaveGame sg){
        if (isGameActive() && lobby.getNameFromId(sg.getId()) != null) {
            boolean res;
            try {
                res = playController.saveGame(sg.game);
            } catch (Exception e) {
                e.printStackTrace();
                res = false;
            }
            if (res) {
                // TODO: notify all players
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
                    e.printStackTrace();
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
                        System.out.println("Player joined, game started!");
                    } catch (EmptyLobbyException e) {
                        System.out.println("Player joined, but lobby not full!");
                        return;
                    } catch (ArrestGameException e) {
                        System.err.println("Game arrested unexpected!");
                        e.printStackTrace();
                        //TODO: notify all clients with an errorMessage
                        return;
                    }
                } else {
                    controllerProvider = lobby.getControllerProvider(); //TODO null here
                    System.out.println("Game started from another protocol!");
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


    //TODO check this two
    /*

     * Send a message to all the clients
     * @param message Msg to send
     /
    protected abstract void sendMessageToAll(Msg message);
    protected abstract void sendMessageToClient(Object client, Msg message);
    */
}
