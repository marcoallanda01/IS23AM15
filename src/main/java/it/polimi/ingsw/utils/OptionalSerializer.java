package it.polimi.ingsw.utils;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;
import java.util.Optional;

/**
 * Serializer for Optional class
 */
public class OptionalSerializer implements JsonSerializer<Optional<?>> {
    /**
     * Default constructor
     */
    public OptionalSerializer() {
    }

    @Override
    public JsonElement serialize(Optional<?> optional, Type type, JsonSerializationContext context) {
        return (optional.isPresent() ? context.serialize(optional.get()) : JsonNull.INSTANCE);
    }
}
