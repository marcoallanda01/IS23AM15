package it.polimi.ingsw.server;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import it.polimi.ingsw.server.communication.RMIServerApp;
import it.polimi.ingsw.server.communication.TCPServer;
import it.polimi.ingsw.server.controller.Lobby;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static java.lang.System.exit;

public class ServerApp {
    //default config
    private static int rmi_port  = 7001;
    private static int tcp_port = 6000;
    private static String saves = "saves";

    /**
     * Parse json set up file
     */
    private static void parseJson(String file_setup){

        Map<String, Object> setUpMap = null;
        try {
            Reader reader = Files.newBufferedReader(Paths.get(file_setup));
            Gson gson = new Gson();
            setUpMap = gson.fromJson(reader, Map.class);
            reader.close();
        } catch (IOException | RuntimeException e) {
            System.err.println("Can not read set up file!");
        }

        try{
            rmi_port = ((Double)setUpMap.get("rmi")).intValue();
            System.out.println("Rmi set to port: "+rmi_port);
        }catch (RuntimeException e){
            System.err.println("Rmi set to default port: "+rmi_port);
        }
        try{
            tcp_port = ((Double)setUpMap.get("tcp")).intValue();
            System.out.println("Tcp set to port: "+tcp_port);
        }catch (RuntimeException e){
            System.err.println("Tcp set to default port: "+tcp_port);
        }
        if(rmi_port == tcp_port){
            System.err.println("Server can not starts if ports are the same!");
            exit(1);
        }
        try{
            saves = (String) setUpMap.get("directory");
            if(saves == null){
                throw new NullPointerException("Saves directory can not be null");
            }
            System.out.println("Saves directory set up to: "+ saves);
        }catch (RuntimeException e){
            System.err.println("Saves directory set to default: " + saves);
        }
    }

    /**
     * Start server.
     * Pass as args a json sent up file es: "ServerApp setup.json". Pass no args tp.
     * Set up file example with default settings:
     * {
     *     "rmi" : 7001,
     *     "tcp" : 6000,
     *     "directory" : "saves"
     * }
     * @param args args passed
     */
    public static void main(String[] args) {

        System.out.println("Server configuration...");
        if(args.length == 1){
            String file_setup = args[0];
            parseJson(file_setup);
        }else{
            System.out.println("Standard configuration chosen");
            System.out.println("Rmi default port: "+rmi_port);
            System.out.println("Tcp default port: "+tcp_port);
            System.out.println("Saves default directory: "+saves);
        }

        System.out.println("Starting server...");

        String stringLock = "playLock";
        String sharedLock = stringLock.intern();

        Lobby lobby = new Lobby(saves);

        ExecutorService executorService = Executors.newCachedThreadPool();
        try {
            TCPServer serverTcp = new TCPServer(tcp_port, lobby, sharedLock);
            lobby.registerServer(serverTcp);
            executorService.submit(serverTcp::listenForConnections);
        }catch (RuntimeException e){
            System.err.println("TCP server failed to start!");
        }

        try {
            RMIServerApp serverRMI = new RMIServerApp(rmi_port, lobby, sharedLock);
            serverRMI.start();
            lobby.registerServer(serverRMI);
        } catch (AlreadyBoundException | RemoteException | RuntimeException e) {
            System.err.println("RMI server failed to start!");
        }
    }
}
