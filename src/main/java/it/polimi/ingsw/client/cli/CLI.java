package it.polimi.ingsw.client.cli;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.View;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicInteger;

public class CLI extends View {
    private final Thread inputThread;
    private final Scanner inputScanner;
    private boolean running;

    /**
     * Constructor for CLI
     */
    public CLI() {
        this.inputScanner = new Scanner(System.in).useDelimiter("\n");
        this.inputThread = new Thread(this::inputHandler);
        this.running = true;
        initializeViewAndConnection(5);
    }
    private void initializeViewAndConnection(int countdown) {
        try {
            Client.getInstance().init(this);
            start();
        } catch (Exception e) {
            AtomicInteger retryCount = new AtomicInteger(countdown); // Initial retry count
            System.out.println();
            System.out.print("MyShelfie encountered an error while starting, will retry in: " + retryCount.get());

            TimerTask task = new TimerTask() {
                @Override
                public void run() {
                    int count = retryCount.decrementAndGet();
                    System.out.print(" " + count);
                    if (count == 0) {
                        initializeViewAndConnection(countdown * 2);
                        cancel();
                    }
                }
            };

            Timer timer = new Timer();
            timer.schedule(task, 1000, 1000); // Update the countdown every second
        }
    }
    /**
     * Starts the CLI
     */
    private void start() {
        inputThread.start();
        Client.getInstance().getLogger().log("CLI Started");
        CLIRenderer.printGame();
    }

    /**
     * Stops the CLI
     */
    public void stop(String message) {
        showError(message);
        showError("Enter any key to close");
        running = false;
        Client.getInstance().getClientController().logout();
    }

    /**
     * Clears the screen
     */
    private void clearScreen() {
        try {
            final String os = System.getProperty("os.name");
            if (os.contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                System.out.print("\033[H\033[2J");
                System.out.flush();
            }
        } catch (Exception e) {
            System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
        }
    }

    /**
     * Handles the input from the user
     */
    public void inputHandler() {
        while (running) {
            InputCLI.inputHandler(inputScanner);
        }
        System.out.println("CLI InputHandler Closed");
    }

    /**
     * Renders the CLI based on the current state of the client
     */
    public synchronized void render() {
        Client.getInstance().getLogger().log("Rendering: " + Client.getInstance().getClientState());
        clearScreen();
        switch (Client.getInstance().getClientState()) {
            case LOGIN -> {
                CLIRenderer.printLogin();
            }
            case CREATE_LOBBY -> {
                CLIRenderer.printCreateLobby();
            }
            case CREATE_GAME -> CLIRenderer.printCreateGame();
            case LOAD_GAME -> CLIRenderer.printSavedGames(this.getSavedGames());
            case LOAD_NAMES -> CLIRenderer.printLoadedGameNames(this.getLobbyPlayers());
            case LOBBY -> CLIRenderer.printLobby(this.isEasyRules());
            case IN_GAME -> {
                CLIRenderer.printLivingRoomBoard(this.getLivingRoomBoard());
                CLIRenderer.printCommonCards(this.getCommonCards());
                CLIRenderer.printPoints(this.getPoints());
                CLIRenderer.printBookshelves(this.getPlayers(), this.getBookShelves(), Client.getInstance().getNickname(), this.getCurrentTurnPlayer());
                CLIRenderer.printPickedTiles(this.getPickedTiles());
            }
            case END_GAME -> CLIRenderer.printEndGame(this.getPoints(), this.getWinner());
            default -> Client.getInstance().getLogger().log("Invalid state");
        }
    }

    /**
     * Shows a message to the user
     * @param message the message to show
     */
    public void showError(String message) {
        CLIRenderer.printError(message);
    }

    /**
     * Opens the chat
     */
    public void showChat() {
        CLIRenderer.printChat(this.getChat());
    }

    /**
     * Shows the help
     */
    public void showHelp() {
        CLIRenderer.printHelp();
    }

    /**
     * Shows the goals
     */
    public void showGoals() {
        CLIRenderer.printGoals(this.getCommonGoals(), this.getPersonalGoal());
    }
}
