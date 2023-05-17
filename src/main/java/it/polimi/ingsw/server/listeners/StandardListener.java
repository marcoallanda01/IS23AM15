package it.polimi.ingsw.server.listeners;

import it.polimi.ingsw.server.controller.PushNotificationController;
import org.jetbrains.annotations.NotNull;

public abstract class StandardListener {
    protected final PushNotificationController pnc;

    /**
     * Listener where a pushNotificationController is needed
     * @param pushNotificationController pushNotificationController
     */
    public StandardListener(@NotNull PushNotificationController pushNotificationController){
        this.pnc = pushNotificationController;
    }

    /**
     * Safe property name from null value
     * @param propertyName property name
     * @return "null" if propertyName is null, else propertyName is returned
     */
    protected String safePropertyName(String propertyName){
        return propertyName == null ? "null" : propertyName;
    }
}
