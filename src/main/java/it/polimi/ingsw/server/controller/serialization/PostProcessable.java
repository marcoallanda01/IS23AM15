package it.polimi.ingsw.server.controller.serialization;

/**
 * Interface that represents an object that needs to be post processed after deserialization
 */
public interface PostProcessable {
    /**
     * Method that is called after deserialization
     */
    default void gsonPostProcess() {
    }
}

