package it.polimi.ingsw.client.cli;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.View;

import java.util.Scanner;

public class CLI extends View {
    private final Thread inputThread;
    private final Scanner inputScanner;
    private boolean running;
    private String message;

    public CLI() {
        this.inputScanner = new Scanner(System.in).useDelimiter("\n");
        this.inputThread = new Thread(this::inputHandler);
        this.running = true;
        start();
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
        switch (Client.getInstance().getClientState()) {
            case LOGIN -> {
                CLIRenderer.printGame();
                CLIRenderer.printLogin();
            }
            case CREATE_LOBBY -> CLIRenderer.printCreateLobby();
            case CREATE_GAME -> CLIRenderer.printCreateGame();
            case LOAD_GAME -> CLIRenderer.printSavedGames(this.getSavedGames());
            case LOBBY -> CLIRenderer.printLobby(this.getPlayers(), this.getNumberOfPlayers(), this.isEasyRules());
            case IN_GAME -> {
                CLIRenderer.printLivingRoomBoard(this.getLivingRoomBoard());
                CLIRenderer.printCommonCards(this.getCommonCards());
                CLIRenderer.printBookshelves(this.getBookShelves(), Client.getInstance().getNickname(), this.getCurrentTurnPlayer());
                CLIRenderer.printPickedTiles(this.getPickedTiles());
                CLIRenderer.printPersonalGoal(this.getPersonalGoal());
            }
            case END_GAME -> CLIRenderer.printEndGame(this.getPoints(), this.getWinner());
            default -> System.out.println("Invalid state");
        }
        showError(this.message);
    }
    public void showError(String message){
        this.message = message;
        if(this.message != null){
            CLIRenderer.printError(message);
        }
    }
    public void showChat(){
        CLIRenderer.printChat(this.getChat());
    }
}
