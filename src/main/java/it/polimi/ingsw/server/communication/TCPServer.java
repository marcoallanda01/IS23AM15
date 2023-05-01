package it.polimi.ingsw.server.communication;

import it.polimi.ingsw.communication.commands.*;
import it.polimi.ingsw.communication.responses.BooleanResponse;
import it.polimi.ingsw.communication.responses.LoadedGamePlayers;
import it.polimi.ingsw.communication.responses.SavedGames;
import it.polimi.ingsw.server.controller.ChatController;
import it.polimi.ingsw.server.controller.Lobby;
import it.polimi.ingsw.server.controller.PlayController;
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
    private PlayController playController;
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

    private void closeClient(Socket client){
        this.clients.remove(client);
        try {
            client.close();
        }catch (IOException b){
            b.printStackTrace();
        }
    }

    /**
     *
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

                case ("Disconnect"):
                    Optional<Disconnect> d = Disconnect.fromJson(json);
                    if (d.isPresent()) {
                        BooleanResponse br;
                        if(isGameActive()){
                            boolean res = playController.leave(playersIds.get(client));
                            if(res) {
                                notifyDisconnection(playersIds.get(client));
                            }
                            br = new BooleanResponse(res);
                        }
                        else {
                            br = new BooleanResponse(true);
                        }
                        out.println(br.toJson());
                        closeClient(client);
                        return false;
                    } else {
                        wrongFormatted = true;
                        break;
                    }

                case "GetLoadedPlayers":
                    //TODO: recheck protocol
                    Optional<GetLoadedPlayers> glp = GetLoadedPlayers.fromJson(json);
                    if (glp.isPresent()) {
                        if(!isGameActive()) {
                            Set<String> pns = new HashSet<>(lobby.getLoadedPlayersNames());
                            out.println(new LoadedGamePlayers(pns).toJson());
                        }
                        else {
                            out.println(new LoadedGamePlayers(new HashSet<>()).toJson());
                        }
                        return true;
                    } else {
                        wrongFormatted = true;
                        break;
                    }

                case "GetSavedGames":
                    Optional<GetSavedGames> gsg = GetSavedGames.fromJson(json);
                    if(gsg.isPresent()){
                        out.println(new SavedGames(lobby.getSavedGames()).toJson());
                    }else {
                        wrongFormatted = true;
                        break;
                    }
                    break;
                case "HelloCommand":
                    Optional<HelloCommand> hc = HelloCommand.fromJson(json);
                    if(hc.isPresent()){
                        if(isGameActive()){

                        }
                    }
                    else{
                        wrongFormatted = true;
                        break;
                    }
                    break;
                case "Join":
                    break;
                case "JoinLoadedAsFirst":
                    break;
                case "JoinNewAsFirst":
                    break;
                case "LoadGame":
                    break;
                case "PickTilesCommand":
                    break;
                case "Pong":
                    break;
                case "PutTilesCommand":
                    break;
                case "Reconnect":
                    break;
                default:
                    System.err.println("GameCommand from "+client.getLocalSocketAddress().toString()+
                            " with name: "+commandName+" can not be found");
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
        synchronized (playController){
            playControllerActive = (playController != null);
        }
        return playControllerActive;
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