package it.polimi.ingsw.server.model;

import it.polimi.ingsw.server.controller.PushNotificationController;
import it.polimi.ingsw.server.listeners.PlayerListener;
import it.polimi.ingsw.server.listeners.StandardListenable;

import java.beans.PropertyChangeSupport;

public class Player implements StandardListenable {
    private final String userName;
    private final BookShelf bookShelf;
    private boolean firstToFinish = false;
    private transient boolean isPlaying = true;
    private boolean fullBookShelf = false;
    private final transient PropertyChangeSupport propertyChangeSupport;

    /**
     * Create a new player
     * @param userName the username of the player
     */
    public Player(String userName) {
        this.userName = userName;
        this.bookShelf = new BookShelf();
        this.propertyChangeSupport = new PropertyChangeSupport(this);
    }
    public BookShelf getBookShelf() {
        return this.bookShelf;
    }
    /**
     * Get the username of the player
     * @return the username of the player
     */
    public String getUserName() {
        return new String(this.userName);
    }

    /**
     * Get the value of isPlaying
     * @return the value of isPlaying
     */
    public boolean isPlaying() {
        return this.isPlaying;
    }
    /**
     * Set the value of isPlaying
     * @param playing the new value of isPlaying
     */
    public void setPlaying(boolean playing) {
        this.isPlaying = playing;
        this.propertyChangeSupport.firePropertyChange("playingState", null, this.isPlaying);
    }
    /**
     * Get the value of firstToFinish
     * @return the value of firstToFinish
     */
    public boolean isFirstToFinish() {
        return this.firstToFinish;
    }
    /**
     * Set the value of firstToFinish
     * @param firstToFinish the new value of firstToFinish
     */
    public void setFirstToFinish(boolean firstToFinish) {
        this.firstToFinish = firstToFinish;
    }
    /**
     * Get the value of fullBookShelf
     * @return the value of fullBookShelf
     */
    public boolean hasFinished() {
        return this.fullBookShelf;
    }
    /**
     * Set the value of fullBookShelf
     * @param fullBookShelf the new value of fullBookShelf
     */
    public void setFullBookShelf(boolean fullBookShelf) {
        this.fullBookShelf = fullBookShelf;
    }

    /**
     * Set standard player listener
     */
    public void setStandardListener(PushNotificationController pnc){
        this.propertyChangeSupport.addPropertyChangeListener(new PlayerListener(pnc));
    }
}
