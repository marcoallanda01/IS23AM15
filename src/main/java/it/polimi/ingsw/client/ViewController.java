package it.polimi.ingsw.client;

import it.polimi.ingsw.comunication.ServerController;
import it.polimi.ingsw.server.communication.GameSetUp;

public class ViewController {
    private ServerController serverController;
    private View view;

    public ViewController(View view, ServerController serverController) {
        this.serverController = serverController;
    }

    public void showGame(GameSetUp gameSetUp) {
    }
    public void showWinner(String nickname) {
        System.out.println("winner is: " + nickname);
    }
}
