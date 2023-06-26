package it.polimi.ingsw.utils;

import java.lang.ref.PhantomReference;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;

/**
 * Class used in testing to ensure that an object is garbage collected,
 * only used in testing
 */
public class ObjectCleaner {
    private final ReferenceQueue<Object> referenceQueue = new ReferenceQueue<>();
    private final PhantomReference<Object> phantomReference;
    private boolean isObjectGarbageCollected = false;

    /**
     * Constructor for object cleaner
     * @param object the object to check for garbage collection
     */
    public ObjectCleaner(Object object) {
        this.phantomReference = new PhantomReference<>(object, referenceQueue);

        Thread referenceQueueProcessingThread = new Thread(() -> {
            while (!isObjectGarbageCollected) {
                try {
                    Reference<?> reference = referenceQueue.remove();
                    if (reference == phantomReference) {
                        isObjectGarbageCollected = true;
                        break;
                    }
                } catch (InterruptedException e) {
                    // Do nothing
                }
            }
        });
        referenceQueueProcessingThread.setDaemon(true);
        referenceQueueProcessingThread.start();
    }

    /**
     * Used to understand if the given object has been garbage collected
     * @return true if the object has been garbage collected
     */
    public boolean isObjectGarbageCollected() {
        return isObjectGarbageCollected;
    }
}