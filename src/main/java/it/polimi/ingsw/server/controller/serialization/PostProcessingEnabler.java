package it.polimi.ingsw.server.controller.serialization;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

/**
 * TypeAdapterFactory for PostProcessable class
 */
public class PostProcessingEnabler implements TypeAdapterFactory {
    /**
     * Default constructor
     */
    public PostProcessingEnabler() {
    }

    /**
     * Creates a new instance of the type adapter for {@code type}.
     *
     * @param gson the gson instance
     * @param type the type of object to be deserialized
     * @param <T>  the type of object to be deserialized
     * @return a type adapter for {@code type}, or {@code null} if this
     */
    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
        TypeAdapter<T> delegate = gson.getDelegateAdapter(this, type);
        if (PostProcessable.class.isAssignableFrom(type.getRawType())) {
            return new PostProcessingEnablerTypeAdapter(delegate);
        }
        return delegate;
    }

    private static class PostProcessingEnablerTypeAdapter<T> extends TypeAdapter<T> {
        private final TypeAdapter<T> delegate;

        public PostProcessingEnablerTypeAdapter(TypeAdapter<T> delegate) {
            this.delegate = delegate;
        }

        @Override
        public void write(JsonWriter out, T value) throws IOException {
            delegate.write(out, value);
        }

        @Override
        public T read(JsonReader in) throws IOException {
            T result = delegate.read(in);
            if (result instanceof PostProcessable) {
                ((PostProcessable) result).gsonPostProcess();
            }
            return result;
        }
    }
}
