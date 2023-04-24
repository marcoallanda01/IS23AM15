package it.polimi.ingsw.communication;


import java.io.*;
import java.net.Socket;

public class TCPClient {
    private Socket socket;

    public void openConnection() {
        String serverHost = Settings.SERVER_NAME; // Server hostname or IP address
        int serverPort = Settings.PORT; // Server port number

        try {
            // Create a socket to connect to the server
            socket = new Socket(serverHost, serverPort);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void closeConnection() {
        try {
            if (socket != null) {
                socket.close();
                System.out.println("Socket closed.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendObjectToServer(Object object) {
        try {
            // Create output stream for communication with the server
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());

            // Send serialized object to the server
            out.writeObject(object);
            out.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Object receiveObjectFromServer() {
        try {
            // Create input stream for communication with the server
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

            // Read serialized response object from the server
            Object serverResponse = in.readObject();
            return serverResponse;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static class Settings {
        public static int PORT = 1234;
        public static String SERVER_NAME = "127.0.0.1";

    }
}