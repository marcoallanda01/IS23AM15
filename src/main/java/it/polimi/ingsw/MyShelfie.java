package it.polimi.ingsw;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.server.ServerApp;

public class MyShelfie {
    public static void main(String[] args) {
        if (args.length == 0) throw new IllegalArgumentException("Please specify a mode (client or server)");
        LaunchMode mode = parseMode(args);
        switch (mode) {
            case Client -> Client.main(args);
            case Server -> ServerApp.main(args);
        }

    }

    public static LaunchMode parseMode(String[] args) {
        for (String s : args) {
            if (s.equalsIgnoreCase("client")) return LaunchMode.Client;
            if (s.equalsIgnoreCase("server")) return LaunchMode.Server;
        }
        throw new IllegalArgumentException("Invalid arguments");
    }

    private enum LaunchMode {
        Client,
        Server
    }
}
