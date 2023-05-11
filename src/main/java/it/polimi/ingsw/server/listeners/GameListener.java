package it.polimi.ingsw.server.listeners;

import it.polimi.ingsw.server.controller.PushNotificationController;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class GameListener extends StandardListener implements PropertyChangeListener {

    public GameListener(PushNotificationController pnc){
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
        System.out.println("\u001B[33m"+"GameListener: "+proprietyName+"\u001B[0m");
        if(proprietyName.equals("currentTurn")) {
            String playerName = (String) evt.getNewValue();
            pnc.notifyTurnChange(playerName);
        }
        else if(proprietyName.equals("gameStarted")){
            pnc.notifyGameSetUp();
        }
        else if(proprietyName.equals("gameWon")){
            pnc.notifyWinner((String) evt.getNewValue());
        }
        else{
            System.err.println("TurnListener: propriety name "+proprietyName+" not known");
        }
    }
}
