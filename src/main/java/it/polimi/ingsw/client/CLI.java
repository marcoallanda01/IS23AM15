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

    private void clearScreen()
    {
        try
        {
            final String os = System.getProperty("os.name");
            if (os.contains("Windows"))
            {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            }
            else
            {
                System.out.print("\033[H\033[2J");
                System.out.flush();
            }
        }
        catch (Exception e)
        {
            System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
        }
    }

    public void render(){}
    public void showError(String error){}
    public void showChat(){}
}
