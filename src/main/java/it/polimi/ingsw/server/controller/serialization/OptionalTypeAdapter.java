package it.polimi.ingsw.server.controller.serialization;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Optional;

/**
 * TypeAdapter for Optional class
 */
public class OptionalTypeAdapter<E> extends TypeAdapter<Optional<E>> {

    /**
     * Factory for OptionalTypeAdapter
     */
    public static final TypeAdapterFactory FACTORY = new TypeAdapterFactory() {
        /**
         * Creates a new instance of the type adapter for {@code type}.
         * @param gson the gson instance
         * @param type the type of object to be deserialized
         * @return a type adapter for {@code type}, or {@code null} if this
         * @param <T> the type of object to be deserialized
         */
        @Override
        public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
            Class<T> rawType = (Class<T>) type.getRawType();
            if (rawType != Optional.class) {
                return null;
            }
            final ParameterizedType parameterizedType = (ParameterizedType) type.getType();
            final Type actualType = parameterizedType.getActualTypeArguments()[0];
            final TypeAdapter<?> adapter = gson.getAdapter(TypeToken.get(actualType));
            return new OptionalTypeAdapter(adapter);
        }
    };
    private final TypeAdapter<E> adapter;

    public OptionalTypeAdapter(TypeAdapter<E> adapter) {
        this.adapter = adapter;
    }

    @Override
    public Optional<E> read(JsonReader in) throws IOException {
        if (in.peek() != JsonToken.NULL) {
            return Optional.ofNullable(adapter.read(in));
        } else {
            in.nextNull();
            return Optional.empty();
        }
    }

    @Override
    public void write(JsonWriter out, Optional<E> value) throws IOException {
        if (value.isPresent()) {
            adapter.write(out, value.get());
        } else {
            out.nullValue();
        }
    }
}