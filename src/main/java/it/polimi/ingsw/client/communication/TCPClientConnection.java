package it.polimi.ingsw.client.communication;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class TCPClientConnection {
    private Socket socket;
    private ExecutorService executorService;

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

    public void notificationHandler(Socket server) {
        Scanner in;
        try {
            in = new Scanner(server.getInputStream());
        }
        catch (IOException e){
            e.printStackTrace();
            closeConnection();
            return;
        }
        String json;
        do {
            json = in.nextLine();
            System.out.println("Received from "+server.getLocalSocketAddress().toString()+": " + json);
        } while (handleNotification(json));
        in.close();
        closeConnection();
    }

    private boolean handleNotification(String json) {
        return true;
    }

    public void sendToServer(String json) {
        executorService.submit(()->{sendStringToServer(json);});
    }

    private void sendStringToServer(String json) {
        synchronized (socket) {
            try {
                // Create output stream for communication with the server
                PrintWriter out = new PrintWriter(socket.getOutputStream());
                out.println(json);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public Future<String> receiveFromServer(Predicate<String> isExpectedResponse) {
        return executorService.submit(() -> receiveStringFromServer(isExpectedResponse));
    }

    private String receiveStringFromServer(Predicate<String> isExpectedResponse) {
        synchronized (socket) {
            try {
                // Create output stream for communication with the server
                Scanner in = new Scanner(socket.getInputStream());
                String json = in.nextLine();
                while(!isExpectedResponse.test(json)) {
                    handleNotification(json);
                }
                return json;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
    private void listenForNotifications() {
        synchronized (socket) {
            try {
                // Create output stream for communication with the server
                Scanner in = new Scanner(socket.getInputStream());
                String json = in.nextLine();
                while(true) {
                    handleNotification(json);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
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
}