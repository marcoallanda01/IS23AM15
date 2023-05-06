package it.polimi.ingsw.server.listeners;

import it.polimi.ingsw.server.controller.PushNotificationController;

public abstract class StandardListener {
    protected final PushNotificationController pnc;
    public StandardListener(PushNotificationController pushNotificationController){
        this.pnc = pushNotificationController;
    }
}
