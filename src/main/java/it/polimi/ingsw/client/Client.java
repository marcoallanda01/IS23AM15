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

/**
 * Class that represents the client
 */
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

    /**
     * Default constructor
     *
     * @param hostname  the hostname
     * @param port      the port
     * @param goalsPath the path to the goals file
     * @param protocol  the protocol
     * @param view      the view
     */
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

    /**
     * Constructor only used for testing
     */
    public Client() {
        this.hostname = null;
        this.port = 0;
        this.protocolSetting = null;
        this.viewSetting = null;
        this.goalsPath = null;
        this.state = null;
    }

    /**
     * Used only for testing
     */
    public static void main() {
        singleton = new Client();
    }

    /**
     * Gets the instance of the client
     *
     * @return the instance of the client
     */
    public static Client getInstance() {
        return singleton;
    }

    /**
     * The main method of the client
     *
     * @param args the arguments passed to the program
     */
    public static void main(String[] args) {
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

    /**
     * Initializes the view
     *
     * @param view the view to initialize
     */
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

    /**
     * Parses the arguments passed to the program
     *
     * @param args          the arguments
     * @param option        the short option
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

    /**
     * Parses the protocol from the arguments
     *
     * @param args the arguments
     * @return the protocol
     */
    private static Protocols parseProtocol(String[] args) {
        for (String s : args) {
            if (s.equalsIgnoreCase("rmi")) return Protocols.RMI;
            if (s.equalsIgnoreCase("tcp")) return Protocols.TCP;
        }
        return Protocols.RMI; // default
    }

    /**
     * Parses the view from the arguments
     *
     * @param args the arguments
     * @return the view
     */
    private static Views parseView(String[] args) {
        for (String s : args) {
            if (s.equalsIgnoreCase("cli")) return Views.CLI;
            if (s.equalsIgnoreCase("gui")) return Views.GUI;
        }
        return Views.CLI; // default
    }

    private static boolean isTesting(String[] args) {
        for (String s : args) {
            if (s.contains("testing")) return true;
        }
        return false; // default
    }

    /**
     * returns if the client is the first player
     * @return true if the client is the first player
     */
    public boolean isFirstPlayer() {
        return isFirstPlayer;
    }


    /**
     * Sets if the client is the first player
     * @param firstPlayer true if the client is the first player
     */
    public void setFirstPlayer(boolean firstPlayer) {
        isFirstPlayer = firstPlayer;
    }

    /**
     * Setups the RMI connection
     */
    public void setupNetworkRMI() throws RuntimeException {
        try {
            logger.log("RMI setup");
            System.setProperty("java.rmi.server.hostname", hostname);
            RMIClientConnection rmiClientConnection = new RMIClientConnection(hostname, port, clientController);
            singleton.clientConnection = rmiClientConnection;
            singleton.clientCommunication = new RMIClientCommunication(rmiClientConnection);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
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

    /**
     * get the view
     * @return the view
     */
    public View getView() {
        return view;
    }

    /**
     * Gets the client goals
     *
     * @return the client goals
     */
    public Map<String, ClientGoal> getClientGoals() {
        return clientGoals;
    }

    /**
     * get the client state
     * @return the client state
     */
    public ClientStates getClientState() {
        synchronized (state) {
            return state;
        }
    }

    /**
     * Sets the client state
     *
     * @param state the client state
     */
    public void setClientState(ClientStates state) {
        synchronized (state) {
            this.state = state;
        }
    }

    /**
     * Gets the client ID
     *
     * @return the client ID
     */
    public String getId() {
        synchronized (id) {
            return this.id;
        }
    }

    /**
     * Sets the client ID
     *
     * @param id the client ID
     */
    public void setId(String id) {
        synchronized (id) {
            this.id = id;
        }
        saveClientInfo(getId(), getNickname());
    }

    /**
     * Gets the client nickname
     *
     * @return the client nickname
     */
    public String getNickname() {
        synchronized (nickname) {
            return nickname;
        }
    }

    /**
     * Sets the client nickname
     *
     * @param nickname the client nickname
     */
    public void setNickname(String nickname) {
        synchronized (nickname) {
            this.nickname = nickname;
        }
        saveClientInfo(getId(), getNickname());
    }

    /**
     * Saves the client ID to a file
     *
     * @param id   the client ID
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

    /**
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

    /**
     * Gets the client controller
     *
     * @return the client controller
     */
    public ClientController getClientController() {
        if (clientController instanceof ClientController)
            return (ClientController) clientController;
        else {
            RuntimeException e = new RuntimeException("ClientController is not an instance of ClientController");
            Client.getInstance().getLogger().log(e);
            throw e;
        }
    }

    /**
     * Gets the client connection
     *
     * @return the client connection
     */
    public ClientCommunication getClientCommunication() {
        return clientCommunication;
    }

    /**
     * Gets the logger
     *
     * @return the logger
     */
    public Logger getLogger() {
        return logger;
    }

    /**
     * Initializes the client
     *
     * @param view the view
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

    /**
     * Method called upon successful connection establishment
     */
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
        scheduleDisconnect(60);
    }

    /**
     * Schedule a disconnect after 10 seconds
     *
     * @param seconds the seconds after which the disconnect should be scheduled
     */
    public void scheduleDisconnect(int seconds) {
        disconnectTimer.schedule(new DisconnectTask(), seconds * 1000L);
    }

    /**
     * Reset the disconnect timer
     *
     * @param seconds the seconds after which the disconnect should be scheduled
     */
    public void resetDisconnectTimer(int seconds) {
        disconnectTimer.cancel();
        disconnectTimer = new Timer();
        scheduleDisconnect(seconds);
    }

    private enum Protocols {
        RMI, TCP
    }

    private enum Views {
        CLI, GUI
    }

    private static class DisconnectTask extends TimerTask {

        public DisconnectTask() {
        }

        @Override
        public void run() {
            Client.getInstance().getLogger().log("No response from server for too many seconds, try restarting the app...");
            Client.getInstance().getClientController().close("No response from server for too many seconds, try restarting the app...");
        }
    }
}
