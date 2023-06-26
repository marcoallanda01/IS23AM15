package it.polimi.ingsw.client.gui;

import it.polimi.ingsw.client.communication.ClientCommunication;

/**
 * Generic GUI state representing a state or a part of the GUI.
 */
public abstract class GUIState {
    protected GUIApplication guiApplication;

    /**
     * The GUI state
     * @param guiApplication the guiApplication to use
     */
    public GUIState(GUIApplication guiApplication) {
        this.guiApplication = guiApplication;
    }
}
