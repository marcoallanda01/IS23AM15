package it.polimi.ingsw.server.listeners;

import it.polimi.ingsw.server.controller.PushNotificationController;
import it.polimi.ingsw.server.controller.SaveException;
import it.polimi.ingsw.server.model.Player;
import it.polimi.ingsw.server.model.Tile;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

public class PlayerListener extends StandardListener implements PropertyChangeListener {
    public PlayerListener(PushNotificationController pnc){
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
        if(proprietyName.equals("playingState")) {
            String playerName = ((Player) evt.getSource()).getUserName();
            if ((boolean) evt.getNewValue()) {
                pnc.notifyReconnection(playerName);
            } else {
                pnc.notifyDisconnection(playerName);
            }
        }
        else if(proprietyName.equals("pointsUpdate")) {
            String playerName = ((Player) evt.getSource()).getUserName();
            if ((int) evt.getOldValue() != (int) evt.getNewValue()) {
                pnc.updatePlayerPoints(playerName, (int) evt.getNewValue());
            }
        }
        else if(proprietyName.equals("bookShelfChange")) {
                String playerName = ((Player) evt.getSource()).getUserName();
                pnc.notifyChangeBookShelf(playerName, (List<Tile>) evt.getNewValue() );
        }else{
            System.err.println("PlayerListener: propriety name "+proprietyName+" not known");
        }
    }
}
