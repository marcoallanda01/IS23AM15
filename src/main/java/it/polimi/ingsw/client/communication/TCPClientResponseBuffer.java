package it.polimi.ingsw.client.communication;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public class TCPClientResponseBuffer {
    private List<String> buffer;

    public synchronized String getNext() {
        Iterator<String> iterator = buffer.iterator();
        if (iterator.hasNext())
            return buffer.iterator().next();
        return null;
    }
    public synchronized void remove(String string) {
        buffer.remove(string);
    }
    public synchronized void add(String string) {
        buffer.add(string);
    }
    public synchronized Optional<String> getByPredicate(Predicate<String> predicate) {
        return buffer.stream().filter(predicate).findAny();
    }
    public synchronized Optional<String> popByPredicate(Predicate<String> predicate) {
        Optional<String> response = buffer.stream().filter(predicate).findAny();
        if (response.isPresent()) buffer.remove(response);
        return response;
    }
}
