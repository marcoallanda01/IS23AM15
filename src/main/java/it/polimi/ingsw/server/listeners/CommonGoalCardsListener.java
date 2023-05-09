package it.polimi.ingsw.server.listeners;

import it.polimi.ingsw.server.controller.PushNotificationController;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class CommonGoalCardsListener extends StandardListener implements PropertyChangeListener {
    public CommonGoalCardsListener(PushNotificationController pushNotificationController) {
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
        if(proprietyName.equals("cardsToTokens")) {

        }else{
            System.err.println("CommonGoalCardsListener: propriety name "+proprietyName+" not known");
        }
    }
}
