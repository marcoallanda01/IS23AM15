package it.polimi.ingsw.server.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import it.polimi.ingsw.server.model.Adjacent;
import it.polimi.ingsw.server.model.Pattern;
import it.polimi.ingsw.server.model.Personal;
import it.polimi.ingsw.server.model.Specific;

import java.io.IOException;
import java.time.LocalDateTime;

public class PatternTypeAdapter extends TypeAdapter<Pattern> {

    private final Gson gson =
            new GsonBuilder().registerTypeAdapterFactory(OptionalTypeAdapter.FACTORY).registerTypeAdapter(LocalDateTime.class, new DateTimeTypeAdapter()).create();

    @Override
    public void write(JsonWriter jsonWriter, Pattern pattern) throws IOException {
        jsonWriter.beginObject();
        jsonWriter.name("type");
        if (pattern instanceof Adjacent) {
            jsonWriter.value("Adjacent");
            jsonWriter.name("Pattern");
            gson.toJson(pattern, Adjacent.class, jsonWriter);
        } else if (pattern instanceof Personal){
            jsonWriter.value("Personal");
            jsonWriter.name("Pattern");
            gson.toJson(pattern, Personal.class, jsonWriter);
        } else if (pattern instanceof Specific){
            jsonWriter.value("Specific");
            jsonWriter.name("Pattern");
            gson.toJson(pattern, Specific.class, jsonWriter);
        } else {
            throw new JsonSyntaxException("Pattern not recognized");
        }
        jsonWriter.endObject();
    }

    @Override
    public Pattern read(JsonReader jsonReader) throws IOException {
        jsonReader.beginObject();
        Pattern pattern;
        if (jsonReader.nextName().equals("type")) {
            switch (jsonReader.nextString()) {
                case "Adjacent" -> {
                    jsonReader.nextName();
                    pattern = gson.fromJson(jsonReader, Adjacent.class);
                }
                case "Personal" -> {
                    jsonReader.nextName();
                    pattern = gson.fromJson(jsonReader, Personal.class);
                }
                case "Specific" -> {
                    jsonReader.nextName();
                    pattern = gson.fromJson(jsonReader, Specific.class);
                }
                default -> throw new JsonSyntaxException("Pattern not recognized");
            }
        } else {
            throw new JsonSyntaxException("Pattern not recognized");
        }
        jsonReader.endObject();
        return pattern;
    }
}
