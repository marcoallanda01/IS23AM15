package it.polimi.ingsw.client.communication;

import it.polimi.ingsw.communication.commands.HelloCommand;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static org.junit.jupiter.api.Assertions.assertEquals;


class TCPClientClientCommunicationTest {
    ExecutorService serverExecutorService = Executors.newCachedThreadPool();
    ClientNotificationListener clientNotificationListener;
    TCPClientConnection tcpClientConnection;
    List<String> notificationsSentToTheListener = new ArrayList<>();
    Socket serverSideClientSocket;
    ServerSocket serverSocket;

    TCPClientCommunication tcpClientCommunication;
    Future<?> acceptingThread;
    @AfterEach
    void close() throws IOException, InterruptedException {
        this.tcpClientConnection.closeConnection();
        this.serverSocket.close();
    }

    @BeforeEach
    @Test
    void constructorTest() throws InterruptedException, ExecutionException {
            try {
                this.serverSocket = new ServerSocket(100);
                serverExecutorService = Executors.newCachedThreadPool();
                this.acceptingThread = serverExecutorService.submit(() -> {
                    try {
                        this.serverSideClientSocket = serverSocket.accept();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            } catch (IOException e) {
                System.err.println(e.getMessage());
            }
            tcpClientConnection = new TCPClientConnection("127.0.0.1", 100, clientNotificationListener);
            tcpClientCommunication = new TCPClientCommunication(tcpClientConnection);
            tcpClientConnection.openConnection();
            acceptingThread.get();
    }
    @Test
    void helloTest() throws InterruptedException, ExecutionException {
        Future<String> receivedMessage = serverExecutorService.submit(() -> receiveFromClient());
        tcpClientCommunication.hello();
        Thread.sleep(500);
        String result = receivedMessage.get();
        assertEquals((new HelloCommand()).toJson(), result.toString());
    }
    public String receiveFromClient() throws IOException {
        try {
            Scanner in;
            in = new Scanner(serverSideClientSocket.getInputStream());
            String json = in.nextLine();
            in.close();
            return json;
        } catch (IOException e) {
                e.printStackTrace();
            if (serverSideClientSocket != null)
                serverSideClientSocket.close();
            return null;
        }
    }
}