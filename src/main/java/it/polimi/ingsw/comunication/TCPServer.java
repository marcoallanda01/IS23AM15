package it.polimi.ingsw.comunication;

import it.polimi.ingsw.comunication.Settings;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class TCPServer {
    private ServerSocket serverSocket;
    private List<Socket> clientSockets;
    private Map<String, Socket> clientIdsToSockets;

    public void openConnection() {
        int port = Settings.PORT; // Port number to listen on

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
        try {
            while (true) {
                // Accept incoming client connections
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected: " + clientSocket.getInetAddress().getHostAddress());
                clientSockets.add(clientSocket);
                String id = null;
                do {
                    id = String.valueOf((new Random()).nextInt());
                }
                while(clientIdsToSockets.get(id) != null);
                clientIdsToSockets.put(id, clientSocket);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendObjectToClients(Object object) {
        try {
            for (Socket clientSocket : clientSockets) {
                // Create output stream for communication with the client
                ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());

                // Send serialized object to the client
                out.writeObject(object);
                out.flush();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // used to send response objects
    public void sendObjectToClient(String clientId, Object object) {
        try {
                // Create output stream for communication with the client
                ObjectOutputStream out = new ObjectOutputStream(clientIdsToSockets.get(clientId).getOutputStream());

                // Send serialized object to the client
                out.writeObject(object);
                out.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Object receiveObjectFromClient(Socket clientSocket) {
        try {
            // Create input stream for communication with the client
            ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream());

            // Read serialized request object from the client
            Object clientRequest = in.readObject();
            return clientRequest;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
}