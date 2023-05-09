package it.polimi.ingsw.client.communication;

import java.util.*;
import java.util.function.Predicate;

public class TCPClientResponseBuffer {
    private List<String> buffer;

    public TCPClientResponseBuffer() {
        this.buffer = new ArrayList<>();
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
    @Override
    public String toString() {
        return "TCPClientResponseBuffer{" +
                "buffer=" + buffer +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TCPClientResponseBuffer that = (TCPClientResponseBuffer) o;
        return Objects.equals(buffer, that.buffer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(buffer);
    }
}
