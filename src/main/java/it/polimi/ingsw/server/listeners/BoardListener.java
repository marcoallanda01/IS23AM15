package it.polimi.ingsw.server.listeners;

import it.polimi.ingsw.server.controller.PushNotificationController;
import it.polimi.ingsw.server.model.Tile;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

public class BoardListener extends StandardListener implements PropertyChangeListener {
    public BoardListener(PushNotificationController pushNotificationController) {
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
        String proprietyName = safePropertyName(evt.getPropertyName());

        System.out.println("\u001B[33m"+"BoardLister: "+proprietyName+"\u001B[0m");
        if(proprietyName.equals("removedTiles")) {
            pnc.notifyChangeBoard((List<Tile>)evt.getNewValue());
        }
        else if(proprietyName.equals("addedTiles")){
            pnc.notifyChangeBoard((List<Tile>)evt.getNewValue());
        }else{
            System.err.println("\u001B[33m"+"BoardListener: propriety name "+proprietyName+" not known"+"\u001B[0m");
        }
    }
}
