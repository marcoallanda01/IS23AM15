package it.polimi.ingsw.client.communication;

import it.polimi.ingsw.client.View;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


class TCPClientClientConnectionTest {
    ClientNotificationListener clientNotificationListener;
    TCPClientClientConnection tcpClientConnection;
    @Test
    void constructorTest() {
        tcpClientConnection = new TCPClientClientConnection("abc", 100, clientNotificationListener);
    }
    @Test
    void openConnectionTest() {
    }
}