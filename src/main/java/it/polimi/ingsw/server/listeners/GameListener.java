package it.polimi.ingsw.server.listeners;

import it.polimi.ingsw.server.controller.PushNotificationController;
import it.polimi.ingsw.server.model.Game;
import it.polimi.ingsw.server.model.Tile;
import it.polimi.ingsw.server.model.turn.Turn;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.stream.Collectors;

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
        String proprietyName = safePropertyName(evt.getPropertyName());
        System.out.println("\u001B[33m"+"GameListener: "+proprietyName+"\u001B[0m");
        if(proprietyName.equals("currentTurn")) {
            String playerName = (String) evt.getNewValue();
            pnc.notifyTurnChange(playerName);
        } else if(proprietyName.equals("gameStarted")){
            pnc.notifyGameSetUp();
        }
        else if(proprietyName.equals("gameWon")){
            pnc.notifyWinner((String) evt.getNewValue());
        }
        else if(proprietyName.equals("lastPlayerDisconnected")){
            String player = (String) evt.getNewValue();
            System.out.println("\u001B[33m"+"GameListener: last player " + player +" disconnected!" + "\u001B[0m");
            pnc.notifyLastPlayerDisconnection();
        }
        else if(proprietyName.equals("pickedTiles")){
            Turn turn = (Turn) evt.getNewValue();
            System.out.println("\u001B[33m"+"GameListener: player " + turn.getCurrentPlayer().getUserName() +
                    " picked tiles " + turn.getPickedTiles() + "\u001B[0m");
            pnc.notifyPickedTiles(turn.getCurrentPlayer().getUserName(),
                    turn.getPickedTiles().stream().map(Tile::getType).collect(Collectors.toList()));
        }
        else{
            System.err.println("\u001B[33m"+"GameListener: propriety name "+proprietyName+" not known"+"\u001B[0m");
        }
    }
}
