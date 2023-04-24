package it.polimi.ingsw.server.communication;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TCPServer {
    private List<Socket> clients;
    private int port;

    private ServerSocket serverSocket;

    public TCPServer(int port){
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
                executorService.submit(()->{requestHandler(clientSocket);});
            } catch(IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void requestHandler(Socket client){
        Scanner in;
        PrintWriter out;
        try {
            in = new Scanner(client.getInputStream());
            out = new PrintWriter(client.getOutputStream());
        }
        catch (IOException e){
            e.printStackTrace();

            this.clients.remove(client);
            try {
                client.close();
            }catch (IOException b){
                b.printStackTrace();
            }

            return;
        }
        // Leggo e scrivo nella connessione finche' non ricevo "quit"
        String json;
        do {
            json = in.nextLine();
            out.println("Received: " + json);
            out.flush();

        } while (NotificationHandler.respond(client, json));

        System.out.println("Closing sockets");
        in.close();
        out.close();
    }

/*
    public void openConnection() {


        try {
            // Create a server socket
            serverSocket = new ServerSocket(port);
            System.out.println("Server is listening on port " + port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void closeConnection() {
        try {
            if (serverSocket != null) {
                serverSocket.close();
                System.out.println("Server socket closed.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void acceptConnections() {
        while (true) {
            // Accept incoming client connections
            Socket clientSocket;
            try{
                clientSocket = serverSocket.accept();
            }catch (IOException e) {
                e.printStackTrace();
                break;
            }
            System.out.println("Client connected: " + clientSocket.getInetAddress().getHostAddress());
            clientSockets.add(clientSocket);
        }
    }

    public void sendMsgToAll(Msg msg) {
        try {
            for (Socket clientSocket : clientSockets) {
                // Create output stream for communication with the client
                ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());

                // Send serialized object to the client
                out.writeObject(msg.toJson());
                out.flush();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // used to send response objects
    public void sendMsgToClient(String clientId, Msg msg) {

    }
    */
}