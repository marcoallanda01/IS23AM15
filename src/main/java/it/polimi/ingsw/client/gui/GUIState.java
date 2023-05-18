package it.polimi.ingsw.client.gui;

import it.polimi.ingsw.client.communication.ClientCommunication;

public abstract class GUIState {
    protected GUIApplication guiApplication;
    public GUIState(GUIApplication guiApplication) {
        this.guiApplication = guiApplication;
    }
}
