package it.polimi.ingsw.client;

import it.polimi.ingsw.client.cli.CLI;
import it.polimi.ingsw.client.communication.*;
import it.polimi.ingsw.client.gui.GUIApplication;
import it.polimi.ingsw.utils.Logger;
import javafx.application.Application;

import java.io.*;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class Client {
    private static Client singleton;
    private final String hostname;
    private final int port;
    private final String goalsPath;
    private final Protocols protocolSetting;
    private final Views viewSetting;
    private final Logger logger = new Logger();
    private ClientStates state;
    private View view;
    private ClientNotificationListener clientController;
    private String id = "";
    private boolean isFirstPlayer;
    private String nickname;
    private ClientConnection clientConnection;
    private ClientCommunication clientCommunication;
    private Map<String, ClientGoal> clientGoals;
    private Timer disconnectTimer;

    public Client(String hostname, int port, String goalsPath, Protocols protocol, Views view) {
        this.hostname = hostname;
        this.port = port;
        loadClientInfo();
        this.protocolSetting = protocol;
        this.viewSetting = view;
        this.goalsPath = goalsPath;
        state = ClientStates.STARTUP;
        disconnectTimer = new Timer();
    }

    // used for testing
    public Client() {
        this.hostname = null;
        this.port = 0;
        this.protocolSetting = null;
        this.viewSetting = null;
        this.goalsPath = null;
        this.state = null;
    }

    // used for testing
    public static void main() {
        singleton = new Client();
    }

    public static Client getInstance() {
        return singleton;
    }

    public static void main(String[] args)  //static method
    {
        String hostname = parseArg(args, "-a", "--address");
        String port = parseArg(args, "-p", "--port");
        String goalsPath = parseArg(args, "-g", "--goals");
        Protocols protocol = parseProtocol(args);
        Views view = parseView(args);

        try {
            singleton = new Client(hostname == null ? "localhost" : hostname, port == null ? 6000 : Integer.parseInt(port), goalsPath == null ? "/data/client_goals.json" : goalsPath, protocol, view);
        } catch (NumberFormatException e) {
            System.out.println("Invalid port number");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        initView(view);
    }

    private static void initView(Views view) {
        try {
            if (view.equals(Views.CLI)) {
                new CLI();
            } else if (view.equals(Views.GUI)) {
                Application.launch(GUIApplication.class, "");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    /*
     * Parses the arguments passed to the program
     * @param args the arguments
     * @param option the short option
     * @param optionVerbose the long option
     * @return the value of the option
     */
    private static String parseArg(String[] args, String option, String optionVerbose) {
        for (int i = 0; i < args.length - 1; i++) {
            String arg = args[i];
            if (arg.equalsIgnoreCase(option) || arg.equalsIgnoreCase(optionVerbose)) return args[i + 1];
        }
        return null;
    }

    /*
     * Parses the protocol from the arguments
     * @param args the arguments
     * @return the protocol
     */
    private static Protocols parseProtocol(String[] args) {
        for (String s : args) {
            if (s.contains("rmi")) return Protocols.RMI;
            if (s.contains("tcp")) return Protocols.TCP;
        }
        return Protocols.RMI; // default
    }

    /*
     * Parses the view from the arguments
     * @param args the arguments
     * @return the view
     */
    private static Views parseView(String[] args) {
        for (String s : args) {
            if (s.contains("cli")) return Views.CLI;
            if (s.contains("gui")) return Views.GUI;
        }
        return Views.CLI; // default
    }

    private static boolean isTesting(String[] args) {
        for (String s : args) {
            if (s.contains("testing")) return true;
        }
        return false; // default
    }

    public boolean isFirstPlayer() {
        return isFirstPlayer;
    }

    public void setFirstPlayer(boolean firstPlayer) {
        isFirstPlayer = firstPlayer;
    }

    /*
     * Setups the RMI connection
     */
    public void setupNetworkRMI() throws RuntimeException {
        try {
            logger.log("RMI setup");
            RMIClientConnection rmiClientConnection = new RMIClientConnection(hostname, port, clientController);
            singleton.clientConnection = rmiClientConnection;
            singleton.clientCommunication = new RMIClientCommunication(rmiClientConnection);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /*
     * Setups the TCP connection
     */
    public void setupNetworkTCP() {
        try {
            logger.log("TCP setup");
            TCPClientConnection tcpClientConnection = new TCPClientConnection(hostname, port, clientController);
            singleton.clientConnection = tcpClientConnection;
            singleton.clientCommunication = new TCPClientCommunication(tcpClientConnection);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public View getView() {
        return view;
    }

    public Map<String, ClientGoal> getClientGoals() {
        return clientGoals;
    }

    public ClientStates getClientState() {
        synchronized (state) {
            return state;
        }
    }

    public void setClientState(ClientStates state) {
        synchronized (state) {
            this.state = state;
        }
    }

    public String getId() {
        synchronized (id) {
            return this.id;
        }
    }

    public void setId(String id) {
        synchronized (id) {
            this.id = id;
        }
        saveClientInfo(getId(), getNickname());
    }

    public String getNickname() {
        synchronized (nickname) {
            return nickname;
        }
    }

    public void setNickname(String nickname) {
        synchronized (nickname) {
            this.nickname = nickname;
        }
    }

    /*
     * Saves the client ID to a file
     * @param id the client ID
     * @param name the client name
     */
    private void saveClientInfo(String id, String name) {
        try {
            File clientIdFile = new File("client_info.txt");
            FileWriter fileWriter = new FileWriter(clientIdFile);
            fileWriter.write(id);
            if (name != null) {
                fileWriter.write("\n");
                fileWriter.write(name);
            }
            fileWriter.close();
        } catch (IOException e) {
            logger.log("Error while saving client ID");
            e.printStackTrace();
        }
    }

    /*
     * Loads the client ID from a file
     */
    private void loadClientInfo() {
        String id = "NoId";
        String name = "NoName";
        try {
            File clientIdFile = new File("client_info.txt");
            if (clientIdFile.exists()) {
                FileReader fileReader = new FileReader(clientIdFile);
                BufferedReader bufferedReader = new BufferedReader(fileReader);
                id = bufferedReader.readLine();
                name = bufferedReader.readLine();
                bufferedReader.close();
            }
        } catch (IOException e) {
            logger.log("Error while loading client ID");
            e.printStackTrace();
        }
        this.id = id;
        this.nickname = name;
    }

    public ClientController getClientController() {
        if (clientController instanceof ClientController)
            return (ClientController) clientController;
        else {
            RuntimeException e = new RuntimeException("ClientController is not an instance of ClientController");
            Client.getInstance().getLogger().log(e);
            throw e;
        }
    }

    public ClientCommunication getClientCommunication() {
        return clientCommunication;
    }

    public Logger getLogger() {
        return logger;
    }

    /*
     * Initializes the client
     */
    public void init(View view) {
        this.view = view;
        // clientController must be set before setting up the network, because the connection needs a reference to it
        try {
            singleton.clientController = new ClientController(goalsPath);
        } catch (Exception e) {
            logger.log("Error while instantiating the ClientController, try restarting the app");
            throw e;
        }
        if (protocolSetting.equals(Protocols.RMI)) {
            singleton.setupNetworkRMI();
        } else if (protocolSetting.equals(Protocols.TCP)) {
            singleton.setupNetworkTCP();
        }
        try {
            singleton.clientConnection.openConnection();
        } catch (ClientConnectionException e) {
            logger.log("Error while opening the connection, try restarting the app");
            throw e;
        }
    }

    public void onConnectionReady() {
        try {
            Client.getInstance().getClientCommunication().hello();
            Client.getInstance().getClientCommunication().reconnect(Client.getInstance().getId());
        } catch (Exception e) {
            Client.getInstance().getLogger().log("Error while checking lobby status: ");
            Client.getInstance().getLogger().log(e);
        }
        try {
            Client.getInstance().getClientCommunication().reconnect(Client.getInstance().getId());
        } catch (Exception e) {
            Client.getInstance().getLogger().log("Error while attempting to reconnect: ");
            Client.getInstance().getLogger().log(e);
        }
        try {
            Thread shutdownHook = new Thread(() -> {
                Client.getInstance().getLogger().log("Shutting down");
                Client.getInstance().getClientController().logout();
            });
            Runtime.getRuntime().addShutdownHook(shutdownHook);
        } catch (Exception e) {
            Client.getInstance().getLogger().log("Error while adding the shutdown hook: ");
            Client.getInstance().getLogger().log(e);
        }
        scheduleDisconnect();
    }

    /*
     * Schedule a disconnect after 10 seconds
     */
    public void scheduleDisconnect() {
        disconnectTimer.schedule(new DisconnectTask(), 10 * 1000);
    }

    /*
     * Reset the disconnect timer
     */
    public void resetDisconnectTimer() {
        disconnectTimer.cancel();
        disconnectTimer = new Timer();
        scheduleDisconnect();
    }

    private enum Protocols {
        RMI, TCP
    }

    private enum Views {
        CLI, GUI
    }

    private static class DisconnectTask extends TimerTask {

        public DisconnectTask() {}

        @Override
        public void run() {
            Client.getInstance().getLogger().log("No response from server for 10 seconds, try restarting the app...");
            Client.getInstance().getClientController().logout("No response from server for 10 seconds, try restarting the app...");
        }
    }
}
