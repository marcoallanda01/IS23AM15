package it.polimi.ingsw.server.listeners;

import it.polimi.ingsw.server.controller.PushNotificationController;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class TurnListener extends StandardListener implements PropertyChangeListener {

    public TurnListener(PushNotificationController pnc){
        super(pnc);
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
        if(proprietyName.equals("currentTurn")) {
            String playerName = (String) evt.getNewValue();
            pnc.notifyTurnChange(playerName);
        }else{
            System.err.println("TurnListener: propriety name "+proprietyName+" not known");
        }
    }
}
