package it.polimi.ingsw.utils;

import java.lang.ref.PhantomReference;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;

public class ObjectCleaner {
    private final ReferenceQueue<Object> referenceQueue = new ReferenceQueue<>();
    private final PhantomReference<Object> phantomReference;
    private boolean isObjectGarbageCollected = false;

    public ObjectCleaner(Object object) {
        this.phantomReference = new PhantomReference<>(object, referenceQueue);

        Thread referenceQueueProcessingThread = new Thread(() -> {
            while (!isObjectGarbageCollected) {
                try {
                    Reference<?> reference = referenceQueue.remove();
                    if (reference == phantomReference) {
                        isObjectGarbageCollected = true;
                        onObjectGarbageCollected();
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

    public boolean isObjectGarbageCollected() {
        return isObjectGarbageCollected;
    }

    protected void onObjectGarbageCollected() {
        System.out.println("garbage collected");
    }
}