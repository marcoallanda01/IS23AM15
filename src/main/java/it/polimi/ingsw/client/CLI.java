package it.polimi.ingsw.client;

import it.polimi.ingsw.client.communication.ClientCommunication;
import it.polimi.ingsw.communication.responses.GameSetUp;
import it.polimi.ingsw.server.model.Tile;

import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class CLI extends View {
    private Client client;

    private Thread inputThread;
    private Scanner inputScanner;
    private boolean running;

    private CLIRenderer renderHelper;

    public CLI() {
        this.inputScanner = new Scanner(System.in).useDelimiter("\n");
        this.inputThread = new Thread(this::inputHandler);
        this.running = true;
        this.renderHelper = new CLIRenderer();
    }

    public void attach(Client client) {
        this.client = client;
    }

    public void start() {
        inputThread.start();
        render();
        System.out.println("CLI started");
    }

    public void stop() {
        running = false;
    }

    private void clearScreen(){
        try {
            final String os = System.getProperty("os.name");
            if (os.contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            }
            else {
                System.out.print("\033[H\033[2J");
                System.out.flush();
            }
        }
        catch (Exception e) {
            System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
        }
    }
    public void inputHandler() {
        while(running){
            InputCLI.inputHandler(inputScanner);
        }
        System.out.println("CLI InputHandler Closed");
    }

    public void render(){
        clearScreen();
        switch (client.getClientState()){
            case LOGIN:
                renderHelper.printLogin();
                break;
            case CREATE_LOBBY:
                renderHelper.printCreateLobby();
                break;
            case LOBBY:
                renderHelper.printLobby();
                renderHelper.printSavedGames();
                break;
            case IN_GAME:
                renderHelper.printLivingRoomBoard();
                renderHelper.printBookshelves();

                break;
            case END_GAME:
                break;
            default:
                System.out.println("Invalid state");
                break;
        }
    }
    public void showError(){
        renderHelper.printError();
    }
    public void showChat(){
        renderHelper.printChat();
    }
}
