package it.polimi.ingsw.server.listeners;

import it.polimi.ingsw.server.controller.PushNotificationController;
import it.polimi.ingsw.server.model.managers.patterns.Pattern;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

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
        String proprietyName = safePropertyName(evt.getPropertyName());
        System.out.println("\u001B[33m"+"CommonGoalCardsListener: "+proprietyName+"\u001B[0m");
        if(proprietyName.equals("CardsToTokensChange")) {
            Map<Pattern, Stack<Integer>> cardToTokens = (Map<Pattern, Stack<Integer>>) evt.getNewValue();
            Map<String, List<Integer>> cardnameToTokens = new HashMap<>();
            cardToTokens.forEach(
                    (p, lt) -> {
                        cardnameToTokens.put(p.getName(), (List<Integer>) lt.clone());
                    }
            );
            this.pnc.notifyCommonGoalsCards(cardnameToTokens);
        }else{
            System.err.println("\u001B[33m"+"CommonGoalCardsListener: propriety name "+proprietyName+" not known"+"\u001B[0m");
        }
    }
}
