package it.polimi.ingsw.server.model.managers;

/**
 * the frequency to which the PointsManager MUST be updated
 */
public enum UpdateRule {
    /**
     * the PointsManager MUST be updated every end turn
     */
    END_TURN,
    /**
     * the PointsManager MUST be updated only in end game
     */
    END_GAME,
    /**
     * the PointsManager can be updated in both cases depending on the preference
     */
    ANY
}
