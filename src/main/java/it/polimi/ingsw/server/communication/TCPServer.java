package it.polimi.ingsw.server.communication;

import it.polimi.ingsw.communication.commands.*;
import it.polimi.ingsw.communication.responses.*;
import it.polimi.ingsw.server.controller.*;
import it.polimi.ingsw.server.model.PlayerNotFoundException;
import it.polimi.ingsw.server.model.Tile;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TCPServer implements ServerCommunication{
    private List<Socket> clients;
    private Map<Socket, String> playersIds;

    private Lobby lobby;

    private ControllerProvider controllerProvider = null;
    private PlayController playController = null;
    private ChatController chatController = null;
    private int port;
    private ServerSocket serverSocket;

    public TCPServer(int port, Lobby lobby){
        this.port = port;
        this.lobby = lobby;
        this.playController = null;
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    /**
     * Function that listen always for new TCP connection
     */
    public void listenForConnections(){
        ExecutorService executorService = Executors.newCachedThreadPool();
        while(true) {
            try {
                Socket clientSocket = serverSocket.accept();
                clients.add(clientSocket);
                executorService.submit(()->{clientHandler(clientSocket);});
            } catch(IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Function to listen for client request
     * @param client client
     */
    public void clientHandler(Socket client){
        Scanner in;
        PrintWriter out;
        try {
            in = new Scanner(client.getInputStream());
            out = new PrintWriter(client.getOutputStream());
        }
        catch (IOException e){
            e.printStackTrace();
            closeClient(client);
            return;
        }
        String json;
        do {
            json = in.nextLine();
            System.out.println("Received from "+client.getLocalSocketAddress().toString()+": " + json);

        } while (respond(client, out, json));

        System.out.println("Closing sockets of "+client.getLocalSocketAddress().toString());
        in.close();
        out.close();
        closeClient(client);
    }

    /**
     * Method for close a client
     * @param client client
     */
    private void closeClient(Socket client){
        this.clients.remove(client);
        try {
            client.close();
        }catch (IOException b){
            b.printStackTrace();
        }
    }

    /**
     * Method to handle a client request
     * @param client client to witch respond
     * @param json json string that client sent
     * @return true if client still connected
     */
    private boolean respond(Socket client, PrintWriter out, String json){
        String commandName;
        Optional<String> oName = Command.nameFromJson(json);
        if(oName.isPresent()) {
            commandName = oName.get();
            boolean wrongFormatted = false;

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
                            br = new BooleanResponse(lobby.joinLoadedGameFirstPlayer(jlf.player, jlf.idFirstPlayer));
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
                            if (name != null)
                                br = new BooleanResponse(playController.reconnect(name));
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
                default -> System.err.println("GameCommand from " + client.getLocalSocketAddress().toString() +
                        " with name: " + commandName + " can not be found");
            }
            if(wrongFormatted){
                System.err.println("GameCommand from "+client.getLocalSocketAddress().toString()+
                        " cannot be understood because wrong formatted");
            }
        }else{
            System.err.println("GameCommand from "+client.getLocalSocketAddress().toString()+" empty");
        }
        return true;
    }

    /**
     * Check if we are in the playing phase of the game
     * @return true if we are
     */
    private boolean isGameActive(){
        boolean playControllerActive;
        synchronized (controllerProvider) {
            playControllerActive  = (controllerProvider != null);
        }
        return playControllerActive;
    }

    private void tryStartGame(){
        synchronized (controllerProvider){
            try {
                controllerProvider = lobby.startGame();
                playController = controllerProvider.getPlayController();
                chatController = controllerProvider.getChatController();
                System.out.println("Player joined, game started!");
            } catch (EmptyLobbyException e) {
                System.out.println("Player joined, but lobby not full!");
            }
        }
    }

    /*
    private void putTiles(Socket client, PutTilesCommand dc){
        boolean ok = playcontroller.putTiles();
        BooleanResponse br = new BooleanResponse(ok);
        PrintWriter out = new PrintWriter(client.getOutputStream());
        out.println(br.toJson());
    }
    */

    /**
     *
     */
    @Override
    public void gameSetUp() {

    }

    /**
     * @return
     */
    @Override
    public boolean sendWinner() {
        return false;
    }

    /**
     * @param playerId player that disconnecter
     */
    @Override
    public void notifyDisconnection(String playerId) {

    }

    /**
     * @param playerId
     */
    @Override
    public void notifyReconnection(String playerId) {

    }

    /**
     * @param tiles
     */
    @Override
    public void notifyChangeBoard(List<Tile> tiles) {

    }

    /**
     * @param playerName
     * @param tiles
     */
    @Override
    public void notifyChangeBookShelf(String playerName, List<Tile> tiles) {

    }

    /**
     * @param playerName
     * @param points
     */
    @Override
    public void updatePlayerPoints(String playerName, int points) {

    }

    /**
     * @param playerName
     */
    @Override
    public void notifyTurn(String playerName) {

    }

    /**
     * @param cardsAndTokens
     */
    @Override
    public void sendCommonGoalsCards(Map<String, List<Integer>> cardsAndTokens) {

    }

    /**
     * @param card
     * @param tokens
     */
    @Override
    public void notifyChangeToken(String card, List<Integer> tokens) {

    }
}