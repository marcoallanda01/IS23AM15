package it.polimi.ingsw;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.server.ServerApp;

/**
 * Main class to launch the application
 */
public class MyShelfie {
    /**
     * Default constructor
     */
    public MyShelfie() {
    }

    /**
     * Public main method to launch client or server
     *
     * @param args arguments necessary for launching client or server
     * @throws IllegalArgumentException if no launch modality were found
     */
    public static void main(String[] args) {
        if (args.length == 0) throw new IllegalArgumentException("Please specify a mode (client or server)");
        LaunchMode mode = parseMode(args);
        switch (mode) {
            case Client -> Client.main(args);
            case Server -> {
                String[] argsS = new String[args.length - 1];
                System.arraycopy(args, 1, argsS, 0, args.length - 1);
                ServerApp.main(args);
            }
        }

    }

    private static LaunchMode parseMode(String[] args) {
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
