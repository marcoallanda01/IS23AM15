package it.polimi.ingsw.server.listeners;

import it.polimi.ingsw.server.controller.PushNotificationController;
import it.polimi.ingsw.server.model.Message;
import it.polimi.ingsw.server.model.Tile;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

public class ChatListener extends StandardListener implements PropertyChangeListener {
    public ChatListener(PushNotificationController pushNotificationController) {
        super(pushNotificationController);
    }

    /**
     * This method gets called when a bound property is changed.
     *
     * @param evt A PropertyChangeEvent object describing the event source
     *            and the property that has changed.
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        String proprietyName = evt.getPropertyName();
        System.out.println("\u001B[33m"+"ChatLister: "+proprietyName+"\u001B[0m");
        if(proprietyName.equals("messageSent")) {
            Message m = (Message) evt.getNewValue();
            pnc.notifyMessage(m.getSenderName(), m.getDate(), m.getContent(), m.getReceiverName());
        }else{
            System.err.println("\u001B[33mChatLister: propriety name "+proprietyName+" not known\u001B[0m");
        }
    }
}