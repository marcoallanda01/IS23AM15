package it.polimi.ingsw.server.communication;

import it.polimi.ingsw.communication.commands.*;
import it.polimi.ingsw.communication.responses.*;
import it.polimi.ingsw.server.controller.*;
import it.polimi.ingsw.server.controller.exceptions.FirstPlayerAbsentException;
import it.polimi.ingsw.server.model.TileType;
import it.polimi.ingsw.server.model.exceptions.PlayerNotFoundException;
import it.polimi.ingsw.server.model.Tile;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class TCPServer extends ResponseServer implements ServerCommunication{
    private final List<Socket> clients;
    private final List<Socket> clientsInGame;
    private final Map<Socket, String> playersIds;
    private final Map<Socket, PrintWriter> clientsToOut;
    private final String lockOnLocks = "outLocks";
    private final Map<Socket, String> clientsToLockOut; //lock of the map -- listen and closeClient
    private final int port;
    private ServerSocket serverSocket;
    private final ExecutorService executorService;

    public TCPServer(int port, Lobby lobby, String sharedLock){
        super(lobby, sharedLock);
        this.port = port;
        this.clients = Collections.synchronizedList(new ArrayList<>());
        this.clientsInGame = Collections.synchronizedList(new ArrayList<>());
        this.playersIds = Collections.synchronizedMap(new HashMap<>());
        this.clientsToOut = Collections.synchronizedMap(new HashMap<>());
        this.clientsToLockOut = new HashMap<>();
        this.executorService = Executors.newCachedThreadPool();
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
        System.out.println("TCP Server socket opened");
    }

    /**
     * Add client
     * @param client client's socket
     * @return true if add succeeded
     */
    private boolean addClient(Socket client) throws IOException {
        boolean res = clients.add(client);
        startPingPong(client, ANONYMOUS_PING_ID);
        clientsToOut.put(client, new PrintWriter(client.getOutputStream(), true));
        synchronized (lockOnLocks){
            clientsToLockOut.put(client, client.toString());
        }
        System.out.println("Client added: "+client+" "+res);
        return res;
    }

    /**
     * Write something on client output stream
     * @param client client's socket
     * @param content message to send
     */
    private void sendToClient(Socket client, String content){
        this.executorService.submit(()-> {
                    String clientLock;
                    synchronized (lockOnLocks){
                        clientLock = clientsToLockOut.get(client);
                    }
                    if(clientLock != null){
                        // clientLock is a reference because map.get() returns a reference
                        synchronized (clientLock){
                            System.out.println("Lock "+clientLock+" acquired");
                            PrintWriter out = clientsToOut.get(client);
                            if(out != null){

                                //this lock is separated because I don't care if now the client is deleted
                                //the message is sent but no one receive it
                                out.println(content);
                                System.out.println("Sent to "+client+": "+content);

                            }
                            else{
                                System.err.println("TCP Error in writing on "+clientLock);
                            }
                            System.out.println("Lock "+clientLock+" released");
                        }
                    }
                }
        );
    }

    /**
     * Method for close a client
     * @param client client's socket
     */
    private void closeClient(Socket client){
        this.clients.remove(client);
        this.clientsInGame.remove(client);
        PrintWriter out = this.clientsToOut.remove(client);
        if(out != null){
            out.close();
        }
        synchronized (lockOnLocks){
            this.clientsToLockOut.remove(client);
        }
        System.out.println("Closing "+client+"...");
        try {
            Scanner in = new Scanner(client.getInputStream());
            in.close();
            client.close();
        }catch (IOException b){
            System.out.println("Socket was already closed:");
            b.printStackTrace();
        }
    }

    /**
     * Function that listen always for new TCP connection
     */
    public void listenForConnections(){
        System.out.println("TCP Server listening to new connections...");
        /*
        TimerTask closeInactiveClientsTask = new TimerTask() {
            public void run() {
                System.out.println("TCPServer: Cleaning inactive sockets...");
                List<Socket> toClose = clients.stream()
                            .filter(Socket::isInputShutdown).toList();
                int closed = toClose.size();
                System.out.println("TCPServer: Trying to close "+closed+" inactive clients...");
                toClose.forEach(socket -> {closeClient(socket);});
            }
        };
        new Timer().scheduleAtFixedRate(closeInactiveClientsTask, 20000, 30000);
        */
        while(true) {
            try {
                Socket clientSocket = serverSocket.accept();
                if(addClient(clientSocket)) {
                    this.executorService.submit(() -> {
                        clientHandler(clientSocket);
                    });
                }
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
        try {
            in = new Scanner(client.getInputStream());
        }
        catch (IOException e){
            e.printStackTrace();
            closeClient(client);
            return;
        }
        String json;
        do {
            json = in.nextLine();
            System.out.println("Received from "+client+": " + json);
        } while (respond(client, json));

        in.close();
        closeClient(client);
    }

    /**
     * Method for add a playing client
     * @param client client socket
     */
    private synchronized void addPlayingClient(Socket client, String id){
        if(!clientsInGame.contains(client)){
            clientsInGame.add(client);
            playersIds.put(client, id);
            System.out.println("Client "+client+" added to playing clients");
        }
    }

    /**
     * Method to handle a client request
     * @param client client to witch respond
     * @param json json string that client sent
     * @return true if client still connected
     */
    private boolean respond(Socket client, String json){
        String commandName;
        Optional<String> oName = Command.nameFromJson(json);
        if(oName.isPresent()) {
            commandName = oName.get();
            boolean wrongFormatted = false;

            switch (commandName) {
                case ("HelloCommand") -> {
                    Optional<HelloCommand> hc = HelloCommand.fromJson(json);
                    if (hc.isPresent()) {
                        Hello hello = respondHello(hc.get(), client);
                        sendToClient(client, hello.toJson());
                        return true;
                    } else {
                        wrongFormatted = true;
                    }
                }
                case ("JoinNewAsFirst") -> {
                    Optional<JoinNewAsFirst> ojnf = JoinNewAsFirst.fromJson(json);
                    if (ojnf.isPresent()) {
                        FirstJoinResponse fjr = respondJoinNewAsFirst(ojnf.get(), client);
                        sendToClient(client, fjr.toJson());
                        return true;
                    } else {
                        wrongFormatted = true;
                    }
                }
                case ("Join") -> {
                    Optional<Join> oj = Join.fromJson(json);
                    if (oj.isPresent()) {
                        JoinResponse joinResponse = null;
                        try {
                            joinResponse = respondJoin(oj.get(), client);
                            sendToClient(client, joinResponse.toJson());
                        } catch (FirstPlayerAbsentException e) {
                            System.out.println(client+" tried to join without a first player preset!");
                        }
                        return true;
                    } else {
                        wrongFormatted = true;
                    }
                }
                case "GetSavedGames" -> {
                    Optional<GetSavedGames> gsg = GetSavedGames.fromJson(json);
                    if (gsg.isPresent()) {
                        SavedGames sg = respondGetSavedGames(gsg.get());
                        sendToClient(client, sg.toJson());
                        return true;
                    } else {
                        wrongFormatted = true;
                    }
                }
                case "LoadGame" -> {
                    Optional<LoadGame> lgo = LoadGame.fromJson(json);
                    if (lgo.isPresent()) {
                        LoadGame lg = lgo.get();
                        LoadGameResponse loadGameResponse = respondLoadGame(lg);
                        sendToClient(client, loadGameResponse.toJson());
                        return true;
                    } else {
                        wrongFormatted = true;
                    }
                }
                case "GetLoadedPlayers" -> {
                    Optional<GetLoadedPlayers> glp = GetLoadedPlayers.fromJson(json);
                    if (glp.isPresent()) {
                        LoadedGamePlayers loadedGamePlayers = respondGetLoadedPlayers(glp.get());
                        sendToClient(client, loadedGamePlayers.toJson());;
                        return true;
                    } else {
                        wrongFormatted = true;
                    }
                }
                case ("JoinLoadedAsFirst") -> {
                    Optional<JoinLoadedAsFirst> jlfo = JoinLoadedAsFirst.fromJson(json);
                    if (jlfo.isPresent()) {
                        JoinLoadedAsFirst jlf = jlfo.get();
                        FirstJoinResponse firstJoinResponse = respondJoinLoadedAsFirst(jlf, client);
                        sendToClient(client, firstJoinResponse.toJson());
                        return true;
                    } else {
                        wrongFormatted = true;
                    }
                }
                case ("Disconnect") -> {
                    Optional<Disconnect> d = Disconnect.fromJson(json);
                    if (d.isPresent()) {
                        respondDisconnect(d.get(), client);
                        return false;
                    } else {
                        wrongFormatted = true;
                    }
                }
                case "Reconnect" -> {
                    Optional<Reconnect> ro = Reconnect.fromJson(json);
                    if (ro.isPresent()) {
                        respondReconnect(ro.get(), client);
                        return true;
                    } else {
                        wrongFormatted = true;
                    }
                }
                case "PickTilesCommand" -> {
                    Optional<PickTilesCommand> ptco = PickTilesCommand.fromJson(json);
                    if (ptco.isPresent()) {
                        PickTilesCommand ptc = ptco.get();
                        respondPickTiles(ptc);
                        return true;
                    } else {
                        wrongFormatted = true;
                    }
                }
                case "PutTilesCommand" -> {
                    Optional<PutTilesCommand> putco = PutTilesCommand.fromJson(json);
                    if (putco.isPresent()) {
                        PutTilesCommand ptc = putco.get();
                        respondPutTiles(ptc);
                        return true;
                    } else {
                        wrongFormatted = true;
                    }
                }
                case "SaveGame" -> {
                    Optional<SaveGame> sgo = SaveGame.fromJson(json);
                    if (sgo.isPresent()) {
                        respondSaveGame(sgo.get());
                        return true;
                    } else {
                        wrongFormatted = true;
                    }
                }
                case "SendMessage" -> {
                    Optional<SendMessage> smo = SendMessage.fromJson(json);
                    if (smo.isPresent()) {
                        respondSendMessage(smo.get());
                        return true;
                    } else {
                        wrongFormatted = true;
                    }
                }
                case "Pong" -> {
                    Optional<Pong> po = Pong.fromJson(json);
                    System.out.println("\u001B[94mPong Optional received from "+client+"\u001B[0m");
                    if (po.isPresent()) {
                        respondPong(po.get(), client);
                        return true;
                    } else {
                        wrongFormatted = true;
                    }
                }
                default -> System.err.println("GameCommand from " + client.toString() +
                        " with name: " + commandName + " can not be found");
            }
            if(wrongFormatted){
                System.err.println("GameCommand from "+client.toString()+
                        " cannot be understood because wrong formatted: "+json);
            }
        }else{
            System.err.println("GameCommand from "+client.toString()+" empty");
        }
        return true;
    }

    /**
     * Notify all clients that game have been saved
     *
     * @param name name of the save
     */
    @Override
    public void notifyGameSaved(String name) {
        this.clientsInGame.forEach((c) -> {
            sendToClient(c,
                    new GameSaved(name).toJson()
            );
        });
    }

    /**
     * Close a client connection
     *
     * @param client client object. Client is cast to Socket
     */
    @Override
    protected void closeClient(Object client) {
        closeClient((Socket) client);
    }

    /**
     * Get name of a playing client form his connection
     *
     * @param client client object. A cast is needed
     * @return client's nickname. Return null if client is not in game
     */
    @Override
    protected String getPlayerNameFromClient(Object client) {
        return this.lobby.getNameFromId(this.playersIds.get((Socket) client));
    }

    /**
     * Add a playing client
     * @param client (casted to Socket)
     */
    @Override
    protected void addPlayingClient(Object client, String id) {
        Socket socket = (Socket) client;
        addPlayingClient(socket, id);
    }


    /**
     * Send to all clients game set up
     */
    @Override
    public void gameSetUp() {
        tryStartGame();
        synchronized (playLock){
            this.clientsInGame.forEach((c) -> {
                try {
                    sendToClient(c,
                            new GameSetUp(
                                    playController.getPlayers(),
                                    new ArrayList<>(playController.getEndGameGoals()),
                                    playController.getPersonalGoalCard(getPlayerNameFromClient(c))
                            ).toJson()
                    );
                } catch (PlayerNotFoundException e) {
                    System.out.println("GameSetUp: This player do not exists " + getPlayerNameFromClient(c));
                }
            });
        }
    }

    /**
     * Notify to all clients that a player disconnected
     * @param playerName name of the player who disconnected
     */
    @Override
    public void notifyDisconnection(String playerName) {
        this.clientsInGame.forEach(c->{
            sendToClient(c, new Disconnection(playerName).toJson());
        });
    }

    /**
     * Notify to all clients that a player reconnected
     * @param playerName name of the player who reconnected
     */
    @Override
    public void notifyReconnection(String playerName) {
        Socket reconnectedPlayer = null;
        for(Socket c : this.clientsInGame){
            if(getPlayerNameFromClient(c).equals(playerName)){
                reconnectedPlayer = c;
                System.out.println("TCP notifyReconnection: client reconnected "+reconnectedPlayer+" "+playerName);
            }
        }
        Socket finalReconnectedPlayer = reconnectedPlayer;
        this.clientsInGame.forEach(c->{
            sendToClient(c, new Reconnected(playerName).toJson());
            if(!getPlayerNameFromClient(c).equals(playerName)){
                synchronized (playLock){
                    try {
                        sendToClient(finalReconnectedPlayer,
                                new BookShelfUpdate(
                                        getPlayerNameFromClient(c),
                                        playController.getBookshelf(getPlayerNameFromClient(c))
                                ).toJson()
                        );
                    } catch (PlayerNotFoundException e) {
                        System.err.println("notifyReconnection: "+e.getMessage());
                    }
                }
            }

        });
        synchronized (playLock){
            try{
                sendToClient(reconnectedPlayer,
                        new GameSetUp(
                                playController.getPlayers(),
                                new ArrayList<>(playController.getEndGameGoals()),
                                playController.getPersonalGoalCard(playerName)
                        ).toJson()
                );
                sendToClient(reconnectedPlayer, new CommonCards(playController.getCommonGoalCardsToTokens()).toJson());
                sendToClient(reconnectedPlayer, new BoardUpdate(playController.getBoard()).toJson());
            } catch (PlayerNotFoundException e) {
                System.err.println("Cannot handle GameSetUp reconnection of "+reconnectedPlayer);
            }
        }
    }

    /**
     * Send a message to all players
     * @param sender sender's name
     * @param date date of message creation
     * @param message actual message to be sent
     */
    @Override
    public void notifyMessage(String sender, String date, String message) {
        this.clientsInGame.forEach(c->{
            sendToClient(c, new ChatMessage(sender, date, message).toJson());
        });
    }

    /**
     * Send a message to all players
     * @param sender sender's name
     * @param date date of message creation
     * @param message actual message to be sent
     * @param receiver receiver's name
     */
    @Override
    public void notifyMessage(String sender, String date, String message, String receiver) {
        System.out.println(new ChatMessage(sender, date, message).toJson());
        this.playersIds.entrySet().stream()
                .filter(entry -> Objects.equals(entry.getValue(), lobby.getIdFromName(receiver)))
                .map(Map.Entry::getKey)
                .findFirst()
                .ifPresent(
                        c -> sendToClient(c, new ChatMessage(sender, date, message).toJson())
                );
    }

    /**
     * Notify change in the board to all clients in game
     * @param tiles board
     */
    @Override
    public void notifyChangeBoard(List<Tile> tiles) {
        this.clientsInGame.forEach(c->{
            sendToClient(c, new BoardUpdate(new HashSet<>(tiles)).toJson());
        });
    }

    /**
     * Notify to all clients change in player's bookshelf
     * @param playerName player's name
     * @param tiles bookshelf
     */
    @Override
    public void notifyChangeBookShelf(String playerName, List<Tile> tiles) {
        this.clientsInGame.forEach(c->{
            sendToClient(c, new BookShelfUpdate(playerName, new HashSet<>(tiles)).toJson());
        });
    }

    /**
     * Notify change in point of a player to all clients
     * @param playerName player's name
     * @param points new points
     */
    @Override
    public void updatePlayerPoints(String playerName, int points) {
        this.clientsInGame.forEach(c->{
            sendToClient(c, new PlayerPoints(playerName, points).toJson());
        });
    }

    /**
     * Notify to all player whom turn is
     * @param playerName current player
     */
    @Override
    public void notifyTurn(String playerName) {
        this.clientsInGame.forEach(c->{
            sendToClient(c, new TurnNotify(playerName).toJson());
        });
    }

    /**
     * Notify to all clients a change in common goals cards and tokens
     * @param cardsAndTokens cards with associated tokens
     */
    @Override
    public void sendCommonGoalsCards(Map<String, List<Integer>> cardsAndTokens) {
        this.clientsInGame.forEach(c->{
            sendToClient(c, new CommonCards(cardsAndTokens).toJson());
        });
    }

    /**
     * Notify the winner to all playing clients, close all playing clients, reset lobby
     * @param playerName winner's game
     */
    @Override
    public void notifyWinner(String playerName) {
        this.clientsInGame.forEach(c->{
            sendToClient(c, new Winner(playController.getWinner()).toJson());
        });
        // Close all playing clients
        this.clientsInGame.forEach(this::closeClient);
        // Reset lobby
        reset();
    }

    /**
     * Send notification of tiles picked to all players
     *
     * @param player player's name who picked
     * @param tiles  list of picked tiles
     */
    @Override
    public void notifyPickedTiles(String player, List<TileType> tiles) {
        this.clientsInGame.forEach(c->{
            sendToClient(c, new TilesPicked(player, tiles).toJson());
        });
    }

    /**
     * Handle the disconnection of the last player terminating the game
     */
    @Override
    public void handleLastPlayerDisconnection() {
        reset();
    }

    /**
     * Ping a client to keep connection alive
     * @param client object (Cast to Socket)
     */
    @Override
    protected void ping(Object client){
        Socket clientSock = (Socket) client;
        sendToClient(clientSock, new Ping().toJson());
    }

    /**
     * Send an error message to a player
     *
     * @param player  player's name
     * @param message Error message
     */
    @Override
    protected void sendErrorMessage(String player, String message) {
        this.playersIds.forEach((s, id) ->{
            if(id.equals(player)){
                sendToClient(s, new ErrorMessage(message).toJson());
            }
        });
    }

    /**
     * Send an error message to all players
     *
     * @param message Error message
     */
    @Override
    protected void sendErrorMessageToAll(String message) {
        this.playersIds.forEach((s, id) ->{
            sendToClient(s, new ErrorMessage(message).toJson());
        });
    }

    /**
     * Handle reconnection of a client sending them all the necessary.
     *
     * @param client client's object
     */
    /*
    @Override
    protected void handleReconnection(Object client) {
        Socket socket = (Socket) client;
        synchronized (playLock){
            String playerName = getPlayerNameFromClient(socket);
            try{
                sendToClient(socket,
                        new GameSetUp(
                                playController.getPlayers(),
                                new ArrayList<>(playController.getEndGameGoals()),
                                playController.getPersonalGoalCard(playerName)
                        ).toJson()
                );
                sendToClient(socket, new BookShelfUpdate(playerName, playController..toJson());

            } catch (PlayerNotFoundException e) {
                System.err.println("Cannot handle reconnection of "+socket);
            }
        }
    }
    */
}