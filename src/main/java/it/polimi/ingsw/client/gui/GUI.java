package it.polimi.ingsw.client.gui;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.View;
import it.polimi.ingsw.client.cli.CLIRenderer;

import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GUI extends View {
    private ExecutorService executorService;
    private GUIApplication guiApplication;
    private GUIState guiState;
    public GUI(GUIApplication guiApplication) {
        this.executorService = Executors.newCachedThreadPool();
        this.guiApplication = guiApplication;
    }
    @Override
    public void showError(String error) {
        guiApplication.showPopup(error);
    }
    public void render(){
        System.out.println("Rendering: " + Client.getInstance().getClientState());
        switch (Client.getInstance().getClientState()) {
            case LOGIN -> this.guiState = new GUILogin(guiApplication);
            case CREATE_LOBBY -> this.guiState = new GUICreateLobby(guiApplication, Client.getInstance().getNickname());
            case LOBBY -> this.guiState = new GUILobby(guiApplication, this.getLobbyPlayers(), this.getNumberOfPlayers());
            case CREATE_GAME -> this.guiState = new GUICreateGame(guiApplication);
            case LOAD_GAME -> this.guiState = new GUILoadGame(guiApplication, this.getSavedGames());
            case IN_GAME -> this.guiState = new GUIInGame(guiApplication, this.getLivingRoomBoard(), this.getBookShelves());
            default -> System.out.println("Invalid state");
        }
    }
}
