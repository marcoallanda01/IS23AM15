package it.polimi.ingsw.client;

import java.util.Scanner;

public class CLI extends View {
    private final Thread inputThread;
    private final Scanner inputScanner;
    private boolean running;

    public CLI() {
        this.inputScanner = new Scanner(System.in).useDelimiter("\n");
        this.inputThread = new Thread(this::inputHandler);
        this.running = true;
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
        switch (Client.getInstance().getClientState()){
            case LOGIN:
                CLIRenderer.printLogin();
                break;
            case CREATE_LOBBY:
                CLIRenderer.printCreateLobby();
                break;
            case CREATE_GAME:
                CLIRenderer.printCreateGame();
                break;
            case LOAD_GAME:
                CLIRenderer.printSavedGames(this.getSavedGames());
                break;
            case LOBBY:
                CLIRenderer.printLobby(this.getPlayers(), this.getNumberOfPlayers(), this.isEasyRules());
                CLIRenderer.printSavedGames(this.getSavedGames());
                break;
            case IN_GAME:
                CLIRenderer.printLivingRoomBoard(this.getLivingRoomBoard());
                CLIRenderer.printCommonGoals(this.getGoals());

                CLIRenderer.printBookshelves(this.getBookShelves(), this.getNickname(), this.getCurrentTurnPlayer());
                break;
            case END_GAME:
                CLIRenderer.printEndGame(this.getPoints(), this.getWinner());
                break;
            default:
                System.out.println("Invalid state");
                break;
        }
    }
    public void showError(String message){
        CLIRenderer.printError(message);
    }
    public void showChat(){
        CLIRenderer.printChat(this.getChat());
    }
}
