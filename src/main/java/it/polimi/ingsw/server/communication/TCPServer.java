package it.polimi.ingsw.server.communication;

import it.polimi.ingsw.communication.commands.Command;
import it.polimi.ingsw.communication.commands.Disconnect;
import it.polimi.ingsw.communication.commands.GetLoadedPlayers;
import it.polimi.ingsw.server.controller.Lobby;
import it.polimi.ingsw.server.model.Tile;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TCPServer implements ServerCommunication{
    private List<Socket> clients;
    private Map<Socket, String> playersIds;
    private int port;

    private ServerSocket serverSocket;

    public TCPServer(int port, Lobby lobby){
        this.port = port;
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
            out.println("Received: " + json);
            out.flush();

        } while (respond(client, json));

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
    public boolean respond(Socket client, String json){
        String commandName;
        Optional<String> oName = Command.nameFromJson(json);
        if(oName.isPresent()) {
            commandName = oName.get();
            boolean wrongFormatted = false;
            switch (commandName) {
                case "Disconnect":
                    Optional <Disconnect> d = Disconnect.fromJson(json);
                    if(d.isPresent()) {
                        //if(){
                            notifyDisconnection(playersIds.get(client));

                        //}
                        closeClient(client);
                        return false;
                    }else{
                        wrongFormatted = true;
                    }
                case "GetLoadedPlayers":
                    Optional <GetLoadedPlayers> o = GetLoadedPlayers.fromJson(json);
                    if(o.isPresent()) {
                        //sendLoadedPlayers(client);
                        return true;
                    }else{
                        wrongFormatted = true;
                    }
                case "PutTileCommand":
                    break;
            }
            if(wrongFormatted){
                System.out.println("GameCommand from "+client.getLocalSocketAddress().toString()+
                        " cannot be understood because wrong formatted");
            }
        }else{
            System.out.println("GameCommand from "+client.getLocalSocketAddress().toString()+" empty");
        }
        return true;
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