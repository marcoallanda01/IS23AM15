package it.polimi.ingsw.server.communication;


import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.server.controller.*;
import it.polimi.ingsw.server.model.Game;

import java.util.Objects;
import java.util.concurrent.locks.Lock;

/**
 * Class that implements methods to answer a client request
 */
// TODO: make TCPServer and RMIServerApp to extends this
public abstract class ResponseServer {

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
     * Add a playing client
     * @param client object (need cast)
     */
    protected abstract void addPlayingClient(Object client);

    /**
     * Lock playLock and tries to start the game
     */
    protected abstract void tryStartGame();

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
}
/*
    switch (commandName) {
        case ("HelloCommand") -> {
            Optional<HelloCommand> hc = HelloCommand.fromJson(json);
            if (hc.isPresent()) {
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
                out.println(hello.toJson());
                return true;
            } else {
                wrongFormatted = true;
            }
        }
        case ("JoinNewAsFirst") -> {
            Optional<JoinNewAsFirst> ojnf = JoinNewAsFirst.fromJson(json);
            if (ojnf.isPresent()) {
                JoinNewAsFirst jnf = ojnf.get();
                boolean res = lobby.joinFirstPlayer(jnf.player, jnf.numOfPlayers, jnf.easyRules, jnf.idFirstPlayer);
                out.println(new BooleanResponse(res));
                if(res){
                    addPlayingClient(client, jnf.idFirstPlayer);
                }
                return true;
            } else {
                wrongFormatted = true;
            }
        }
        case ("Join") -> {
            Optional<Join> oj = Join.fromJson(json);
            if (oj.isPresent()) {
                Join j = oj.get();
                JoinResponse joinResponse;
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
                    addPlayingClient(client, joinResponse.id);
                }
                out.println(joinResponse.toJson());
                tryStartGame();
                return true;
            } else {
                wrongFormatted = true;
            }
        }
        case "GetSavedGames" -> {
            Optional<GetSavedGames> gsg = GetSavedGames.fromJson(json);
            if (gsg.isPresent()) {
                out.println(new SavedGames(lobby.getSavedGames()).toJson());
                return true;
            } else {
                wrongFormatted = true;
            }
        }
        case "LoadGame" -> {
            Optional<LoadGame> lgo = LoadGame.fromJson(json);
            if (lgo.isPresent()) {
                LoadGame lg = lgo.get();
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
                out.println(loadGameResponse.toJson());
                return true;
            } else {
                wrongFormatted = true;
            }
        }
        case "GetLoadedPlayers" -> {
            Optional<GetLoadedPlayers> glp = GetLoadedPlayers.fromJson(json);
            if (glp.isPresent()) {
                if (!isGameActive()) {
                    Set<String> pns = new HashSet<>(lobby.getLoadedPlayersNames());
                    out.println(new LoadedGamePlayers(pns).toJson());
                } else {
                    out.println(new LoadedGamePlayers(new HashSet<>()).toJson());
                }
                return true;
            } else {
                wrongFormatted = true;
            }
        }
        case ("JoinLoadedAsFirst") -> {
            Optional<JoinLoadedAsFirst> jlfo = JoinLoadedAsFirst.fromJson(json);
            if (jlfo.isPresent()) {
                JoinLoadedAsFirst jlf = jlfo.get();
                BooleanResponse br;
                try {
                    boolean res = lobby.joinLoadedGameFirstPlayer(jlf.player, jlf.idFirstPlayer);
                    br = new BooleanResponse(res);
                    if(res){
                        addPlayingClient(client, jlf.idFirstPlayer);
                    }
                } catch (NicknameException e) {
                    br = new BooleanResponse(false);
                }
                out.println(br.toJson());
                return true;
            } else {
                wrongFormatted = true;
            }
        }
        case ("Disconnect") -> {
            Optional<Disconnect> d = Disconnect.fromJson(json);
            if (d.isPresent()) {
                BooleanResponse br;
                if (isGameActive()) {
                    boolean res = playController.leave(playersIds.get(client));
                    if (res) {
                        // TODO: to finish
                        notifyDisconnection(playersIds.get(client));
                    }
                    br = new BooleanResponse(res);
                } else {
                    br = new BooleanResponse(true);
                }
                out.println(br.toJson());
                closeClient(client);
                return false;
            } else {
                wrongFormatted = true;
            }
        }
        case "Reconnect" -> {
            Optional<Reconnect> ro = Reconnect.fromJson(json);
            if (ro.isPresent()) {
                String id = ro.get().getId();
                BooleanResponse br;
                if (isGameActive()) {
                    String name = lobby.getNameFromId(id);
                    if (name != null) {
                        boolean res = playController.reconnect(name);
                        br = new BooleanResponse(res);
                        if(res){
                            addPlayingClient(client, id);
                        }
                    }
                    else
                        br = new BooleanResponse(false);
                } else {
                    br = new BooleanResponse(false);
                }
                out.println(br.toJson());
                return true;
            } else {
                wrongFormatted = true;
            }
        }
        case "PickTilesCommand" -> {
            Optional<PickTilesCommand> ptco = PickTilesCommand.fromJson(json);
            if (ptco.isPresent()) {
                PickTilesCommand ptc = ptco.get();
                BooleanResponse br;
                String namep = lobby.getNameFromId(ptc.getId());
                if (isGameActive() && namep != null) {
                    br = new BooleanResponse(playController.pickTiles(new ArrayList<>(ptc.tiles), namep));
                } else {
                    br = new BooleanResponse(false);
                }
                out.println(br.toJson());
                return true;
            } else {
                wrongFormatted = true;
            }
        }
        case "PutTilesCommand" -> {
            Optional<PutTilesCommand> putco = PutTilesCommand.fromJson(json);
            if (putco.isPresent()) {
                PutTilesCommand ptc = putco.get();
                BooleanResponse br;
                String namep = lobby.getNameFromId(ptc.getId());
                if (isGameActive() && namep != null) {
                    List<Tile> tilesPut = ptc.tiles.stream().map(Tile::new).toList();
                    br = new BooleanResponse(playController.putTiles(tilesPut, ptc.column, namep));
                } else {
                    br = new BooleanResponse(false);
                }
                out.println(br.toJson());
                return true;
            } else {
                wrongFormatted = true;
            }
        }
        case "SaveGame" -> {
            Optional<SaveGame> sgo = SaveGame.fromJson(json);
            if (sgo.isPresent()) {
                SaveGame sg = sgo.get();
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
                return true;
            } else {
                wrongFormatted = true;
            }
        }
        case "SendMessage" -> {
            Optional<SendMessage> smo = SendMessage.fromJson(json);
            if (smo.isPresent()) {
                SendMessage sm = smo.get();
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
                return true;
            } else {
                wrongFormatted = true;
            }
        }
}

*/
