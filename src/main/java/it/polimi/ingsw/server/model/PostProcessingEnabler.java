package it.polimi.ingsw.server.model;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

public class PostProcessingEnabler implements TypeAdapterFactory {
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
