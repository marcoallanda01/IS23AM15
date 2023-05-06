package it.polimi.ingsw.server.listeners;


import it.polimi.ingsw.server.controller.PushNotificationController;

/**
 * Interface used from classes that can be listened to, from standard listeners in "listeners" package
 */
public interface StandardListenable {

    /**
     * Methods that allow the registration of a standard listener. Call it only once.
     * @param pushNotificationController object PushNotificationController is necessary to launch notifications to the
     *                                   clients
     */
    public void setStandardListener(PushNotificationController pushNotificationController);
}
